# round-result-throws-import Specification

## Purpose
Define the import, persistence, completion, and restart behavior for round-result throw timelines.

## Requirements

### Requirement: Throw import selects all unimported round results

The system SHALL select every round result whose `throws_imported` value is false, regardless of whether `pdga_score_id` is null.

#### Scenario: Unimported result with score identifier is selected
- **WHEN** a round result has `throws_imported = false` and a non-null `pdga_score_id`
- **THEN** the throw import includes that round result and requests its timeline

#### Scenario: Unimported result without score identifier is selected
- **WHEN** a round result has `throws_imported = false` and `pdga_score_id = null`
- **THEN** the throw import includes that round result without requesting a remote timeline

#### Scenario: Already imported result is skipped
- **WHEN** a round result has `throws_imported = true`
- **THEN** the throw import excludes that round result

### Requirement: Throw timeline is fetched using the PDGA score identifier

The system SHALL request the PDGA throw-timeline endpoint using the selected round result's non-null `pdga_score_id` as the path parameter.

#### Scenario: Timeline request is made for a selected result
- **WHEN** an eligible result has a non-null `pdga_score_id`
- **THEN** the connector requests `/throw-timelines/{pdga_score_id}` for that result

### Requirement: Individual throws are persisted as normalized fields

The system SHALL persist one `round_result_throws` row for each non-empty `holeThrows` entry, using the containing `liveHoleScore.holeOrdinal` as `hole_ordinal` and mapping all throw fields into separate columns.

#### Scenario: Complete throw entry is persisted
- **WHEN** a successful response contains a `holeThrows` entry with `scoreOrdinal`, `penaltyCount`, and `liveScoreThrow`
- **THEN** the system stores one row with the round-result ID, hole ordinal, score ordinal, penalty count, `throwId`, `zoneId`, `distanceToTarget`, `penaltyType`, `dropZoneId`, and `dropDistanceToTarget`

#### Scenario: Optional throw fields are null
- **WHEN** a throw entry contains null optional values such as distance, penalty type, or drop-zone data
- **THEN** the system persists those corresponding columns as null without rejecting the throw

#### Scenario: Empty score throws collection
- **WHEN** a successful response has an absent or empty `scoreThrows` collection
- **THEN** the system creates no throw rows and marks the round result imported

#### Scenario: Empty hole throws collection
- **WHEN** a `scoreThrows` entry has an absent or empty `holeThrows` collection
- **THEN** the system creates no rows for that hole and continues processing the round result

### Requirement: Round results with null score identifiers are completed

The system SHALL mark an unimported round result with `pdga_score_id = null` as `throws_imported = true` without making a PDGA timeline request or creating throw rows.

#### Scenario: Null score identifier is completed locally
- **WHEN** an unimported round result has no PDGA score identifier
- **THEN** the system creates no throw rows and commits `throws_imported = true`

### Requirement: Each round result is imported atomically

The system SHALL persist all normalized throw rows for one round result and set its `throws_imported` flag to true in the same transaction.

#### Scenario: Round result import succeeds
- **WHEN** the timeline response or null-score completion is successfully processed
- **THEN** all required rows and the completion flag are committed together

#### Scenario: Persistence fails
- **WHEN** saving throw rows or updating the completion flag fails
- **THEN** the transaction rolls back and the round result remains eligible for a later run

#### Scenario: Timeline request fails
- **WHEN** a non-null-score timeline request fails or returns no usable response data
- **THEN** the round result is not marked imported and later eligible runs can retry it

### Requirement: Throw import is restartable

The system SHALL commit completed round-result imports independently so that stopping and restarting preserves prior successful results.

#### Scenario: Import resumes after interruption
- **WHEN** the process stops after some round results have been marked imported and then starts again
- **THEN** previously imported results are skipped and remaining unimported results are attempted

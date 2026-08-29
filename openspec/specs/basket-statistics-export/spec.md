# basket-statistics-export Specification

## Purpose
TBD - created by archiving change add-static-basket-statistics-export. Update Purpose after archive.
## Requirements
### Requirement: Local basket statistics export page
The system SHALL provide a local administration page for exporting static basket statistics data.

#### Scenario: Export page is available
- **WHEN** the local user opens the basket statistics export administration page
- **THEN** the system displays an Export button

#### Scenario: Export is triggered explicitly
- **WHEN** the local user submits the export action
- **THEN** the system generates a basket statistics snapshot file

### Requirement: Static statistics snapshot file
The system SHALL write the basket statistics snapshot as human-readable JSON under the GitHub Pages data directory.

#### Scenario: Snapshot is written to docs data
- **WHEN** the local user triggers a statistics export
- **THEN** the system writes `docs/data/statistics.json`

#### Scenario: Data directory is created
- **WHEN** `docs/data` does not exist during export
- **THEN** the system creates the directory before writing the snapshot

#### Scenario: Existing snapshot is overwritten
- **WHEN** `docs/data/statistics.json` already exists during export
- **THEN** the system overwrites it with the newly exported snapshot

### Requirement: Minimal exported score samples
The system SHALL export the minimum score sample data needed by the static page to filter by rating and aggregate by basket variation.

#### Scenario: Mapped rated score is exported
- **WHEN** a hole score belongs to a round result with a non-null `round_result.rating` and the round-division hole has a basket variation mapping
- **THEN** the snapshot includes a score sample with competition identity, basket identity, basket label, variation identity, variation label, rating, and score

#### Scenario: Player identity is excluded
- **WHEN** a score sample is exported
- **THEN** the sample does not include player name, PDGA number, profile URL, or other player-identifying fields

#### Scenario: Variation distance is included in label
- **WHEN** an exported basket variation has a distance
- **THEN** the exported variation label includes the distance in brackets

### Requirement: Export eligibility filtering
The system SHALL exclude data that cannot be displayed by the static basket statistics page.

#### Scenario: Unrated score is ignored
- **WHEN** a hole score belongs to a round result with null `round_result.rating`
- **THEN** the score is not exported as a sample

#### Scenario: Unmapped score is ignored
- **WHEN** a hole score has no basket variation mapping for its round division and hole ordinal
- **THEN** the score is not exported as a sample

#### Scenario: Empty competition is hidden from export
- **WHEN** a competition has no mapped score samples with non-null ratings
- **THEN** the competition is not included in the exported competition list

### Requirement: Export diagnostics
The system SHALL report diagnostic counts for the local export operation.

#### Scenario: Export result displays diagnostics
- **WHEN** the local user completes a statistics export
- **THEN** the administration page displays counts for exported samples, ignored unrated scores, ignored unmapped scores, and included competitions

#### Scenario: Snapshot includes metadata
- **WHEN** the system writes the statistics snapshot
- **THEN** the snapshot includes export time and diagnostic metadata


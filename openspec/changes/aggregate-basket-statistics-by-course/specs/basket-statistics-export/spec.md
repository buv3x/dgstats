## MODIFIED Requirements

### Requirement: Static statistics snapshot file
The system SHALL write basket statistics data as human-readable JSON under the GitHub Pages data directory using a manifest file and course-scoped statistics files.

#### Scenario: Manifest is written to docs data
- **WHEN** the local user triggers a statistics export
- **THEN** the system writes `docs/data/statistics.json` as the basket statistics manifest

#### Scenario: Course files are written to docs data
- **WHEN** the local user triggers a statistics export and at least one basket course has eligible statistics
- **THEN** the system writes one course statistics file per included basket course under `docs/data/courses/`

#### Scenario: Data directories are created
- **WHEN** `docs/data` or `docs/data/courses` does not exist during export
- **THEN** the system creates the needed directories before writing statistics files

#### Scenario: Existing manifest is overwritten
- **WHEN** `docs/data/statistics.json` already exists during export
- **THEN** the system overwrites it with the newly exported manifest

#### Scenario: Existing course file is overwritten
- **WHEN** a course statistics file already exists for an included basket course during export
- **THEN** the system overwrites it with the newly exported course statistics file

### Requirement: Minimal exported score samples
The system SHALL export the minimum score sample data needed by the static page to filter by rating and aggregate by basket variation for a selected basket course.

#### Scenario: Mapped rated score is exported
- **WHEN** a hole score belongs to a round result with a non-null `round_result.rating` and the round-division hole has a basket variation mapping
- **THEN** the matching course statistics file includes a score sample with competition identity, basket course identity, basket identity, basket label, variation identity, variation label, rating, and score

#### Scenario: Player identity is excluded
- **WHEN** a score sample is exported
- **THEN** the sample does not include player name, PDGA number, profile URL, or other player-identifying fields

#### Scenario: Variation distance is included in label
- **WHEN** an exported basket variation has a distance
- **THEN** the exported variation label includes the distance in brackets

#### Scenario: Manifest lists courses with statistics
- **WHEN** the system writes the basket statistics manifest
- **THEN** the manifest includes each basket course with eligible statistics using course identity, course name, sample count, and relative course file path

### Requirement: Export eligibility filtering
The system SHALL exclude data that cannot be displayed by the static basket statistics page.

#### Scenario: Unrated score is ignored
- **WHEN** a hole score belongs to a round result with null `round_result.rating`
- **THEN** the score is not exported as a sample

#### Scenario: Unmapped score is ignored
- **WHEN** a hole score has no basket variation mapping for its round division and hole ordinal
- **THEN** the score is not exported as a sample

#### Scenario: Empty basket course is hidden from export
- **WHEN** a basket course has no mapped score samples with non-null ratings
- **THEN** the basket course is not included in the exported course list

### Requirement: Export diagnostics
The system SHALL report diagnostic counts for the local export operation.

#### Scenario: Export result displays diagnostics
- **WHEN** the local user completes a statistics export
- **THEN** the administration page displays counts for exported samples, ignored unrated scores, ignored unmapped scores, included basket courses, and generated course files

#### Scenario: Snapshot includes metadata
- **WHEN** the system writes the statistics manifest
- **THEN** the manifest includes export time and diagnostic metadata

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

### Requirement: Basket sliding-window statistics export
The system SHALL export precomputed basket variation sliding-window statistics for the static Basket stats view.

#### Scenario: Basket stats files are written to docs data
- **WHEN** the local user triggers a statistics export and at least one basket course has eligible basket sliding-window statistics
- **THEN** the system writes one basket stats file per included basket course under `docs/data/basket-stats/`

#### Scenario: Basket stats directory is created
- **WHEN** `docs/data/basket-stats` does not exist during export
- **THEN** the system creates the directory before writing basket stats files

#### Scenario: Existing basket stats file is overwritten
- **WHEN** a basket stats file already exists for an included basket course during export
- **THEN** the system overwrites it with the newly exported basket stats file

#### Scenario: Manifest includes basket stats path
- **WHEN** the system writes the basket statistics manifest
- **THEN** each course with eligible basket sliding-window statistics includes a relative `basketStatsPath` to its basket stats file

#### Scenario: Basket stats file contains variation descriptors
- **WHEN** the system writes a basket stats file
- **THEN** it includes each eligible basket variation using basket identity, basket label, variation identity, variation label, total sample count, and precomputed windows

#### Scenario: Player identity is excluded from basket stats
- **WHEN** basket sliding-window statistics are exported
- **THEN** the basket stats file does not include player name, PDGA number, profile URL, or other player-identifying fields

### Requirement: Basket sliding-window calculation
The system SHALL calculate basket variation windows using fixed rating-window rules and SPR/VAR definitions.

#### Scenario: Windows use fixed size and step
- **WHEN** basket sliding-window statistics are calculated
- **THEN** the system evaluates inclusive 50-rating-point windows with a 5-rating-point step

#### Scenario: Windows use shared rating grid
- **WHEN** basket sliding-window statistics are calculated for any basket variation
- **THEN** window starts are aligned to the shared grid `0, 5, 10, ...`

#### Scenario: Window midpoint is exported
- **WHEN** a basket sliding-window statistic is exported
- **THEN** it includes the window rating midpoint as `ratingMidpoint`

#### Scenario: Sparse windows are omitted
- **WHEN** a rating window contains fewer than 50 matching score samples for a basket variation
- **THEN** the system omits that window from the exported basket stats

#### Scenario: Windows without rating variance are omitted
- **WHEN** a rating window contains at least 50 score samples but all samples have the same rating
- **THEN** the system omits that window from the exported basket stats

#### Scenario: Window SPR is calculated
- **WHEN** a basket sliding-window statistic is exported
- **THEN** its SPR is `-100` multiplied by the linear regression slope of score over rating for samples in that window

#### Scenario: Window VAR is calculated
- **WHEN** a basket sliding-window statistic is exported
- **THEN** its VAR is the average absolute difference between actual score and the score expected from that window's regression line

#### Scenario: Count bucket is exported
- **WHEN** a basket sliding-window statistic is exported
- **THEN** it includes a count bucket of `50-99`, `100-199`, or `200+` based on the window sample count

#### Scenario: Variations without eligible windows are omitted
- **WHEN** a basket variation has no windows with at least 50 score samples and rating variance
- **THEN** the system omits that basket variation from the exported basket stats file

### Requirement: Basket statistics export administration navigation
The system SHALL display a top administration navigation menu on the basket statistics export administration page.

#### Scenario: Export page has top navigation
- **WHEN** the local user opens the basket statistics export administration page
- **THEN** the page displays navigation links to `/basket-courses`, `/basket-variation-mappings`, and `/basket-statistics-export`

#### Scenario: Export page behavior is preserved
- **WHEN** the local user uses the basket statistics export page after navigation is added
- **THEN** the existing export action, success diagnostics, and validation feedback continue to work


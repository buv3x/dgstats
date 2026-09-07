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
The system SHALL export precomputed basket variation sliding-window statistics for the static Basket stats view using SPRW as the only exported window metric.

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
- **THEN** it includes each eligible basket variation using basket identity, basket label, variation identity, variation label, total sample count, and precomputed SPRW windows

#### Scenario: Basket stats window exposes SPRW fields
- **WHEN** the system writes a basket stats window
- **THEN** the window includes `ratingFrom`, `ratingTo`, `ratingMidpoint`, `count`, `sprw`, `sprwCount`, and `sprwCountBucket`

#### Scenario: Raw Basket stats metrics are omitted
- **WHEN** the system writes a basket stats window
- **THEN** the window does not include `spr`, `var`, `spr2`, `spr2Count`, `spr2CountBucket`, or `countBucket`

#### Scenario: Player identity is excluded from basket stats
- **WHEN** basket sliding-window statistics are exported
- **THEN** the basket stats file does not include player name, PDGA number, profile URL, or other player-identifying fields

### Requirement: Basket sliding-window calculation
The system SHALL calculate basket variation windows using fixed rating-window rules and the weighted SPRW definition.

#### Scenario: Windows use fixed size and step
- **WHEN** basket sliding-window statistics are calculated
- **THEN** the system evaluates inclusive 50-rating-point windows with a 5-rating-point step for window placement and raw sample count

#### Scenario: Windows use shared rating grid
- **WHEN** basket sliding-window statistics are calculated for any basket variation
- **THEN** window starts are aligned to the shared grid `0, 5, 10, ...`

#### Scenario: Window midpoint is exported
- **WHEN** a basket sliding-window statistic is exported
- **THEN** it includes the window rating midpoint as `ratingMidpoint`

#### Scenario: Sparse windows are omitted
- **WHEN** a rating window contains fewer than 50 matching score samples for a basket variation
- **THEN** the system omits that window from the exported basket stats

#### Scenario: Window SPRW is calculated from weighted rating range
- **WHEN** a basket sliding-window statistic is exported with rating midpoint `M`
- **THEN** its `sprw` is `-100` multiplied by the weighted linear regression slope of score over rating for samples with rating between `M - 50` and `M + 50`, inclusive

#### Scenario: SPRW weights decrease linearly from midpoint
- **WHEN** a sample is included in the SPRW source range for midpoint `M`
- **THEN** its regression weight is `max(0, 1 - abs(rating - M) / 50)`

#### Scenario: SPRW weighted count is exported
- **WHEN** a basket sliding-window statistic is exported
- **THEN** it includes `sprwCount` equal to the sum of SPRW regression weights

#### Scenario: SPRW count bucket is exported
- **WHEN** a basket sliding-window statistic is exported
- **THEN** it includes `sprwCountBucket` of `50-99`, `100-199`, or `200+` based on `sprwCount`

#### Scenario: Sparse weighted SPRW windows are omitted
- **WHEN** an otherwise eligible Basket stats window has SPRW weighted count below 50
- **THEN** the system omits that window from the exported basket stats

#### Scenario: Windows without weighted rating variance are omitted
- **WHEN** an otherwise eligible Basket stats window has SPRW weighted count at least 50 but weighted rating variance is zero
- **THEN** the system omits that window from the exported basket stats

#### Scenario: Variations without eligible windows are omitted
- **WHEN** a basket variation has no windows with enough raw samples, enough weighted SPRW count, and weighted rating variance
- **THEN** the system omits that basket variation from the exported basket stats file

### Requirement: Personal statistics export files
The system SHALL export personal basket statistics data as human-readable JSON under the GitHub Pages data directory.

#### Scenario: Player lookup file is written
- **WHEN** the local user triggers a statistics export and at least one player has eligible personal statistics
- **THEN** the system writes `docs/data/players.json`

#### Scenario: Personal stats directory is created
- **WHEN** `docs/data/personal-stats` does not exist during export
- **THEN** the system creates the directory before writing personal statistics files

#### Scenario: Personal stats files are written
- **WHEN** the local user triggers a statistics export and a player has eligible personal statistics
- **THEN** the system writes one personal statistics file for that player under `docs/data/personal-stats/`

#### Scenario: Existing personal files are overwritten
- **WHEN** a personal statistics file already exists for an eligible player during export
- **THEN** the system overwrites it with the newly exported personal statistics file

#### Scenario: Manifest includes personal statistics paths
- **WHEN** the system writes the basket statistics manifest
- **THEN** the manifest includes the relative player lookup path and the relative personal statistics file path template

### Requirement: Personal statistics export player identity
The system SHALL export only the player identity fields needed for personal statistics lookup and display.

#### Scenario: Eligible player identity is exported
- **WHEN** a player has at least one eligible personal statistics row
- **THEN** the player lookup file includes player id, player name, PDGA number when available, display label, and personal statistics file path

#### Scenario: Ineligible player identity is excluded
- **WHEN** a player has no eligible personal statistics rows
- **THEN** the player lookup file does not include that player

#### Scenario: Unneeded player identity is excluded
- **WHEN** player lookup or personal statistics files are exported
- **THEN** they do not include player profile URL, city, country, nationality, or other player-identifying fields beyond id, name, PDGA number, and display label

### Requirement: Personal statistics export rows
The system SHALL export eligible personal basket variation rows with calculated ratings and score summaries.

#### Scenario: Personal row contains descriptors
- **WHEN** a personal basket variation row is exported
- **THEN** it includes basket course identity and name, basket identity and label, variation identity and label, global sample count, personal result count, decimal calculated rating, rounded display rating, and scores

#### Scenario: Personal scores preserve chronological order
- **WHEN** scores are exported for a personal basket variation row
- **THEN** they are sorted by round date ascending and round id ascending when dates are equal

#### Scenario: Personal rows are exported in display order
- **WHEN** a personal statistics file is written
- **THEN** its variation rows are ordered by decimal calculated rating from highest to lowest

### Requirement: Personal statistics export diagnostics
The system SHALL report diagnostic counts for the personal statistics export.

#### Scenario: Export diagnostics include personal files
- **WHEN** the local user completes a statistics export
- **THEN** the administration page displays counts for eligible personal players and generated personal statistics files

#### Scenario: Snapshot metadata includes personal diagnostics
- **WHEN** the system writes the statistics manifest
- **THEN** the manifest diagnostic metadata includes eligible personal player count and generated personal statistics file count

### Requirement: Basket statistics export administration navigation
The system SHALL display a top administration navigation menu on the basket statistics export administration page.

#### Scenario: Export page has top navigation
- **WHEN** the local user opens the basket statistics export administration page
- **THEN** the page displays navigation links to `/basket-courses`, `/basket-variation-mappings`, and `/basket-statistics-export`

#### Scenario: Export page behavior is preserved
- **WHEN** the local user uses the basket statistics export page after navigation is added
- **THEN** the existing export action, success diagnostics, and validation feedback continue to work

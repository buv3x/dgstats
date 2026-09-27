## MODIFIED Requirements

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

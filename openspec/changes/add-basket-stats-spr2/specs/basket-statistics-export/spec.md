## ADDED Requirements

### Requirement: Basket SPR2 weighted-window export
The system SHALL export SPR2 weighted-regression statistics for Basket Stats windows without changing the existing SPR calculation.

#### Scenario: Window SPR2 is calculated from weighted rating range
- **WHEN** a basket sliding-window statistic is exported with rating midpoint `M`
- **THEN** its SPR2 is `-100` multiplied by the weighted linear regression slope of score over rating for samples with rating between `M - 50` and `M + 50`, inclusive

#### Scenario: SPR2 weights decrease linearly from midpoint
- **WHEN** a sample is included in the SPR2 source range for midpoint `M`
- **THEN** its regression weight is `max(0, 1 - abs(rating - M) / 50)`

#### Scenario: SPR2 weighted count is exported
- **WHEN** a basket sliding-window statistic is exported with SPR2
- **THEN** it includes `spr2Count` equal to the sum of SPR2 regression weights

#### Scenario: SPR2 count bucket is exported
- **WHEN** a basket sliding-window statistic is exported with SPR2
- **THEN** it includes `spr2CountBucket` of `50-99`, `100-199`, or `200+` based on `spr2Count`

#### Scenario: Sparse weighted SPR2 is omitted for a window
- **WHEN** an otherwise exported Basket Stats window has SPR2 weighted count below 50
- **THEN** the window does not include an SPR2 value or SPR2 count bucket

#### Scenario: SPR2 without weighted rating variance is omitted for a window
- **WHEN** an otherwise exported Basket Stats window has SPR2 weighted count at least 50 but weighted rating variance is zero
- **THEN** the window does not include an SPR2 value or SPR2 count bucket

#### Scenario: Existing SPR semantics are preserved
- **WHEN** basket sliding-window statistics are calculated
- **THEN** the existing SPR value still uses the unweighted linear regression over the existing 50-rating-point window

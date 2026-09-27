## ADDED Requirements

### Requirement: Eligible player lookup
The system SHALL provide a player lookup data set for selecting players who have personal basket statistics.

#### Scenario: Eligible player is listed
- **WHEN** a player has at least one personally eligible basket variation result
- **THEN** the player lookup data includes that player

#### Scenario: Ineligible player is omitted
- **WHEN** a player has no personally eligible basket variation result
- **THEN** the player lookup data does not include that player

#### Scenario: Player display label includes PDGA number
- **WHEN** a listed player has a PDGA number
- **THEN** the lookup display label includes the player name and PDGA number

#### Scenario: Player display label without PDGA number
- **WHEN** a listed player has no PDGA number
- **THEN** the lookup display label includes the player name without a PDGA number suffix

### Requirement: Global variation regression eligibility
The system SHALL calculate personal statistics only from basket variations with a usable global regression line.

#### Scenario: Variation with enough data is eligible
- **WHEN** a basket variation has at least 50 mapped hole-score results with non-null round result ratings, non-zero rating variance, and a negative unweighted score-over-rating regression slope
- **THEN** the system treats the basket variation as globally eligible for personal statistics

#### Scenario: Sparse global variation is omitted
- **WHEN** a basket variation has fewer than 50 mapped hole-score results with non-null round result ratings
- **THEN** the system omits the basket variation from personal statistics

#### Scenario: Global variation without rating variance is omitted
- **WHEN** all mapped rated results for a basket variation have the same rating
- **THEN** the system omits the basket variation from personal statistics

#### Scenario: Global variation with unusable slope is omitted
- **WHEN** a basket variation's unweighted score-over-rating regression slope is zero or positive
- **THEN** the system omits the basket variation from personal statistics

### Requirement: Personal variation rating calculation
The system SHALL calculate a player's basket variation rating from the inverse of the global variation regression line.

#### Scenario: Personal variation row is eligible
- **WHEN** a selected player has at least two mapped rated scores on a globally eligible basket variation
- **THEN** the system includes a personal statistics row for that player and variation

#### Scenario: Personal variation row with one result is omitted
- **WHEN** a selected player has fewer than two mapped rated scores on a globally eligible basket variation
- **THEN** the system omits that player and variation row

#### Scenario: Per-score rating uses global regression inverse
- **WHEN** a personal score belongs to a globally eligible basket variation with regression `score = intercept + slope * rating`
- **THEN** the score's calculated rating is `(score - intercept) / slope`

#### Scenario: Personal average uses equal weighting
- **WHEN** a personal statistics row has multiple calculated score ratings
- **THEN** the row rating is the arithmetic average of those calculated ratings with each recorded score weighted equally

### Requirement: Personal variation list ordering and formatting
The system SHALL expose personal basket variation rows as one best-to-worst list.

#### Scenario: Rows are ordered by calculated rating
- **WHEN** personal basket variation rows are displayed for a selected player
- **THEN** rows are ordered by decimal calculated rating from highest to lowest

#### Scenario: Row rating is displayed as integer
- **WHEN** a personal basket variation row is displayed
- **THEN** the calculated rating is rounded to the nearest integer for display

#### Scenario: Decimal rating is preserved for ordering
- **WHEN** multiple personal basket variation rows are sorted
- **THEN** sorting uses the unrounded decimal calculated rating

#### Scenario: Personal count is displayed
- **WHEN** a personal basket variation row is displayed
- **THEN** it displays the number of recorded scores for that selected player and variation

#### Scenario: Scores are displayed chronologically
- **WHEN** a personal basket variation row is displayed
- **THEN** it displays that player's scores for the variation as a comma-separated list sorted by round date ascending and round id ascending when dates are equal

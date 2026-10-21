## MODIFIED Requirements

### Requirement: Leaderboard period options are ordered by recency
The Leaderboards period selector SHALL list standard-category years from the current year down to 2025 followed by `Overall`. For the Aces category, it SHALL list only ace-containing years in descending order, including years before 2025 when applicable, followed by `Overall`.

#### Scenario: Standard category years descend before Overall
- **WHEN** a non-Aces category is selected
- **THEN** each standard year from the current year through 2025 is listed from newest to oldest
- **AND** `Overall` is the final option

#### Scenario: Aces years descend before Overall
- **WHEN** the Aces category is selected
- **THEN** ace-containing years are listed from newest to oldest
- **AND** years before 2025 are included when they contain an ace
- **AND** `Overall` is the final option

#### Scenario: Empty ace years are omitted
- **WHEN** a year has no ace occurrence
- **THEN** that year is omitted from the Aces period selector

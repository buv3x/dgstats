# leaderboard-period-thresholds Specification

## Purpose

Define scope-specific leaderboard eligibility, period selection, and threshold messaging.

## Requirements

### Requirement: Scope-specific leaderboard eligibility thresholds
The leaderboard exporter SHALL apply 100 total C1x putts and 180 played holes as the eligibility thresholds for the Overall C1x and C2+ per round leaderboards, respectively, while applying 50 total C1x putts and 90 played holes for each yearly scope.

#### Scenario: Overall C1x uses the strict threshold
- **WHEN** a player has 100 or more C1x putts in Overall
- **THEN** the player is eligible for the Overall C1x leaderboard
- **AND** a player with fewer than 100 Overall C1x putts is omitted

#### Scenario: Overall C2+ uses the strict threshold
- **WHEN** a player has 180 or more played holes across complete Overall rounds
- **THEN** the player is eligible for the Overall C2+ per round leaderboard
- **AND** a player with fewer than 180 Overall played holes is omitted

#### Scenario: Yearly C1x uses the original threshold
- **WHEN** a player has 50 or more C1x putts in a selected year
- **THEN** the player is eligible for that year's C1x leaderboard
- **AND** a player with fewer than 50 C1x putts in that year is omitted

#### Scenario: Yearly C2+ uses the original threshold
- **WHEN** a player has 90 or more played holes across complete rounds in a selected year
- **THEN** the player is eligible for that year's C2+ per round leaderboard
- **AND** a player with fewer than 90 played holes in that year is omitted

#### Scenario: Eligibility is independent between scopes
- **WHEN** a player's totals meet the threshold in one scope but not another
- **THEN** the player is included only in the qualifying scope

### Requirement: Leaderboard period options are ordered by recency
The Leaderboards period selector SHALL list standard-category years from the current year down to 2025 followed by `Overall`. For the Aces category, it SHALL list only ace-containing years in descending order, including years before 2025 when applicable, followed by `Overall`.

#### Scenario: Current year is listed first
- **WHEN** the Leaderboards view is initialized
- **THEN** the first period option is the current calendar year

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

### Requirement: Current year is selected by default
The Leaderboards view SHALL select the current calendar year by default when it is first initialized.

#### Scenario: Current year is the initial selection
- **WHEN** the user first opens the Leaderboards view
- **THEN** the period selectbox has the current calendar year selected
- **AND** the displayed entries use that year's scope

#### Scenario: Empty current year remains selected
- **WHEN** the current year has no qualifying entries for the selected category
- **THEN** the current year remains selected
- **AND** the existing empty-state message is displayed

### Requirement: Threshold notes reflect the selected scope
The C1x and C2+ per round leaderboard notes SHALL state the threshold for the selected scope: 100 C1x putts and 180 holes for Overall, or 50 C1x putts and 90 holes for a yearly scope.

#### Scenario: Overall threshold note is displayed
- **WHEN** Overall and the C1x category are selected
- **THEN** the note states that at least 100 C1x putts are required
- **WHEN** Overall and the C2+ per round category are selected
- **THEN** the note states that at least 180 holes played are required

#### Scenario: Yearly threshold note is displayed
- **WHEN** a year and the C1x category are selected
- **THEN** the note states that at least 50 C1x putts are required
- **WHEN** a year and the C2+ per round category are selected
- **THEN** the note states that at least 90 holes played are required

#### Scenario: Other categories do not show threshold notes
- **WHEN** a category other than C1x or C2+ per round is selected
- **THEN** no threshold note is displayed

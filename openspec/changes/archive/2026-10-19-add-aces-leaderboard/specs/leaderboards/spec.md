## MODIFIED Requirements

### Requirement: Leaderboards navigation and categories
The static statistics page SHALL provide a top-level `Leaderboards` view between `Personal stats` and `Description`, with second-level categories for `C1x`, `C2+ per round`, `Most C2+ in a round`, `Best C1x round`, `Longest throw-in`, and `Aces`.

#### Scenario: Leaderboards tab is placed in navigation
- **WHEN** the page is loaded
- **THEN** the top-level navigation displays `Leaderboards` after `Personal stats` and before `Description`

#### Scenario: Leaderboard categories are available
- **WHEN** the Leaderboards view is active
- **THEN** the six requested categories are displayed as second-level tabs

### Requirement: Leaderboard year selection
The Leaderboards view SHALL provide category-appropriate year selectors. Non-Aces categories SHALL contain `Overall` and every calendar year from 2025 through the current calendar year. The Aces category SHALL contain `Overall` and only years with at least one ace, including years before 2025.

#### Scenario: Standard categories retain their year options
- **WHEN** a non-Aces category is selected
- **THEN** the year selector contains every calendar year from 2025 through the current calendar year
- **AND** the selector contains `Overall`

#### Scenario: Aces exposes only populated years
- **WHEN** the Aces category is selected
- **THEN** the year selector contains only years with at least one ace
- **AND** those years may be earlier than 2025
- **AND** the selector contains `Overall`

#### Scenario: Aces year with no entry is unavailable
- **WHEN** a calendar year contains no ace
- **THEN** that year is not present in the Aces year selector

#### Scenario: Category changes reconcile the selected year
- **WHEN** the selected year is unavailable in the newly selected category
- **THEN** the selector changes to the newest available year for that category
- **AND** if no yearly scope is available, `Overall` is selected

### Requirement: Leaderboard data export
The statistics export SHALL provide one aggregated leaderboard data asset and expose its relative path from the statistics manifest. Existing categories SHALL use the existing complete-round data rules. Aces SHALL be calculated independently from `hole_score` and mapped `basket_variation` data and SHALL NOT depend on `round_result_throws` or round completeness.

#### Scenario: Leaderboard asset is referenced
- **WHEN** the statistics export completes
- **THEN** the manifest contains a relative path for leaderboard data
- **AND** the referenced asset contains Overall and available year-scoped category data

#### Scenario: Ace data is independent of throw timelines
- **WHEN** a hole score has value `1` and a mapped basket variation has a distance
- **THEN** the hole is exported as an ace regardless of whether round-result throws were imported

#### Scenario: Incomplete rounds contribute aces
- **WHEN** an ace is recorded in a round that is incomplete according to putting-round processing
- **THEN** the ace is included in the corresponding yearly and Overall Aces scopes

### Requirement: Leaderboard category values
The leaderboard SHALL use the existing values and display rules for all existing categories. Longest throw-in and Aces values SHALL display meters with a trailing `m`. Aces SHALL display one row per ace occurrence and SHALL provide a tooltip listing competition, round, and hole.

#### Scenario: Longest throw-in value is displayed
- **WHEN** a player has a qualifying successful throw-in with a recorded distance in the selected scope
- **THEN** the Longest throw-in value displays the existing whole-meter value followed by `m`
- **AND** the value provides a tooltip listing competition, round, and hole for every matching occurrence

#### Scenario: Ace occurrence value is displayed
- **WHEN** a hole score is exactly `1` and its basket variation has a recorded distance
- **THEN** the Aces category displays that occurrence's distance in meters followed by `m`
- **AND** the value provides a tooltip listing competition, round, and hole

#### Scenario: Multiple aces by one player remain separate
- **WHEN** one player has multiple ace occurrences in the selected scope
- **THEN** the Aces category displays one row for each occurrence
- **AND** no `(N times)` suffix replaces those rows

### Requirement: Ranking and ties
Each existing category SHALL retain its current top-ten, tie-boundary, and one-entry-per-player behavior. The Aces category SHALL display all occurrences and SHALL rank solely by distance.

#### Scenario: Higher ace distance ranks first
- **WHEN** two ace occurrences have different distances
- **THEN** the occurrence with the greater distance receives the better place

#### Scenario: Equal ace distances share a place
- **WHEN** multiple ace occurrences have equal distances
- **THEN** they receive the same competition place
- **AND** no occurrence count, player name, competition, round, or hole is used as a placement tie-breaker

#### Scenario: All ace occurrences are retained
- **WHEN** more than ten ace occurrences exist in the selected scope
- **THEN** every occurrence is displayed

#### Scenario: One player may have multiple ace rows
- **WHEN** a player has multiple ace occurrences in the selected scope
- **THEN** the player appears once for each occurrence

### Requirement: Leaderboard table presentation
The selected category SHALL display rows with place, name, and value columns and no table header.

#### Scenario: Leaderboard rows are rendered
- **WHEN** a category and year are selected and qualifying entries exist
- **THEN** each row displays place, player name, and value in that order
- **AND** no table header is rendered

#### Scenario: Category and year changes rerender results
- **WHEN** the user changes the category or year
- **THEN** the displayed rows update to the corresponding exported scope and category

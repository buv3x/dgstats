## ADDED Requirements

### Requirement: Leaderboards navigation and categories
The static statistics page SHALL provide a top-level `Leaderboards` view between `Personal stats` and `Description`, with second-level categories for `C1x`, `C2+ per round`, `Most C2+ in a round`, `Best C1x round`, and `Longest throw-in`.

#### Scenario: Leaderboards tab is placed in navigation
- **WHEN** the page is loaded
- **THEN** the top-level navigation displays `Leaderboards` after `Personal stats` and before `Description`

#### Scenario: Leaderboard categories are available
- **WHEN** the Leaderboards view is active
- **THEN** the five requested categories are displayed as second-level tabs

### Requirement: Leaderboard year selection
The Leaderboards view SHALL provide a year selector containing `Overall` and every calendar year from 2025 through the current calendar year, with `Overall` selected by default.

#### Scenario: Overall is selected initially
- **WHEN** the user opens the Leaderboards view
- **THEN** the year selector has `Overall` selected
- **AND** the displayed entries use all qualifying data

#### Scenario: Current-year options are generated
- **WHEN** the current calendar year is 2026
- **THEN** the year selector contains `Overall`, `2025`, and `2026`

#### Scenario: Empty year remains selectable
- **WHEN** a year has no qualifying leaderboard entries
- **THEN** that year remains present in the selector
- **AND** the selected category displays the existing empty-state message

### Requirement: Leaderboard data export
The statistics export SHALL provide one aggregated leaderboard data asset and expose its relative path from the statistics manifest.

#### Scenario: Leaderboard asset is referenced
- **WHEN** the statistics export completes
- **THEN** the manifest contains a relative path for leaderboard data
- **AND** the referenced asset contains Overall and available year-scoped category data

#### Scenario: Complete rounds are used
- **WHEN** leaderboard values are calculated
- **THEN** only the same complete player rounds used by existing overall and yearly putting summaries contribute to the rankings

### Requirement: Leaderboard category values
The leaderboard SHALL use the following values and display rules.

#### Scenario: C1x value is displayed
- **WHEN** a player has qualifying C1x attempts in the selected scope
- **THEN** the C1x value displays the player’s success percentage using the existing percentage and successful/total format
- **AND** the value has no tooltip

#### Scenario: C2+ per round value is displayed
- **WHEN** a player has at least one qualifying complete round in the selected scope
- **THEN** the C2+ per round value displays the existing average weighted C2+ value
- **AND** the value has no occurrence-count suffix
- **AND** the value has no tooltip

#### Scenario: Most C2+ value is displayed
- **WHEN** a player has a qualifying complete round in the selected scope
- **THEN** the Most C2+ value uses the existing rounded maximum display and occurrence suffix
- **AND** the value provides a tooltip listing matching rounds

#### Scenario: Best C1x round value is displayed
- **WHEN** a player has a qualifying C1x attempt in a complete round in the selected scope
- **THEN** the Best C1x round value uses the existing percentage, successful/total, and occurrence suffix display
- **AND** the value provides a tooltip listing matching rounds

#### Scenario: Longest throw-in value is displayed
- **WHEN** a player has a qualifying successful throw-in with a recorded distance in the selected scope
- **THEN** the Longest throw-in value displays the existing whole-meter value and exact-distance occurrence suffix
- **AND** the value provides a tooltip listing competition, round, and hole for every matching occurrence

### Requirement: Ranking and ties
Each category SHALL show at most the top ten ranks, except that all players tied at the tenth rank SHALL be included. A player SHALL appear at most once per category and selected scope.

#### Scenario: Higher result ranks first
- **WHEN** two players have different comparable values in a category
- **THEN** the player with the higher result receives the better rank

#### Scenario: Category occurrence count breaks a tie
- **WHEN** two players have equal primary values in Most C2+ in a round, Best C1x round, or Longest throw-in
- **THEN** the player with the larger category occurrence count receives the better rank

#### Scenario: C2+ per round has no occurrence tie-breaker
- **WHEN** two players have equal C2+ per round values
- **THEN** neither player receives an occurrence-count advantage
- **AND** tied players receive the same place

#### Scenario: Tied players receive competition places
- **WHEN** multiple players have equal primary and applicable secondary ranking values
- **THEN** their place is displayed as `Tn`
- **AND** the next non-tied place skips the occupied tied places

#### Scenario: Ties at the boundary are retained
- **WHEN** more than ten players share the tenth rank
- **THEN** every player sharing that rank is displayed

#### Scenario: Tied names are alphabetical
- **WHEN** players share a rank
- **THEN** their rows are sorted alphabetically by player name

### Requirement: Round-length explanation
The Most C2+ leaderboard SHALL identify results whose placement is affected by expected round length.

#### Scenario: Round length affects placement
- **WHEN** expected-hole weighting changes a Most C2+ player’s placement relative to the raw C2+ count
- **THEN** the displayed value includes an asterisk
- **AND** its tooltip includes `Round with N holes` for the relevant round, where `N` is the actual expected hole count

#### Scenario: Round length does not affect placement
- **WHEN** expected-hole weighting does not change the Most C2+ placement
- **THEN** the displayed value has no asterisk

### Requirement: Leaderboard table presentation
The selected category SHALL display rows with place, name, and value columns and no table header.

#### Scenario: Leaderboard rows are rendered
- **WHEN** a category and year are selected and qualifying entries exist
- **THEN** each row displays place, player name, and value in that order
- **AND** no table header is rendered

#### Scenario: Category and year changes rerender results
- **WHEN** the user changes the category or year
- **THEN** the displayed rows update to the corresponding exported scope and category

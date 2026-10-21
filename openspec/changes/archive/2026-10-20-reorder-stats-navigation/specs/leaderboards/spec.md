## MODIFIED Requirements

### Requirement: Leaderboards navigation and categories
The static statistics page SHALL provide a top-level `Leaderboards` view first in the navigation and make it the initial default view, with second-level categories for `C1x`, `C2+ per round`, `Most C2+ in a round`, `Best C1x round`, `Longest throw-in`, and `Aces`.

#### Scenario: Leaderboards tab is placed in navigation
- **WHEN** the page is loaded
- **THEN** the top-level navigation displays `Leaderboards` first and `Personal stats` second

#### Scenario: Leaderboards tab is selected initially
- **WHEN** the page first loads successfully
- **THEN** the `Leaderboards` tab is active
- **AND** the Leaderboards view is visible

#### Scenario: Leaderboard categories are available
- **WHEN** the Leaderboards view is active
- **THEN** the six requested categories are displayed as second-level tabs

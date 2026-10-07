## MODIFIED Requirements

### Requirement: Personal stats autocomplete
The static page SHALL provide autocomplete selection for eligible players and SHALL apply a committed player selection immediately after the browser has finalized the input value.

#### Scenario: Autocomplete uses eligible player labels
- **WHEN** player lookup data loads successfully
- **THEN** the Personal stats player input offers autocomplete options using exported player display labels

#### Scenario: Selecting a player applies exact identity
- **WHEN** the user selects an autocomplete option
- **THEN** the page uses that option's player id and personal statistics path rather than matching by free-form text alone

#### Scenario: Selecting a player updates immediately in Chromium
- **WHEN** the user selects an autocomplete option in Chrome or Edge
- **THEN** the page processes the committed option value without requiring another click, focus, or blur interaction

#### Scenario: Related input events do not duplicate loading
- **WHEN** selecting an option produces closely related `input` and `change` events
- **THEN** the page performs one effective player-selection update and does not issue duplicate personal-statistics requests for that selection

#### Scenario: Clearing player selection resets personal results
- **WHEN** the user clears the selected player input
- **THEN** the page hides the personal statistics list and shows an unselected state

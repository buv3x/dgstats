## ADDED Requirements

### Requirement: Personal stats data loading
The static page SHALL load personal statistics data using manifest-provided paths.

#### Scenario: Player lookup data is loaded
- **WHEN** the statistics manifest includes a player lookup path
- **THEN** the page fetches the player lookup data from that relative path

#### Scenario: Missing player lookup data is reported
- **WHEN** the page cannot load the player lookup data
- **THEN** it displays a clear personal-statistics missing-data message in the Personal stats view

#### Scenario: Empty player lookup is reported
- **WHEN** the player lookup data contains no eligible players
- **THEN** the Personal stats view displays a no-eligible-players message

#### Scenario: Selected player data is loaded
- **WHEN** the user selects a player from the Personal stats autocomplete
- **THEN** the page fetches that player's personal statistics file using the selected player's exported path

#### Scenario: Missing selected player data is reported
- **WHEN** the page cannot load the selected player's personal statistics file
- **THEN** it displays a clear selected-player missing-data message

### Requirement: Personal stats autocomplete
The static page SHALL provide autocomplete selection for eligible players.

#### Scenario: Autocomplete uses eligible player labels
- **WHEN** player lookup data loads successfully
- **THEN** the Personal stats player input offers autocomplete options using exported player display labels

#### Scenario: Selecting a player applies exact identity
- **WHEN** the user selects an autocomplete option
- **THEN** the page uses that option's player id and personal statistics path rather than matching by free-form text alone

#### Scenario: Clearing player selection resets personal results
- **WHEN** the user clears the selected player input
- **THEN** the page hides the personal statistics list and shows an unselected state

### Requirement: Personal stats variation list
The static page SHALL display one ordered list of personal basket variation ratings for the selected player.

#### Scenario: Personal list displays rows
- **WHEN** a selected player's personal statistics file contains variation rows
- **THEN** the Personal stats view displays one table or list containing those rows

#### Scenario: Personal row fields are displayed
- **WHEN** a personal variation row is displayed
- **THEN** the row shows basket course, basket label, variation label, rounded rating, personal result count, and comma-separated scores

#### Scenario: Personal rows preserve exported order
- **WHEN** personal variation rows are rendered
- **THEN** the page displays them in the order provided by the selected player's personal statistics file

#### Scenario: Empty selected player results are reported
- **WHEN** the selected player's personal statistics file contains no variation rows
- **THEN** the Personal stats view displays a no-personal-results message instead of an empty list

## MODIFIED Requirements

### Requirement: Basket statistics view navigation
The static page SHALL provide top-level navigation between the Course stats view, Basket stats view, and Personal stats view.

#### Scenario: Course stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Course stats`

#### Scenario: Basket stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Basket stats`

#### Scenario: Personal stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Personal stats`

#### Scenario: Course stats preserves current display
- **WHEN** the user selects `Course stats`
- **THEN** the page displays the existing course-level statistics controls, SPR/VAR scatter chart, and table

#### Scenario: Basket stats switches to basket controls
- **WHEN** the user selects `Basket stats`
- **THEN** the page displays Basket stats course and basket variation controls instead of the Course stats rating controls and table

#### Scenario: Personal stats switches to player controls
- **WHEN** the user selects `Personal stats`
- **THEN** the page displays Personal stats player autocomplete controls and personal variation results instead of the Course stats and Basket stats controls

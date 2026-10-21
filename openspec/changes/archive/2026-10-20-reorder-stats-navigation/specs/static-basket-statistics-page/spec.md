## MODIFIED Requirements

### Requirement: Description tab navigation and default view
The static page SHALL display the Leaderboards, Personal stats, Course stats, Hole stats, and Description navigation items in that order, and SHALL make Leaderboards the default active view.

#### Scenario: Description tab is last
- **WHEN** the static basket statistics page is displayed
- **THEN** the navigation order is Leaderboards, Personal stats, Course stats, Hole stats, Description

#### Scenario: Leaderboards is selected initially
- **WHEN** the static basket statistics page first loads successfully
- **THEN** the Leaderboards tab is active and its content is visible
- **AND** the Personal stats, Course stats, Hole stats, and Description content is hidden

#### Scenario: Course stats is selected initially
- **WHEN** the static basket statistics page first loads successfully
- **THEN** the Leaderboards tab is active and its content is visible
- **AND** the Personal stats, Course stats, Hole stats, and Description content is hidden

#### Scenario: Existing views remain selectable
- **WHEN** the user selects Description, Course stats, Hole stats, or Personal stats
- **THEN** the selected view becomes visible and Leaderboards becomes inactive

### Requirement: Personal stats putting navigation
The Personal stats view SHALL provide shared player selection and second-level Putting stats and Hole stats views in that order, and SHALL default to Putting stats when a player is selected.

#### Scenario: No player is selected
- **WHEN** the Personal stats view is opened without a selected player
- **THEN** it hides the second-level tabs and statistics content

#### Scenario: Player is selected
- **WHEN** a player is selected
- **THEN** the view displays second-level tabs named `Putting stats` and `Hole stats`
- **AND** it defaults to Putting stats

#### Scenario: Hole stats view preserves existing controls
- **WHEN** the Hole stats tab is active
- **THEN** it displays the existing hole-statistics table, course filter, and minimum-count filter

#### Scenario: Putting stats view has no filters
- **WHEN** the Putting stats tab is active
- **THEN** it displays the putting summaries for the selected player
- **AND** it does not display course, minimum-count, or other statistics filters

## ADDED Requirements

### Requirement: Lost three-plus-putting label
The Personal stats putting view SHALL display an available `threePlusPuttPerRound` value in overall and yearly summaries using the label `Lost 3+putting per round` and exactly two decimal places.

#### Scenario: Overall metric uses the new label
- **WHEN** an overall putting summary contains `threePlusPuttPerRound`
- **THEN** the summary displays `Lost 3+putting per round`
- **AND** the value has two digits after the decimal point

#### Scenario: Yearly metric uses the new label
- **WHEN** an expanded yearly putting summary contains `threePlusPuttPerRound`
- **THEN** the summary displays `Lost 3+putting per round`
- **AND** the value has two digits after the decimal point

#### Scenario: Export field remains compatible
- **WHEN** the putting summary is loaded from an existing personal-statistics export
- **THEN** the value is read from `threePlusPuttPerRound` without requiring a field rename

## ADDED Requirements

### Requirement: Personal stats putting navigation
The Personal stats view SHALL provide shared player selection and second-level Hole stats and Putting stats views.

#### Scenario: No player is selected
- **WHEN** the Personal stats view is opened without a selected player
- **THEN** it hides the second-level tabs and statistics content

#### Scenario: Player is selected
- **WHEN** a player is selected
- **THEN** the view displays second-level tabs named `Hole stats` and `Putting stats`
- **AND** it defaults to Hole stats

#### Scenario: Hole stats view preserves existing controls
- **WHEN** the Hole stats tab is active
- **THEN** it displays the existing hole-statistics table, course filter, and minimum-count filter

#### Scenario: Putting stats view has no filters
- **WHEN** the Putting stats tab is active
- **THEN** it displays only the C1x and C2 summaries for the selected player
- **AND** it does not display course, minimum-count, or other statistics filters

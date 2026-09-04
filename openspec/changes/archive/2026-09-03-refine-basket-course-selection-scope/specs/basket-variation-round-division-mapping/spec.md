## MODIFIED Requirements

### Requirement: Basket course selection for mapping options
The system SHALL allow the local user to choose whether basket course selection applies at competition, round, or group level for the current mapping editor form.

#### Scenario: Competition-scoped basket course is the default
- **WHEN** the local user views the mapping editor for a selected competition
- **THEN** the system displays a basket course selector above the round mapping tables
- **AND** course selection by round is unchecked

#### Scenario: Round-level course controls are hidden by default
- **WHEN** course selection by round is unchecked
- **THEN** the system does not display round-level basket course selectors
- **AND** the system does not display round-level same-layout controls
- **AND** the system does not display round-level course selection by group controls

#### Scenario: Competition-scoped basket course controls all cell options
- **WHEN** course selection by round is unchecked and the local user selects a basket course
- **THEN** each mapping cell offers only basket variations belonging to baskets in the selected basket course

#### Scenario: Round-scoped basket course controls round cell options
- **WHEN** course selection by round is checked and the local user selects a basket course for a round
- **THEN** each mapping cell in that round offers only basket variations belonging to baskets in the selected basket course

#### Scenario: Group-scoped basket course controls column cell options
- **WHEN** course selection by group is checked for a round and the local user selects a basket course for a group column
- **THEN** each mapping cell in that group column offers only basket variations belonging to baskets in the selected basket course

#### Scenario: Baskets with multiple variations are distinguishable
- **WHEN** a basket has more than one variation
- **THEN** the system displays enough basket and variation information for the local user to distinguish the selectable options

### Requirement: Single save for all mappings
The system SHALL save all submitted mappings for the selected competition from a single form submission without persisting editor course-scope or same-layout control state.

#### Scenario: All mappings are saved atomically
- **WHEN** the local user submits the mapping editor
- **THEN** the system saves all valid changed mappings in one transaction

#### Scenario: Empty cell clears existing mapping
- **WHEN** the local user submits an empty value for a cell that previously had a mapping
- **THEN** the system removes the mapping for that round division and hole ordinal

#### Scenario: Submitted variations are validated against selected basket course
- **WHEN** the local user submits a basket variation id that does not belong to the effective basket course selected for that cell's competition, round, or group scope
- **THEN** the system rejects the save and does not persist any submitted mapping changes

#### Scenario: Submitted slots are validated against selected competition
- **WHEN** the local user submits a round division id that does not belong to the selected competition
- **THEN** the system rejects the save and does not persist any submitted mapping changes

#### Scenario: Editor scope state is not persisted
- **WHEN** the local user submits mappings with competition, round, or group course-scope controls
- **THEN** the system does not persist the selected course-scope or same-layout control state outside the submitted mapping values

### Requirement: Same-layout round editing
The system SHALL allow same-layout editing at the same scope as course selection except when course selection is group-scoped.

#### Scenario: Competition-scoped same-layout is available by default
- **WHEN** course selection by round is unchecked
- **THEN** the system displays a same-layout control above the round mapping tables
- **AND** the system does not display round-level same-layout controls

#### Scenario: Competition-scoped same-layout applies to each round
- **WHEN** competition-scoped same-layout is enabled
- **THEN** only the first displayed division's mapping cells are editable for each round

#### Scenario: Round-scoped same-layout is available with round-scoped course selection
- **WHEN** course selection by round is checked and course selection by group is unchecked for a round
- **THEN** the system displays an enabled same-layout control for that round

#### Scenario: Round-scoped same-layout applies only to its round
- **WHEN** same-layout is enabled for one round and disabled for another round
- **THEN** the system expands first-division values only for the round whose same-layout setting is enabled

#### Scenario: Group-scoped course selection disables same-layout
- **WHEN** course selection by group is checked for a round
- **THEN** the system unchecks and disables same-layout for that round

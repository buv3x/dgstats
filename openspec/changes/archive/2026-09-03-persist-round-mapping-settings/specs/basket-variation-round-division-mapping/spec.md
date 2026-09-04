## MODIFIED Requirements

### Requirement: Basket course selection for mapping options
The system SHALL allow the local user to select the manual basket course per round whose baskets and variations populate that round's mapping options.

#### Scenario: Basket courses are available within round tables
- **WHEN** the local user views the mapping editor for a selected competition
- **THEN** the system displays a basket course selector for each round table

#### Scenario: Basket course selection controls round cell options
- **WHEN** the local user selects a basket course for a round
- **THEN** each mapping cell in that round offers only basket variations belonging to baskets in the selected basket course

#### Scenario: Baskets with multiple variations are distinguishable
- **WHEN** a basket has more than one variation
- **THEN** the system displays enough basket and variation information for the local user to distinguish the selectable options

### Requirement: Single save for all mappings
The system SHALL save all submitted mappings and round mapping settings for the selected competition from a single form submission.

#### Scenario: All mappings are saved atomically
- **WHEN** the local user submits the mapping editor
- **THEN** the system saves all valid changed mappings in one transaction

#### Scenario: Empty cell clears existing mapping
- **WHEN** the local user submits an empty value for a cell that previously had a mapping
- **THEN** the system removes the mapping for that round division and hole ordinal

#### Scenario: Submitted variations are validated against selected basket course
- **WHEN** the local user submits a basket variation id that does not belong to the basket course selected for that cell's round
- **THEN** the system rejects the save and does not persist any submitted mapping changes

#### Scenario: Submitted slots are validated against selected competition
- **WHEN** the local user submits a round division id that does not belong to the selected competition
- **THEN** the system rejects the save and does not persist any submitted mapping changes

### Requirement: Same-layout round editing
The system SHALL allow the local user to enable same-layout editing independently for each displayed round.

#### Scenario: Same-layout controls are available within round tables
- **WHEN** the local user views the mapping editor for a selected competition
- **THEN** the system displays a same-layout control for each round table

#### Scenario: Same-layout source cells are editable per round
- **WHEN** same-layout is enabled for a round
- **THEN** only the first displayed division's mapping cells are editable for that round

#### Scenario: Same-layout expansion is limited to enabled rounds
- **WHEN** the local user submits mappings with same-layout enabled for one round and disabled for another round
- **THEN** the system expands first-division values only for the round whose same-layout setting is enabled

## ADDED Requirements

### Requirement: Same-layout division mapping mode
The system SHALL provide a same-layout editor mode for copying one division's basket variation mappings to the remaining divisions in the same round.

#### Scenario: Same-layout control is shown near competition controls
- **WHEN** the local user views the basket variation mapping administration page
- **THEN** the system displays a `Same layout for all divisions` checkbox near the competition and basket course controls

#### Scenario: First division remains editable in same-layout mode
- **WHEN** same-layout mode is checked and a round mapping table has division columns
- **THEN** only the first displayed division column for that round remains editable

#### Scenario: Remaining divisions are display-only in same-layout mode
- **WHEN** same-layout mode is checked and a round mapping table has more than one division column
- **THEN** the division columns after the first displayed division show copied mapping values as display-only controls

#### Scenario: Display-only values mirror source division edits
- **WHEN** same-layout mode is checked and the local user changes a first-division mapping value
- **THEN** matching basket ordinals in the other displayed divisions for that same round reflect the first-division value before save

#### Scenario: Same-layout mode applies independently per round
- **WHEN** same-layout mode is checked and the editor displays multiple round tables
- **THEN** each round table uses its own first displayed division as the source for that round

## MODIFIED Requirements

### Requirement: Competition selection for mapping administration
The system SHALL provide a local administration page for choosing which competition's basket variation mappings to edit and SHALL indicate which listed competitions already have persisted basket variation mappings.

#### Scenario: Competitions are listed by newest start date
- **WHEN** the local user opens the basket variation mapping administration page
- **THEN** the system displays competitions sorted by `start_date` descending

#### Scenario: Competition label includes date
- **WHEN** the local user views the competition selector
- **THEN** each competition option displays the competition name and the competition start date in brackets

#### Scenario: Mapped competition label is marked
- **WHEN** a listed competition has at least one persisted basket variation mapping
- **THEN** the competition option label is prefixed with `* `

#### Scenario: Unmapped competition label is unmarked
- **WHEN** a listed competition has no persisted basket variation mappings
- **THEN** the competition option label is displayed without the `* ` prefix

#### Scenario: Selected competition loads mapping editor
- **WHEN** the local user selects a competition
- **THEN** the system displays the mapping editor for rounds and divisions imported for that competition

### Requirement: Single save for all mappings
The system SHALL save all submitted mappings for the selected competition from a single form submission.

#### Scenario: All mappings are saved atomically
- **WHEN** the local user submits the mapping editor
- **THEN** the system saves all valid changed mappings in one transaction

#### Scenario: Empty cell clears existing mapping
- **WHEN** the local user submits an empty value for a cell that previously had a mapping
- **THEN** the system removes the mapping for that round division and hole ordinal

#### Scenario: Same-layout save copies first division to remaining divisions
- **WHEN** the local user submits the mapping editor with same-layout mode checked
- **THEN** the system persists the first displayed division's submitted mapping values to corresponding basket ordinals in the remaining divisions for each round

#### Scenario: Submitted variations are validated against selected basket course
- **WHEN** the local user submits a basket variation id that does not belong to the selected basket course
- **THEN** the system rejects the save and does not persist any submitted mapping changes

#### Scenario: Submitted slots are validated against selected competition
- **WHEN** the local user submits a round division id that does not belong to the selected competition
- **THEN** the system rejects the save and does not persist any submitted mapping changes

# basket-variation-round-division-mapping Specification

## Purpose
TBD - created by archiving change map-basket-variations-to-round-divisions. Update Purpose after archive.
## Requirements
### Requirement: Round-division basket variation mapping persistence
The system SHALL store basket variation assignments at the round-division hole level instead of the player hole-score level.

#### Scenario: Mapping is stored once per round division hole
- **WHEN** a basket variation is assigned for a round division and hole ordinal
- **THEN** the system stores the assignment in `basket_variation_round_division` using `round_division_id`, `hole_ordinal`, and `basket_variation_id`

#### Scenario: Duplicate mapping for same slot is prevented
- **WHEN** the system saves a mapping for a `round_division_id` and `hole_ordinal` that already has a mapping
- **THEN** the existing mapping is updated rather than creating a duplicate row

#### Scenario: Hole score does not store basket variation
- **WHEN** hole score data is persisted or loaded
- **THEN** the system does not read or write a `basket_variation_id` on `hole_score`

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

### Requirement: Round mapping tables
The system SHALL display one editable mapping table per round for the selected competition.

#### Scenario: Round tables are displayed
- **WHEN** the mapping editor is opened for a selected competition
- **THEN** the system displays a separate table for each imported round in that competition

#### Scenario: Divisions are displayed as columns
- **WHEN** a round table is rendered
- **THEN** the system displays that round's imported round divisions as table columns

#### Scenario: Hole ordinals are displayed as basket numbers
- **WHEN** a round table is rendered
- **THEN** the system displays hole ordinals as row labels using the label "Basket"

#### Scenario: Hole row count is derived from imported results
- **WHEN** the system renders a division column
- **THEN** the available hole ordinals are based on the first imported player round result for that round division

#### Scenario: Existing mappings are preselected
- **WHEN** a mapping already exists for a round division and hole ordinal
- **THEN** the corresponding cell select preselects the mapped basket variation

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

### Requirement: Existing import and basket maintenance behavior is preserved
The system SHALL preserve existing PDGA import behavior and existing basket course maintenance behavior while adding mapping administration.

#### Scenario: PDGA import remains manual-mapping agnostic
- **WHEN** PDGA competition data is imported
- **THEN** the system does not create, update, or delete `basket_variation_round_division` records

#### Scenario: Basket course administration remains available
- **WHEN** the local user opens the existing basket course administration pages
- **THEN** the system continues to allow maintaining basket courses, baskets, and basket variations

### Requirement: Basket variation mapping administration navigation
The system SHALL display a top administration navigation menu on the basket variation mapping administration page.

#### Scenario: Mapping page has top navigation
- **WHEN** the local user opens the basket variation mapping administration page
- **THEN** the page displays navigation links to `/basket-courses`, `/basket-variation-mappings`, and `/basket-statistics-export`

#### Scenario: Mapping page behavior is preserved
- **WHEN** the local user uses the basket variation mapping administration page after navigation is added
- **THEN** existing competition selection, basket course selection, mapping copy controls, mapping save behavior, and validation feedback continue to work

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

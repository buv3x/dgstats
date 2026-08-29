## ADDED Requirements

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
The system SHALL provide a local administration page for choosing which competition's basket variation mappings to edit.

#### Scenario: Competitions are listed by newest start date
- **WHEN** the local user opens the basket variation mapping administration page
- **THEN** the system displays competitions sorted by `start_date` descending

#### Scenario: Competition label includes date
- **WHEN** the local user views the competition selector
- **THEN** each competition option displays the competition name and the competition start date in brackets

#### Scenario: Selected competition loads mapping editor
- **WHEN** the local user selects a competition
- **THEN** the system displays the mapping editor for rounds and divisions imported for that competition

### Requirement: Basket course selection for mapping options
The system SHALL allow the local user to select the manual basket course whose baskets and variations populate the mapping options.

#### Scenario: Basket courses are available above round tables
- **WHEN** the local user views the mapping editor for a selected competition
- **THEN** the system displays a basket course selector above the round mapping tables

#### Scenario: Basket course selection controls cell options
- **WHEN** the local user selects a basket course
- **THEN** each mapping cell offers only basket variations belonging to baskets in the selected basket course

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
The system SHALL save all submitted mappings for the selected competition from a single form submission.

#### Scenario: All mappings are saved atomically
- **WHEN** the local user submits the mapping editor
- **THEN** the system saves all valid changed mappings in one transaction

#### Scenario: Empty cell clears existing mapping
- **WHEN** the local user submits an empty value for a cell that previously had a mapping
- **THEN** the system removes the mapping for that round division and hole ordinal

#### Scenario: Submitted variations are validated against selected basket course
- **WHEN** the local user submits a basket variation id that does not belong to the selected basket course
- **THEN** the system rejects the save and does not persist any submitted mapping changes

#### Scenario: Submitted slots are validated against selected competition
- **WHEN** the local user submits a round division id that does not belong to the selected competition
- **THEN** the system rejects the save and does not persist any submitted mapping changes

### Requirement: Existing import and basket maintenance behavior is preserved
The system SHALL preserve existing PDGA import behavior and existing basket course maintenance behavior while adding mapping administration.

#### Scenario: PDGA import remains manual-mapping agnostic
- **WHEN** PDGA competition data is imported
- **THEN** the system does not create, update, or delete `basket_variation_round_division` records

#### Scenario: Basket course administration remains available
- **WHEN** the local user opens the existing basket course administration pages
- **THEN** the system continues to allow maintaining basket courses, baskets, and basket variations

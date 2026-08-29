## Requirements

### Requirement: Pending competition import selection
The system SHALL import competitions from `datas.competition_import` records where `imported` is false.

#### Scenario: Pending rows are selected
- **WHEN** the local PDGA import is triggered
- **THEN** the importer processes competition ids from `datas.competition_import` whose `imported` value is false

#### Scenario: Imported rows are skipped
- **WHEN** the local PDGA import is triggered
- **THEN** the importer does not process competition ids from `datas.competition_import` whose `imported` value is true

### Requirement: Competition metadata persistence
The system SHALL fetch PDGA competition information for each pending competition id and persist normalized competition metadata, divisions, rounds, courses, layouts, and layout holes.

#### Scenario: Event info is stored
- **WHEN** the PDGA event info endpoint returns data for a pending competition
- **THEN** the system stores or updates the local competition record using the PDGA tournament id and event metadata

#### Scenario: Divisions are stored
- **WHEN** the PDGA event info response contains divisions
- **THEN** the system stores or updates each division linked to the local competition

#### Scenario: Rounds are stored
- **WHEN** the PDGA event info response contains a rounds list
- **THEN** the system stores or updates each round linked to the local competition with its round number and date

#### Scenario: Layout details are stored
- **WHEN** the PDGA event info response contains layouts and hole details
- **THEN** the system stores or updates courses, layouts, and layout hole records needed to describe the played holes

### Requirement: Round division result import
The system SHALL fetch and persist PDGA round results for each round/division combination described by the competition info response.

#### Scenario: Result endpoint is called for each combination
- **WHEN** a competition has imported rounds and divisions
- **THEN** the importer calls the PDGA round results endpoint for each division code and round number combination

#### Scenario: Round division batch is stored
- **WHEN** a PDGA round results response is received
- **THEN** the system stores or updates a `round_division` record linked to the local round and division

#### Scenario: Player results are stored
- **WHEN** a PDGA round results response contains player scores
- **THEN** the system stores or updates players and round result records with score totals, places, ratings, layout reference, and completion fields

#### Scenario: Hole scores are stored
- **WHEN** a player score contains hole scores
- **THEN** the system stores or updates one hole score record per played hole linked to the player's round result

### Requirement: Import completion marking
The system SHALL mark a `competition_import` record as imported only after the competition metadata and all round/division result data for that competition have been processed successfully.

#### Scenario: Successful competition import is marked complete
- **WHEN** all PDGA event info and round result data for a competition have been persisted successfully
- **THEN** the corresponding `datas.competition_import.imported` value is set to true

#### Scenario: Failed competition import remains pending
- **WHEN** any required PDGA call or persistence step fails for a competition
- **THEN** the failure is logged and the corresponding `datas.competition_import.imported` value remains false

### Requirement: Local manual execution
The system SHALL provide a simple local trigger to run the PDGA import without adding production operational features.

#### Scenario: Import is manually triggered
- **WHEN** a local user invokes the import trigger
- **THEN** the system starts the pending PDGA import process

#### Scenario: Advanced operational features are absent
- **WHEN** the PDGA import implementation is reviewed
- **THEN** it does not require authentication, session management, scheduling, parallelization, or production deployment infrastructure

### Requirement: Liquibase schema support
The system SHALL define all missing import storage tables and table changes using the existing Liquibase formatted SQL changelog style.

#### Scenario: Missing tables are added
- **WHEN** the application database is migrated
- **THEN** Liquibase creates the tables and columns needed for divisions, layouts, layout holes, players, round divisions, round results, and hole scores

#### Scenario: Round group is renamed
- **WHEN** the schema migration for this change is applied
- **THEN** the previous `datas.round_group` table concept is represented as `datas.round_division`

### Requirement: Idempotent local reruns
The system SHALL avoid duplicate normalized records when the same pending competition is processed more than once.

#### Scenario: Competition is rerun before completion
- **WHEN** the importer processes a competition that already has partially persisted normalized records
- **THEN** the importer reuses or updates existing records based on stable local or PDGA identifiers instead of creating duplicates

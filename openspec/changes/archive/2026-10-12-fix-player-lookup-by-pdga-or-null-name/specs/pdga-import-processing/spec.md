## MODIFIED Requirements

### Requirement: Player results are stored
The system SHALL store or update players and round result records when a PDGA round results response contains player scores. Player identity lookup MUST use the incoming PDGA number when present. If the incoming PDGA number is absent, or if a present number has no database match, the importer MUST look up an existing player by exact name with a null PDGA number. The importer MUST NOT use city, country, nationality, profile URL, or other metadata for player identity lookup.

#### Scenario: Existing PDGA player is selected
- **WHEN** a player score contains a PDGA number matching a database player
- **THEN** the importer uses that database player regardless of the incoming name, city, or country

#### Scenario: Newly numbered player upgrades a null-PDGA record
- **WHEN** a player score contains a PDGA number with no database match and an existing player has the same exact name and a null PDGA number
- **THEN** the importer uses that existing player and stores the incoming PDGA number on it

#### Scenario: Unmatched numbered player is created
- **WHEN** a player score contains a PDGA number with no database match and no same-name player with a null PDGA number exists
- **THEN** the importer creates a new player with the incoming PDGA number

#### Scenario: Existing unnumbered player is selected by name
- **WHEN** a player score has no PDGA number and an existing player has the same exact name and a null PDGA number
- **THEN** the importer uses that existing player regardless of the incoming city or country

#### Scenario: Unmatched unnumbered player is created
- **WHEN** a player score has no PDGA number and no same-name player with a null PDGA number exists
- **THEN** the importer creates a new player with a null PDGA number

#### Scenario: Assigned PDGA identity is not overridden by name
- **WHEN** a player score has a PDGA number that does not match an existing player and a same-name player has a different non-null PDGA number
- **THEN** the importer does not use the same-name player and creates or selects only according to the numbered-player rules

#### Scenario: Player result data remains persisted
- **WHEN** a player score is imported
- **THEN** the system stores or updates the selected player and round result with score totals, places, ratings, layout reference, and completion fields

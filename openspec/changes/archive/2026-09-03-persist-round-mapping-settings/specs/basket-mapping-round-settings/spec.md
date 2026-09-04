## ADDED Requirements

### Requirement: Round mapping settings persistence
The system SHALL persist local basket variation mapping settings independently for each imported round.

#### Scenario: Round settings are stored separately from imported round data
- **WHEN** a round's mapping settings are saved
- **THEN** the system stores the selected basket course and same-layout value in a local mapping settings record associated with that round

#### Scenario: One settings record per round
- **WHEN** a settings record already exists for a round and the user saves new settings for that round
- **THEN** the system updates the existing settings record rather than creating a duplicate row

#### Scenario: Missing settings use editor defaults
- **WHEN** the mapping editor loads a round that has no saved mapping settings
- **THEN** the system displays the round using the first available basket course and same-layout off

### Requirement: Round settings save behavior
The system SHALL save round mapping settings together with the mapping editor's submitted mapping values.

#### Scenario: Round settings are saved atomically with mappings
- **WHEN** the local user submits the basket variation mapping editor
- **THEN** the system saves submitted round settings and valid mapping changes in one transaction

#### Scenario: Invalid round setting rejects save
- **WHEN** the local user submits a round setting for a round that does not belong to the selected competition
- **THEN** the system rejects the save and does not persist submitted round settings or mapping changes

#### Scenario: Invalid basket course setting rejects save
- **WHEN** the local user submits a selected basket course id that does not exist
- **THEN** the system rejects the save and does not persist submitted round settings or mapping changes

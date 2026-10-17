## MODIFIED Requirements

### Requirement: Eligible player lookup
The system SHALL provide a player lookup data set for selecting players who have personal basket statistics or imported putting statistics.

#### Scenario: Eligible player is listed
- **WHEN** a player has at least one personally eligible basket variation result
- **THEN** the player lookup data includes that player

#### Scenario: Player with eligible hole statistics is listed
- **WHEN** a player has at least one personally eligible basket variation result
- **THEN** the player lookup data includes that player

#### Scenario: Player with imported throws is listed
- **WHEN** a player has imported throw data represented in the personal statistics export
- **THEN** the player lookup data includes that player even if the player has no eligible basket variation result

#### Scenario: Ineligible player is omitted
- **WHEN** a player has neither a personally eligible basket variation result nor imported throw data
- **THEN** the player lookup data does not include that player

#### Scenario: Player display label includes PDGA number
- **WHEN** a listed player has a PDGA number
- **THEN** the lookup display label includes the player name and PDGA number

#### Scenario: Player display label without PDGA number
- **WHEN** a listed player has no PDGA number
- **THEN** the lookup display label includes the player name without a PDGA number suffix

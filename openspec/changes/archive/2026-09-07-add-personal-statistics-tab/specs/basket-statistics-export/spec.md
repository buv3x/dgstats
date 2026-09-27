## ADDED Requirements

### Requirement: Personal statistics export files
The system SHALL export personal basket statistics data as human-readable JSON under the GitHub Pages data directory.

#### Scenario: Player lookup file is written
- **WHEN** the local user triggers a statistics export and at least one player has eligible personal statistics
- **THEN** the system writes `docs/data/players.json`

#### Scenario: Personal stats directory is created
- **WHEN** `docs/data/personal-stats` does not exist during export
- **THEN** the system creates the directory before writing personal statistics files

#### Scenario: Personal stats files are written
- **WHEN** the local user triggers a statistics export and a player has eligible personal statistics
- **THEN** the system writes one personal statistics file for that player under `docs/data/personal-stats/`

#### Scenario: Existing personal files are overwritten
- **WHEN** a personal statistics file already exists for an eligible player during export
- **THEN** the system overwrites it with the newly exported personal statistics file

#### Scenario: Manifest includes personal statistics paths
- **WHEN** the system writes the basket statistics manifest
- **THEN** the manifest includes the relative player lookup path and the relative personal statistics file path template

### Requirement: Personal statistics export player identity
The system SHALL export only the player identity fields needed for personal statistics lookup and display.

#### Scenario: Eligible player identity is exported
- **WHEN** a player has at least one eligible personal statistics row
- **THEN** the player lookup file includes player id, player name, PDGA number when available, display label, and personal statistics file path

#### Scenario: Ineligible player identity is excluded
- **WHEN** a player has no eligible personal statistics rows
- **THEN** the player lookup file does not include that player

#### Scenario: Unneeded player identity is excluded
- **WHEN** player lookup or personal statistics files are exported
- **THEN** they do not include player profile URL, city, country, nationality, or other player-identifying fields beyond id, name, PDGA number, and display label

### Requirement: Personal statistics export rows
The system SHALL export eligible personal basket variation rows with calculated ratings and score summaries.

#### Scenario: Personal row contains descriptors
- **WHEN** a personal basket variation row is exported
- **THEN** it includes basket course identity and name, basket identity and label, variation identity and label, global sample count, personal result count, decimal calculated rating, rounded display rating, and scores

#### Scenario: Personal scores preserve chronological order
- **WHEN** scores are exported for a personal basket variation row
- **THEN** they are sorted by round date ascending and round id ascending when dates are equal

#### Scenario: Personal rows are exported in display order
- **WHEN** a personal statistics file is written
- **THEN** its variation rows are ordered by decimal calculated rating from highest to lowest

### Requirement: Personal statistics export diagnostics
The system SHALL report diagnostic counts for the personal statistics export.

#### Scenario: Export diagnostics include personal files
- **WHEN** the local user completes a statistics export
- **THEN** the administration page displays counts for eligible personal players and generated personal statistics files

#### Scenario: Snapshot metadata includes personal diagnostics
- **WHEN** the system writes the statistics manifest
- **THEN** the manifest diagnostic metadata includes eligible personal player count and generated personal statistics file count

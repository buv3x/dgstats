## MODIFIED Requirements

### Requirement: Personal statistics export files
The system SHALL export personal basket statistics and putting statistics as human-readable JSON under the GitHub Pages data directory.

#### Scenario: Player lookup file is written
- **WHEN** the local user triggers a statistics export and at least one player has eligible hole or putting statistics
- **THEN** the system writes `docs/data/players.json`

#### Scenario: Personal stats directory is created
- **WHEN** `docs/data/personal-stats` does not exist during export
- **THEN** the system creates the needed directory before writing personal statistics files

#### Scenario: Personal stats files are written
- **WHEN** the local user triggers a statistics export and a player has eligible hole or putting statistics
- **THEN** the system writes one personal statistics file for that player under `docs/data/personal-stats/`

#### Scenario: Existing personal files are overwritten
- **WHEN** a personal statistics file already exists for an eligible player during export
- **THEN** the system overwrites it with the newly exported personal statistics file

#### Scenario: Manifest includes personal statistics paths
- **WHEN** the system writes the basket statistics manifest
- **THEN** the manifest includes the relative player lookup path and the relative personal statistics file path template

### Requirement: Personal statistics export player identity
The system SHALL export only the player identity fields needed for personal and putting statistics lookup and display.

#### Scenario: Eligible player identity is exported
- **WHEN** a player has at least one eligible hole or putting statistic
- **THEN** the player lookup file includes player id, player name, PDGA number when available, display label, and personal statistics file path

#### Scenario: Ineligible player identity is excluded
- **WHEN** a player has no eligible hole or putting statistics
- **THEN** the player lookup file does not include that player

#### Scenario: Unneeded player identity is excluded
- **WHEN** player lookup or personal statistics files are exported
- **THEN** they do not include player profile URL, city, country, nationality, or other player-identifying fields beyond id, name, PDGA number, and display label

## ADDED Requirements

### Requirement: Personal putting statistics export
The system SHALL include C1x and C2 putting summaries in each eligible personal statistics file.

#### Scenario: Putting summary is exported
- **WHEN** a player has imported throw data
- **THEN** the personal statistics file includes successful and total counts for C1x and C2
- **AND** it includes the calculated two-decimal percentage for each statistic when its total is greater than zero

#### Scenario: Throw-only player file is exported
- **WHEN** a player has imported throw data but no eligible regression-based hole rows
- **THEN** the export still writes that player’s personal statistics file
- **AND** the file contains putting statistics with an empty hole-statistics collection

## ADDED Requirements

### Requirement: Putting round completeness export
The personal statistics export SHALL expose completeness metadata for every represented player putting round, including rounds that have no imported throw rows.

#### Scenario: Complete player round is exported
- **WHEN** all expected layout holes for a player round have valid throw sequences ending in zone 5
- **THEN** the corresponding round aggregate contains `complete: true`

#### Scenario: Incomplete player round is exported
- **WHEN** any expected layout hole is missing or has an invalid final throw zone
- **THEN** the corresponding round aggregate contains `complete: false`
- **AND** the aggregate remains in the player’s personal statistics JSON

#### Scenario: Round result with no throws is exported
- **WHEN** a player round result is available but has no related throw rows
- **THEN** the export can still create its round aggregate from round-result metadata
- **AND** the aggregate is marked incomplete

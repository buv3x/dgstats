## ADDED Requirements

### Requirement: Project-root statistics export path
The system SHALL write the basket statistics snapshot under the project directory root rather than under the JVM process working directory.

#### Scenario: Export uses project docs data directory
- **WHEN** the local user triggers a basket statistics export from any JVM working directory
- **THEN** the system writes the snapshot to `<project-root>/docs/data/statistics.json`

#### Scenario: Export creates project docs data directory
- **WHEN** `<project-root>/docs/data` does not exist during export
- **THEN** the system creates that directory before writing the snapshot

#### Scenario: Export overwrites project snapshot
- **WHEN** `<project-root>/docs/data/statistics.json` already exists during export
- **THEN** the system overwrites that file

### Requirement: Absolute export path visibility
The system SHALL report the resolved absolute statistics snapshot path after export.

#### Scenario: Export result shows absolute path
- **WHEN** the local user completes a basket statistics export
- **THEN** the export result displays the normalized absolute path of the written `statistics.json` file

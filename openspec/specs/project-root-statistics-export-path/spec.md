# project-root-statistics-export-path Specification

## Purpose
TBD - created by archiving change root-statistics-export-path. Update Purpose after archive.
## Requirements
### Requirement: Project-root statistics export path
The system SHALL write the basket statistics manifest and course statistics files under the project directory root rather than under the JVM process working directory.

#### Scenario: Export uses project docs data directory
- **WHEN** the local user triggers a basket statistics export from any JVM working directory
- **THEN** the system writes the manifest to `<project-root>/docs/data/statistics.json`

#### Scenario: Export uses project docs courses directory
- **WHEN** the local user triggers a basket statistics export from any JVM working directory
- **THEN** the system writes course statistics files under `<project-root>/docs/data/courses/`

#### Scenario: Export creates project docs data directories
- **WHEN** `<project-root>/docs/data` or `<project-root>/docs/data/courses` does not exist during export
- **THEN** the system creates the needed directories before writing statistics files

#### Scenario: Export overwrites project manifest
- **WHEN** `<project-root>/docs/data/statistics.json` already exists during export
- **THEN** the system overwrites that file

#### Scenario: Export overwrites included project course files
- **WHEN** a course statistics file already exists under `<project-root>/docs/data/courses/` for an included basket course
- **THEN** the system overwrites that file

### Requirement: Absolute export path visibility
The system SHALL report the resolved absolute statistics export paths after export.

#### Scenario: Export result shows absolute manifest path
- **WHEN** the local user completes a basket statistics export
- **THEN** the export result displays the normalized absolute path of the written `statistics.json` manifest file

#### Scenario: Export result shows course file output count
- **WHEN** the local user completes a basket statistics export
- **THEN** the export result displays how many course statistics files were written under the project `docs/data/courses` directory

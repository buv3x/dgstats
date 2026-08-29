## ADDED Requirements

### Requirement: Snapshot planning section
The planning artifact SHALL include a high-level section for future static data snapshots derived from the local database.

#### Scenario: Plan describes snapshot stage
- **WHEN** the planning artifact is reviewed
- **THEN** it describes repository-hosted static data files as the future bridge between local database import and GitHub Pages visualization

### Requirement: Snapshot implementation exclusion
The planning artifact SHALL state that no data export implementation or snapshot file generation is included in this change.

#### Scenario: Scope is checked
- **WHEN** the change scope is reviewed
- **THEN** it is clear that export code, generated data files, and repository data publication are deferred to later changes

### Requirement: Snapshot open questions
The planning artifact SHALL identify unresolved questions that affect future static snapshot design.

#### Scenario: Snapshot questions are reviewed
- **WHEN** a later implementation change is considered
- **THEN** the planning artifact provides snapshot-related questions about file format, file size, data scope, storage location, and metadata

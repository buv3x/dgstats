# basket-statistics-export Specification Delta

## MODIFIED Requirements

### Requirement: Snapshot includes metadata

The system SHALL include export time, diagnostic metadata, and Description-tab
metadata in the statistics manifest.

#### Scenario: Snapshot includes metadata

- **WHEN** the system writes the statistics manifest
- **THEN** the manifest includes export time and diagnostic metadata

#### Scenario: Snapshot includes Description metadata

- **WHEN** the system writes the statistics manifest
- **THEN** the manifest includes the number of competitions represented by
  rated, mapped exported samples, the number of included players represented by
  the player lookup export, and a latest included competition display value

#### Scenario: Latest competition is selected deterministically

- **WHEN** included competitions have non-null start dates
- **THEN** the latest competition metadata identifies the competition with the
  greatest start date

#### Scenario: Latest competition has no dates

- **WHEN** no included competition has a non-null start date
- **THEN** the export still writes a stable latest-competition fallback based on
  included competition identity rather than failing the export

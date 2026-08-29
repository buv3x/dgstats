## Requirements

### Requirement: PDGA calls occur outside persistence transactions
The system SHALL perform PDGA event-info and round-results HTTP calls outside database transactions used for import persistence.

#### Scenario: Event info fetch is outside transaction
- **WHEN** a pending competition is imported
- **THEN** the PDGA event-info request and its intentional request delay are completed before opening the transaction that persists event metadata

#### Scenario: Round results fetch is outside transaction
- **WHEN** a round/division result batch is imported
- **THEN** the PDGA round-results request and its intentional request delay are completed before opening the transaction that persists that result batch

### Requirement: Import completion semantics are preserved
The system SHALL keep a competition import pending until all required PDGA data for that competition has been fetched and persisted successfully.

#### Scenario: Successful competition import is marked complete
- **WHEN** event metadata and every required round/division result batch for a competition are fetched and persisted successfully
- **THEN** the corresponding `datas.competition_import.imported` value is set to true

#### Scenario: Fetch failure leaves competition pending
- **WHEN** a PDGA event-info or round-results request fails for a competition
- **THEN** the failure is logged and the corresponding `datas.competition_import.imported` value remains false

#### Scenario: Persistence failure leaves competition pending
- **WHEN** event metadata or round/division result persistence fails for a competition
- **THEN** the failure is logged and the corresponding `datas.competition_import.imported` value remains false

### Requirement: Existing request pacing is retained
The system SHALL retain the existing sequential request pacing behavior while changing transaction boundaries.

#### Scenario: Connector delay remains active
- **WHEN** the importer fetches PDGA event-info or round-results data
- **THEN** the existing connector-level delay still runs before the request

#### Scenario: No parallel import behavior is introduced
- **WHEN** multiple competitions or round/division combinations are imported
- **THEN** the importer processes them sequentially without adding parallel request execution

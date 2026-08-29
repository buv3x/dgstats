## Why

PDGA imports currently perform remote API calls inside the database transaction for a competition import. Because the connector intentionally sleeps before each request to avoid excessive PDGA API traffic, the transaction remains open during long network waits and can slow multi-competition imports by holding database resources longer than necessary.

## What Changes

- Move PDGA event-info and round-results fetches outside database transaction boundaries.
- Preserve the existing sequential import behavior and the connector's request delay.
- Keep the existing completion rule: `competition_import.imported` is marked true only after all required PDGA data for that competition has been persisted successfully.
- Keep failed imports pending for manual rerun.
- Do not add parallelization, scheduling, queueing, retries, or rate-limit redesign.

## Capabilities

### New Capabilities
- `pdga-import-transaction-scope`: Covers the requirement that PDGA API waits and HTTP calls happen outside import persistence transactions while preserving completion semantics.

### Modified Capabilities

## Impact

- Affected code: `ImportService` transaction orchestration around competition and round/division imports.
- Affected code: possibly small helper data structures or internal methods to pass fetched PDGA DTO data into transactional persistence.
- Unchanged code: `PdgaConnector` request delay behavior and endpoint calls.
- No schema, API endpoint, dependency, or operational infrastructure changes are expected.

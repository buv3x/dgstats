## 1. Refactor Import Orchestration

- [x] 1.1 Remove the whole-competition `TransactionTemplate` wrapper from `importPendingCompetitions`.
- [x] 1.2 Split one-competition import flow into non-transactional fetch orchestration and transactional persistence helper methods.
- [x] 1.3 Fetch PDGA event info outside any import persistence transaction and validate response data before persisting metadata.

## 2. Persist Metadata With Short Transaction Scope

- [x] 2.1 Persist competition metadata, rounds, divisions, layouts, and layout holes inside a transaction after event info is fetched.
- [x] 2.2 Return lightweight round/division fetch descriptors or persisted IDs from metadata persistence without carrying managed entities across PDGA request waits.

## 3. Persist Round Results With Short Transaction Scope

- [x] 3.1 Fetch each PDGA round-results response outside any import persistence transaction.
- [x] 3.2 Persist each fetched round/division result batch inside a transaction, reloading managed entities as needed.
- [x] 3.3 Mark the `competition_import` row imported in a final transaction only after all required round/division batches are fetched and persisted.

## 4. Verification

- [x] 4.1 Verify failed fetch or persistence paths still leave the import row pending through the existing exception handling.
- [x] 4.2 Compile the application to verify the refactor.

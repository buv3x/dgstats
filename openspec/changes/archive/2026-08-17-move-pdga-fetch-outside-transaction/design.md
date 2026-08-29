## Context

The current PDGA import flow wraps `importCompetition` in `TransactionTemplate.executeWithoutResult`. Inside that method, the service calls `PdgaConnector.getCompetitionInfo` and then calls `PdgaConnector.getRoundResults` for every round/division combination. The connector intentionally sleeps before each API request to avoid sending too many requests to PDGA.

That means a single competition transaction remains open during the event-info request, every round-results request, and every intentional sleep. When multiple competitions are imported sequentially, database transactions stay open much longer than the actual persistence work requires.

## Goals / Non-Goals

**Goals:**

- Ensure PDGA HTTP calls and connector sleeps happen outside database transactions.
- Keep imports sequential.
- Keep the connector's existing delay behavior.
- Preserve idempotent persistence and rerun behavior.
- Mark `competition_import.imported` only after all required data for the competition has been persisted successfully.
- Keep failed competitions pending.

**Non-Goals:**

- No parallel import execution.
- No scheduler, queue, background worker redesign, retry framework, or new rate-limit subsystem.
- No schema changes.
- No broad importer decomposition beyond what is needed to separate fetch and persistence transaction boundaries.
- No batching or bulk persistence optimization in this change.

## Decisions

1. Split one-competition import into explicit fetch and persist phases.

   The importer should first fetch the PDGA event-info response outside a transaction. After validating that event data exists, it should persist competition metadata in a transaction. Then it can determine the round/division combinations to fetch and perform each round-results HTTP call outside a transaction before persisting that response in a transaction.

   Alternative considered: keep one transaction and only move the initial event-info call outside it. That leaves the larger cost, round-results sleeps and HTTP calls, inside the transaction.

2. Use transaction boundaries around persistence, not orchestration.

   `importPendingCompetitions` should no longer wrap the whole `importCompetition` method in a transaction. Instead, transaction scopes should be placed around methods that perform database writes or database reads needed for persistence decisions.

   Alternative considered: annotate the full import method with `@Transactional`. That has the same problem as the current `TransactionTemplate` wrapper.

3. Return or reload persisted identifiers between transaction phases.

   The fetch phase needs stable round/division combinations, and the persistence phase needs managed entities. The implementation can return lightweight IDs or simple round/division descriptors after metadata persistence, then reload managed entities inside the round-results persistence transaction.

   Alternative considered: carry managed JPA entities across non-transactional fetch waits. That risks detached entity confusion and makes transaction ownership less clear.

4. Keep completion marking in the final successful persistence transaction.

   The import row should be marked imported only after metadata and every required round/division response has been fetched and persisted. If any fetch or persistence step throws, the exception should be logged by the existing pending import loop and the import row should remain false.

   Alternative considered: mark metadata completion separately. That changes workflow semantics and is outside this focused change.

## Risks / Trade-offs

- Partial data may commit before a later round/division fetch fails -> Existing idempotent find-or-create behavior and unique constraints allow the next manual rerun to converge.
- More than one transaction per competition makes the control flow slightly more complex -> Keep the orchestration method small and use focused helper methods with clear names.
- Round/division combinations can change between metadata fetch and later round fetches if PDGA data changes mid-import -> Accept this for the local manual importer; rerun behavior remains pending on failure.
- Moving transactions can expose accidental lazy-loading assumptions -> Pass IDs or simple DTO/descriptors between phases and reload entities inside transactional persistence methods.

## Migration Plan

1. Refactor `ImportService.importPendingCompetitions` so it calls the one-competition import orchestration without wrapping it in `TransactionTemplate`.
2. Fetch event info outside a transaction.
3. Persist event metadata in a transaction and return round/division combinations or IDs needed for result fetches.
4. For each round/division combination, fetch round results outside a transaction.
5. Persist each round/division response in a transaction.
6. Mark the `competition_import` row imported in a final transaction after all required fetches and persistence steps complete.
7. Compile the application and run focused import verification if a local database is available.

Rollback is code-only: restore the previous import transaction wrapper. No database rollback is required.

## Open Questions

None.

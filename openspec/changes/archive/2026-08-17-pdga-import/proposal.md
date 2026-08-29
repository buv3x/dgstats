## Why

The project has a seeded list of PDGA tournament ids and sample PDGA API payloads, but it cannot yet import those tournaments into the local database for analysis. This change turns the existing planning direction into a concrete local-only importer that reads pending competition ids, fetches PDGA event and round results, stores normalized data, and marks completed imports.

## What Changes

- Add the missing Liquibase tables and columns needed to store PDGA competition metadata, divisions, rounds, layouts, players, round results, and hole scores.
- Rename the current `round_group` table concept to `round_division` so the database model matches the importer loop over round/division combinations.
- Add JPA entities and repositories for the normalized import tables.
- Add typed PDGA DTOs for the event-info and round-results API responses shown in `src/main/resources/examples/`.
- Extend the PDGA connector with typed calls for competition info and round results.
- Implement a local manual import service that:
  - reads unimported rows from `datas.competition_import`;
  - imports event metadata, rounds, divisions, courses, layouts, and layout holes;
  - calls the PDGA round-results endpoint for each round/division combination;
  - stores players, results, and per-hole scores;
  - marks the `competition_import` row imported after the competition is fully processed.
- Provide a simple local trigger for running the import without adding security, sessions, scheduling, parallelization, or production operations.

## Capabilities

### New Capabilities
- `pdga-import-processing`: Covers local manual PDGA competition import from queued tournament ids into normalized database tables.

### Modified Capabilities

None.

## Impact

- Affected application areas:
  - Liquibase schema in `src/main/resources/db/changelog/db.changelog-master.sql`
  - JPA domain entities and repositories under `src/main/java/com/datascience`
  - PDGA DTOs under `src/main/java/com/datascience/dto/pdga`
  - `PdgaConnector` and `ImportService`
  - a simple local trigger endpoint or runner
- External integration:
  - PDGA live API event endpoint: `live_results_fetch_event?TournID=...`
  - PDGA live API round endpoint: `live_results_fetch_round?TournID=...&Division=...&Round=...`
- Database:
  - PostgreSQL-oriented Liquibase changes continue using the current formatted SQL style.
- Operational scope:
  - Local manual execution only; no authentication, scheduling, session management, parallel execution, deployment, or public API hardening.

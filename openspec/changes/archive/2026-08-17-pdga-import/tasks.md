## 1. Schema

- [x] 1.1 Add a Liquibase formatted SQL changeset for the PDGA import schema changes.
- [x] 1.2 Rename `datas.round_group` to `datas.round_division` and align primary key, foreign key, and index names where practical.
- [x] 1.3 Add missing metadata columns to `datas.competition`, `datas.round`, and `datas.course` for PDGA tournament, round, and course data.
- [x] 1.4 Add tables and constraints for competition divisions, layouts, layout holes, players, round divisions, round results, and hole scores.
- [x] 1.5 Add uniqueness constraints or indexes that support idempotent local reruns.
- [x] 1.6 Keep existing `basket` and `basket_variation` tables in place for later use.

## 2. Domain And Repositories

- [x] 2.1 Add or update JPA entities for competition, division, round, course, layout, layout hole, player, round division, round result, and hole score tables.
- [x] 2.2 Add Spring Data repositories for the new entities.
- [x] 2.3 Add repository finder methods needed for pending import selection and find-or-create persistence.

## 3. PDGA DTOs And Connector

- [x] 3.1 Add a generic PDGA response DTO for the PDGA wrapper shape without adding hash persistence or hash checking.
- [x] 3.2 Fill event-info DTOs for competition metadata, divisions, rounds, layouts, and layout hole details.
- [x] 3.3 Add round-results DTOs for result batches, layouts, holes, player scores, and hole scores.
- [x] 3.4 Update `PdgaConnector` to return typed event-info data.
- [x] 3.5 Add `PdgaConnector` support for fetching typed round results by tournament id, division code, and round number.

## 4. Import Processing

- [x] 4.1 Implement pending competition lookup using `competition_import.imported = false`.
- [x] 4.2 Implement competition metadata import and find-or-create persistence for competition, divisions, rounds, courses, layouts, and layout holes.
- [x] 4.3 Implement round/division iteration based on imported rounds and divisions.
- [x] 4.4 Implement round result persistence for round divisions, players, aggregate round results, and per-hole scores.
- [x] 4.5 Mark a competition import row as imported only after all required event and round/division data is persisted successfully.
- [x] 4.6 Log failed competition imports and keep them pending for manual rerun.

## 5. Local Trigger And Verification

- [x] 5.1 Add a simple local trigger for running the pending PDGA import.
- [x] 5.2 Remove or adapt any placeholder importer/test endpoint code that conflicts with the final trigger.
- [x] 5.3 Compile the application to verify the implementation.
- [x] 5.4 Review the implementation against the `pdga-import-processing` spec scenarios.

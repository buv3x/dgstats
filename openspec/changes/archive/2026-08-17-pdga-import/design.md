## Context

The repository is a Spring Boot 3 application with JPA, Liquibase, PostgreSQL configuration, a seeded `datas.competition_import` table, a mostly empty `ImportService`, and a `PdgaConnector` that currently fetches competition info as a raw `Map`. Existing PDGA sample files show two external payload shapes:

- event info from `live_results_fetch_event`, containing tournament metadata, divisions, rounds, layouts, and layout hole details;
- round results from `live_results_fetch_round`, containing one division/round result set with layout metadata, holes, player scores, totals, ratings, places, and hole scores.

The application is expected to run locally and manually. Performance, production operations, security, session management, parallel execution, and scheduling are not needed for this change.

## Goals / Non-Goals

**Goals:**

- Import all rows from `datas.competition_import` where `imported = false`.
- Store enough normalized PDGA data for local analysis: competitions, divisions, rounds, courses, layouts, layout holes, players, round/division result batches, round results, and hole scores.
- Use Liquibase formatted SQL in the same `db.changelog-master.sql` style already present.
- Replace the current `round_group` concept with `round_division`, because the import unit is a specific round and PDGA division.
- Keep PDGA API parsing typed through DTOs instead of raw `Map` handling in business logic.
- Keep execution local, explicit, sequential, and easy to inspect.

**Non-Goals:**

- No authentication, authorization, session management, public API hardening, or multi-user support.
- No scheduler, background worker, queue, parallel requests, retry framework, or rate-limit subsystem.
- No static snapshot export or GitHub Pages visualization.
- No exhaustive persistence of every PDGA response field.
- No raw-response archive unless it becomes necessary during implementation.

## Decisions

1. Use typed PDGA DTOs at the connector boundary.

   The connector should return `PdgaResponse<PdgaCompetitionInfo>` and `PdgaResponse<PdgaRoundResults>` rather than raw `Map`. This keeps the service layer focused on mapping known fields to local entities and makes missing/renamed PDGA fields easier to find during tests or manual runs.

   Alternative considered: continue with raw `Map`. That would be quick for a spike, but it pushes fragile string keys into the importer and makes the result mapping hard to review.

2. Normalize analysis-relevant data instead of storing only raw JSON.

   The importer should create tables for the entities needed by likely analysis and visualization work: competition metadata, divisions, rounds, course/layout/hole structure, players, round results, and per-hole scores.

   Alternative considered: store only raw JSON responses. That would preserve all source data but would postpone the hard modeling work and make database queries awkward.

3. Rename `round_group` to `round_division`.

   PDGA round results are fetched by `TournID`, `Division`, and `Round`. A `round_division` table directly represents this batch and can hold API-level fields such as PDGA live round id, PDGA division id/code, pool, and layout. This is clearer than `round_group`, which does not communicate the import loop or API shape.

   Alternative considered: keep `round_group` and repurpose it. That avoids a rename, but creates a misleading model before the table has meaningful use.

4. Use local id primary keys plus PDGA identifiers for deduplication.

   Existing tables use `BIGSERIAL` ids. New tables should follow that style while adding PDGA ids and uniqueness constraints where natural, such as `competition.pdga_id`, `division.pdga_division_id` per competition, `round` number per competition, `layout.pdga_layout_id`, and PDGA score/result ids where present.

   Alternative considered: use PDGA ids as primary keys. Some entities are only unique in context, some source ids can be absent, and local generated ids keep relationships consistent.

5. Make import idempotent enough for local reruns.

   The service should upsert or find existing records using unique keys before inserting dependents. A rerun of an unmarked or partially imported competition should not create duplicate competitions, divisions, rounds, players, results, or hole scores.

   Alternative considered: delete and reinsert each competition. That is simpler in the short term but riskier with foreign keys and manual inspection.

6. Mark `competition_import.imported` only after all round/division result calls succeed.

   The import row should remain pending if competition metadata imports but a later round/division call fails. This makes manual rerun behavior simple and visible.

   Alternative considered: mark metadata imported separately from results. That gives finer status, but this first local version can keep one completion flag.

7. Trigger import through one simple local entry point.

   A local REST endpoint, for example under `/api/import/pdga`, is sufficient and consistent with the existing minimal web controller. A command-line runner is also acceptable if implementation strongly prefers import-on-start, but an explicit endpoint avoids surprise API calls on every application boot.

8. Leave `basket` and `basket_variation` in place for later use.

   The importer should not remove or repurpose the existing `basket` and `basket_variation` tables. They are outside the active PDGA import model for now, while layouts and layout holes become the tables used for PDGA course structure in this change.

   Alternative considered: remove the existing basket tables during schema cleanup. That would reduce unused schema surface, but these tables are expected to support later functionality.

9. Do not implement PDGA hash checking or reimport detection.

   PDGA competitions are treated as one-time imports. The DTO layer can map the response shape needed to read `data`, but the importer does not need to persist response hashes or compare them to detect source changes.

   Alternative considered: store response hashes on competition and round/division rows. That would support future change detection, but it adds schema and logic for a workflow that is explicitly not needed.

10. Log failed imports and leave them pending.

   When a PDGA call or persistence step fails, logging the error is enough for this local tool. The `competition_import.imported` flag remains false so the user can rerun the import manually.

   Alternative considered: add error message and status columns to `competition_import`. That would improve structured failure inspection, but console/application logs are sufficient for the current local-only workflow.

## Risks / Trade-offs

- PDGA response fields are undocumented or may change -> Keep DTOs limited to fields used by the importer and allow unknown JSON fields to be ignored.
- Partial imports can leave normalized rows before the import row is marked complete -> Use uniqueness constraints and idempotent find-or-create behavior so reruns converge.
- Existing `round_group` table may already exist in local databases -> Add Liquibase migration using formatted SQL, renaming it to `round_division` before adding new relationships and columns.
- `competition.course_id NOT NULL` is too narrow for multi-layout or multi-course events -> Keep `course_id` only if needed for existing schema compatibility, but model actual PDGA layouts with a separate layout table linked to competition and course.
- Player identity can be ambiguous when `PDGANum` is missing -> Use PDGA number when present; otherwise store player records with nullable PDGA number and avoid assuming global uniqueness for missing ids.
- PDGA scores include comma-separated fields and `HoleScores` arrays -> Prefer the explicit `HoleScores` array for per-hole score rows and keep aggregate score fields separately.
- No hash checking means PDGA source changes after import will not be detected -> Accept this because competitions are not expected to be reimported.

## Migration Plan

1. Add a new Liquibase changeset in `db.changelog-master.sql` using the existing formatted SQL style.
2. Rename `datas.round_group` to `datas.round_division` and update constraints/index names as appropriate.
3. Add missing columns to existing tables where needed for PDGA metadata.
4. Add new normalized tables for divisions, layouts, layout holes, players, round results, and hole scores.
5. Add JPA entities and repositories that match the migrated schema.
6. Add typed DTOs and connector methods.
7. Implement the import service and local trigger.
8. Validate with compilation and focused tests or manual execution against a small seeded tournament id.

Rollback for local development can be handled by restoring the database or manually dropping the newly added objects. No production rollback path is required because this application is local-only.

## Open Questions

None.

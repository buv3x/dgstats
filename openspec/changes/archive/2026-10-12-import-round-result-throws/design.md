## Context

The application imports PDGA competition, round, player, round-result, and hole-score data through `ImportService`. The PDGA throw-timeline response contains `scoreThrows`; each entry identifies a hole through `liveHoleScore.holeOrdinal` and contains `holeThrows`. Each hole throw contains `scoreOrdinal`, `penaltyCount`, and a `liveScoreThrow` object with `throwId`, `zoneId`, `distanceToTarget`, `penaltyType`, `dropZoneId`, and `dropDistanceToTarget`.

The existing implementation stores each hole's complete `holeThrows` array as JSONB. The revised design normalizes individual throws so their fields can be queried directly. The import must remain restartable, tolerate valid empty response collections, and handle round results that unexpectedly have no `pdga_score_id`.

## Goals / Non-Goals

**Goals:**

- Add a resumable import operation for throw timelines.
- Persist one relational row per individual throw.
- Preserve hole ordinal, score ordinal, penalty count, and all `liveScoreThrow` fields.
- Make each round result an independent transaction boundary.
- Mark empty successful imports and null-score round results as completed.
- Keep failed requests or persistence failures eligible for retry.

**Non-Goals:**

- Re-importing or refreshing records already marked as imported.
- Changing the existing competition, round-results, or hole-score import flow.
- Exposing throw data through a read API as part of this change.
- Preserving the nested JSONB payload as the primary storage format.

## Decisions

1. **Use a boolean completion flag on `round_result`.**

   Add `throws_imported BOOLEAN NOT NULL DEFAULT FALSE`. The import selects every round result with this flag set to false. A null `pdga_score_id` is handled locally by marking the result imported, because the endpoint cannot be called without a score identifier.

   Alternative considered: exclude null score IDs. This would leave anomalous records permanently eligible and repeatedly visible to the importer.

2. **Persist one normalized row per individual throw.**

   `round_result_throws` contains a generated primary key, a foreign key to `round_result`, `hole_ordinal`, `score_ordinal`, `penalty_count`, `throw_id`, `zone_id`, `distance_to_target`, `penalty_type`, `drop_zone_id`, and `drop_distance_to_target`. A uniqueness constraint on `(round_result_id, hole_ordinal, score_ordinal)` prevents duplicate logical throws during retries or upserts.

   All values are mapped from the response: `hole_ordinal` from `liveScoreThrow`'s containing `liveHoleScore.holeOrdinal`, the two outer fields from each `holeThrows` entry, and the remaining fields from `liveScoreThrow`.

   Alternative considered: retaining the complete `holeThrows` array as JSONB. That prevents direct field-level querying and was the source of the import limitation.

3. **Do not create placeholder rows for empty collections.**

   A null or empty `scoreThrows` collection produces no child rows. A null or empty `holeThrows` collection produces no rows for that hole. A successful response is still considered processed and sets `throws_imported` to true.

4. **Fetch outside the transaction and persist inside a per-result transaction.**

   `inputThrows` obtains the unimported round-result identifiers. For a non-null score ID it calls the PDGA endpoint before opening a transaction. For a null score ID it skips the request and opens a transaction only to mark the result imported. The transaction reloads the result, persists all normalized throw rows, and updates the completion flag atomically.

   A request failure leaves the flag false. A persistence failure rolls back both throw rows and the completion update.

5. **Use an additive import endpoint.**

   Keep the POST endpoint under the existing `/api` resource, consistent with `/api/import/pdga`, and retain the asynchronous import behavior.

## Risks / Trade-offs

- **[Existing JSONB rows may already exist]** → Provide a Liquibase migration or replacement strategy that removes/restructures the old JSONB columns and preserves data only if a reliable conversion is required.
- **[PDGA response shape changes]** → Use DTOs that ignore unknown fields while explicitly mapping the required nested fields.
- **[Nullable source values]** → Keep nullable database columns for optional distance, penalty, zone, and drop-zone values.
- **[Concurrent endpoint invocations]** → Assume one operator-triggered run at a time; enforce the logical throw uniqueness constraint and use upsert/find behavior to avoid duplicate rows.
- **[Process stops during a remote request]** → Update `throws_imported` only in the persistence transaction, leaving the record eligible on restart.

## Migration Plan

1. Add or alter the `throws_imported` column on `datas.round_result`.
2. Replace the JSONB payload schema with normalized columns on `datas.round_result_throws`.
3. Deploy the updated entities, DTOs, repositories, and import service.
4. Run the throw import; completed records are skipped and failed records remain eligible.

If the existing JSONB table has already been populated, the migration must either transform its arrays into normalized rows before dropping the JSONB column or explicitly document that those rows are discarded and re-imported from PDGA.

## Open Questions

- Whether deployed JSONB rows must be converted in-place or may be discarded and re-imported from PDGA.

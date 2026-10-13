## 1. Database and persistence model

- [x] 1.1 Add or retain a non-null `throws_imported BOOLEAN DEFAULT FALSE` column on `datas.round_result`.
- [x] 1.2 Replace the JSONB payload design for `datas.round_result_throws` with normalized columns for `hole_ordinal`, `score_ordinal`, `penalty_count`, `throw_id`, `zone_id`, `distance_to_target`, `penalty_type`, `drop_zone_id`, and `drop_distance_to_target`.
- [x] 1.3 Add a uniqueness constraint on `(round_result_id, hole_ordinal, score_ordinal)` and retain the foreign key to `round_result`.
- [x] 1.4 Add nullable entity fields for optional PDGA throw values and remove the JSONB mapping.
- [x] 1.5 Add repository methods for selecting all unimported round results and finding/upserting normalized throws by round result, hole ordinal, and score ordinal.
- [x] 1.6 Decide and implement the migration strategy for any already-populated JSONB throw rows.

## 2. PDGA throw-timeline integration

- [x] 2.1 Add typed DTOs for `scoreOrdinal`, `penaltyCount`, `liveHoleScore.holeOrdinal`, and every field in `liveScoreThrow`.
- [x] 2.2 Add a `PdgaConnector` method that requests the throw-timeline endpoint with `pdga_score_id` as its path parameter and preserves existing request pacing.
- [x] 2.3 Validate the example response mapping, including null optional fields and absent or empty `scoreThrows` and `holeThrows` collections.

## 3. Resumable import service

- [x] 3.1 Update `inputThrows` to obtain all round results with `throws_imported = false`, including results with null `pdga_score_id`.
- [x] 3.2 Mark null-score round results imported in a transaction without making a remote request or creating child rows.
- [x] 3.3 Fetch non-null-score timelines outside persistence transactions and log/continue when an individual request fails.
- [x] 3.4 Persist one normalized row per non-empty `holeThrows` entry using the containing hole ordinal and all mapped throw fields.
- [x] 3.5 Ensure empty or absent `scoreThrows` and `holeThrows` collections create no rows but complete the round result.
- [x] 3.6 Set `throws_imported = true` only after successful per-round-result persistence, with failures rolling back rows and the flag.
- [x] 3.7 Preserve restart behavior by skipping imported results and retrying failed or interrupted results.

## 4. Import endpoint and verification

- [x] 4.1 Preserve the POST `/api/import/throws` endpoint and asynchronous acknowledgement behavior.
- [x] 4.2 Compile the application and resolve schema, DTO, persistence, transaction, and endpoint integration errors.

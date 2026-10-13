## Why

The current throw import stores nested `holeThrows` data as JSONB, which makes individual throws difficult to query and does not expose the score ordinal, penalty count, or fields from `liveScoreThrow`. The import also needs explicit completion behavior for empty responses and the unexpected case where a round result has no PDGA score identifier.

## What Changes

- Add a `throws_imported` completion flag to `round_result`.
- Replace JSONB throw storage with normalized rows in `round_result_throws`, one row per throw.
- Persist `hole_ordinal`, `score_ordinal`, `penalty_count`, and all fields from `liveScoreThrow`.
- Add a PDGA connector operation for the throw-timeline endpoint when `pdga_score_id` is available.
- Select all round results with `throws_imported = false`.
- Mark round results with a null `pdga_score_id` as imported without making a remote request.
- Import each round result in its own transaction and mark it complete only after successful processing.
- Create no throw rows when `scoreThrows` or an individual `holeThrows` collection is empty or absent, while still marking the round result imported after a successful import decision.
- Leave failed requests or failed transactions unmarked so the process can be restarted safely.

## Capabilities

### New Capabilities

- `round-result-throws-import`: Import and persist PDGA throw timelines for eligible round results with per-record resumability.

### Modified Capabilities

None.

## Impact

- Affected application areas: `ImportResource`, `ImportService`, `PdgaConnector`, PDGA DTOs, round-result persistence, and repositories.
- Affected database: Liquibase schema changes for `round_result` and the normalized `round_result_throws` table.
- Existing JSONB throw data, if already deployed, requires a migration or explicit replacement strategy before the normalized schema is used.
- No existing API contract is removed; the new import endpoint remains additive.

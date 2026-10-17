## Context

The putting exporter currently starts from normalized throw rows, groups them by `round_result_id` and hole ordinal, and ignores holes whose final throw is not in zone 5. It then adds classified attempts from valid holes both to the player total and to the round summary. This permits a partially imported player round to inflate overall putting totals.

`RoundResult` already identifies the player, round, and assigned layout. The layout stores the expected hole count. Throw import intentionally creates no placeholder rows for empty score or hole-throw collections, so a round with no throw rows must be discovered through a left-joined round-result query if it is to appear as an incomplete UI row.

## Goals / Non-Goals

**Goals:**

- Evaluate completeness independently for each player and `round_result`.
- Require every expected layout hole to have at least one throw and an ordered final throw with `zone_id = 5`.
- Preserve round-level metrics for incomplete rounds while excluding them from overall totals.
- Export enough metadata for the UI to identify and style incomplete rounds.
- Retain rounds with no classified attempts when they are represented by an imported player round.

**Non-Goals:**

- Changing throw import, hole classification, C1x/C2 distance rules, or successful-putt determination.
- Changing hole-statistics eligibility or basket-variation calculations.
- Adding a database column or public API endpoint.
- Hiding incomplete rounds from the Personal statistics view.

## Decisions

### Determine completeness at the player-round boundary

The query projection will retain `round_result_id`, player identity, round metadata, and expected hole count from the result layout (falling back to the round-division layout when necessary). The aggregation will first group rows by round result and then by hole ordinal. A player-round is complete only when its expected hole count is known, every expected ordinal is represented, every hole has a non-empty throw sequence, and every sequence ends in zone 5 after sorting by score ordinal. Missing layout information is treated as incomplete because completeness cannot be established.

This keeps one player’s incomplete result from invalidating another player’s result for the same competition round. It also preserves support for score-ordinal gaps caused by penalties: ordinals are ordered, but need not be consecutive.

### Start discovery from round results and left-join throws

Replace the putting projection’s throw-only root with `RoundResult` and left-join its throws. Rows with null throw fields still carry the player-round descriptor and are used to create an incomplete round with zero classified attempts. Existing throw rows continue to be processed through the same hole-level classifier.

### Separate raw round metrics from eligible overall metrics

Each player builder will maintain round builders containing all classified attempts from that player-round, plus a completeness flag. The overall builder will receive an attempt only after the corresponding player-round has been proven complete. Round summaries will be emitted for represented player-rounds, including incomplete rounds with zero attempts.

The exported round summary will add a boolean `complete` field. Existing totals remain the combined metrics of complete rounds only.

### Style incomplete rows in the static UI

The putting renderer will add a dedicated incomplete class to round lines when `complete` is false. CSS will apply italic text and strikethrough to the complete line, including its metrics and labels. Missing `complete` in older JSON is treated as true for backward compatibility, while missing/empty round arrays continue to use the existing total-only behavior.

### Preserve deterministic round ordering

Round summaries remain sorted by round date and round ID. Grouping uses `round_result_id` for completeness and metrics; display labels never determine identity.

## Risks / Trade-offs

- [Some historical results have no layout hole count] → Mark those player-rounds incomplete rather than counting unverifiable attempts.
- [Left-joining throws may produce a descriptor row with null throw fields] → Guard throw-specific processing and create only one round builder per round result.
- [Incomplete round metrics may be mistaken for included totals] → Keep the overall totals separate and make the row styling visibly indicate exclusion.
- [Existing JSON lacks `complete`] → Treat absent metadata as complete so previously generated exports remain readable.
- [Generated personal JSON changes are broad] → Regenerate exports after code changes and verify representative complete, partially complete, and empty-throw rounds.

## Migration Plan

No database migration is required. Deploy the exporter and UI changes, regenerate personal statistics JSON, and publish the updated static assets. Rollback consists of reverting the exporter/UI changes and regenerating the previous export format; the UI remains tolerant of older files.

## Open Questions

None. The agreed behavior is per-player-round completeness, with missing holes making the round incomplete.

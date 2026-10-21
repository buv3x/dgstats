## Context

The exporter already calculates `threePlusPuttLostThrows` on each `PuttingRoundBuilder` while processing valid holes. The value is currently used only for overall and yearly `threePlusPuttPerRound` aggregates; `PuttingRoundSummary` does not expose it. The browser renderer builds each round-detail line from that summary and currently displays C1x and C2 only.

The change must preserve the existing round identity, ordering, incomplete-round styling, aggregate calculations, and backward compatibility with previously generated JSON files.

## Goals / Non-Goals

**Goals:**

- Export the existing per-round lost-throw count in each round summary.
- Render it as `3+putts: N` after C2 when `N > 0`.
- Ensure a round with only three-plus-putt data remains displayable.
- Keep older exports without the field readable.

**Non-Goals:**

- Do not change how three-plus-putts are calculated or aggregated.
- Do not add the metric to overall/yearly summary lines as part of this change.
- Do not add a leaderboard category or modify round ordering.

## Decisions

### Reuse the existing per-round calculation

Add the builder's `threePlusPuttLostThrows` value to `PuttingRoundSummary` rather than recalculating it in the UI or introducing another aggregation path. This keeps round detail consistent with the already-tested aggregate calculation.

### Use a nullable/optional serialized field for compatibility

Expose the count as an integer in newly generated summaries. The UI must tolerate the field being absent in older exports and treat absent, null, non-numeric, or zero values as not displayable. A positive value is rendered as `3+putts: N`.

### Extend displayability filtering

The existing filter hides incomplete rounds with no C1x/C2 attempts. Include a round when its `threePlusPuttLostThrows` value is positive, so a valid three-plus-putt round is not lost merely because it has no C1x or C2 classified attempts. Complete and legacy rounds retain their current visibility rules.

### Preserve comma-separated line formatting

Append the new metric after the existing C2 line using the same text-node comma separator pattern. C1x and C2 remain unchanged and bold; the new `3+putts` label uses the existing metric-line styling.

## Risks / Trade-offs

- [Older JSON files lack the new property] → Use defensive numeric checks and omit the metric when unavailable.
- [Incomplete rounds could be hidden despite having three-plus-putt data] → Include positive three-plus-putt counts in the existing displayability predicate.
- [The field could be confused with the aggregate rate] → Use the explicit `3+putts` label and render the round-level integer only.

## Migration Plan

No database migration is needed. Regenerate personal-statistics exports after implementation. Existing exports remain valid because the new field is optional to the renderer. Rollback consists of reverting the summary field and UI rendering changes.

## Open Questions

None.

## Context

The exporter already groups imported throw timelines into player rounds, marks rounds complete or incomplete, retains C1x/C2 metrics per round, and aggregates C1x/C2 totals from complete rounds only. The Personal stats page renders the global putting summary from each player's `PuttingSummary` and renders round entries separately.

The new metrics are global derived statistics. They must use the same complete-round boundary as existing global totals without changing the existing C1x/C2 classification or adding fields to round entries.

## Goals / Non-Goals

**Goals:**

- Classify successful C2+ attempts using the effective drop-zone/zone and effective distance.
- Calculate normalized C2+ average and maximum values from complete rounds.
- Select the best complete C1x round using exact percentage and total-putt tie-breaking.
- Export stable real/display/tie fields needed by the UI.
- Show the new values in the global putting summary only.

**Non-Goals:**

- Changing existing C1x or C2 attempt classification.
- Adding C2+ fields to per-round summaries.
- Including incomplete rounds in any new global metric.
- Adding filters, charts, database columns, or API endpoints.

## Decisions

### Derive metrics from completed round builders

Keep per-round C1x/C2 output unchanged and extend `PuttingRoundBuilder` with internal C2+ and hole-count data. After `finish()`, the player builder will consider only complete rounds when constructing global metrics. This preserves the current completeness behavior and prevents incomplete valid-hole attempts from affecting global values.

### Use an independent C2+ predicate

C2+ is broader than the current C2 bucket. A successful attempt qualifies when its effective zone is not zone 3 and its effective distance is null or at least 33 feet. Effective values follow existing precedence: `drop_zone_id` overrides `zone_id`, and `distance_to_target` overrides `drop_distance_to_target` when present. The predicate is intentionally separate so distances above the existing C2 maximum are not discarded.

### Normalize each round before averaging

For a complete round, calculate `weightedC2Plus = successfulC2Plus * 18.0 / actualHoleCount`. The average is the arithmetic mean of these weighted round values, with two-decimal display rounding. The maximum uses the same weighted values, but the export retains the exact value alongside the rounded display value. Comparisons should use an exact or sufficiently stable representation of `successfulC2Plus * 18 / holeCount` so equal real values tie reliably.

The actual hole count comes from the round's expected layout hole count, which is already required for a complete round. A non-positive hole count cannot produce a complete contributing round.

### Represent best C1x as a global summary object

For complete rounds with at least one C1x attempt, compare the exact ratio `successful / total`, then compare total C1x attempts. Export the winning percentage, successful count, total count, and number of rounds sharing both winning values. This makes the tie rule explicit and gives the UI enough information to render the result without reconstructing rounds.

### Preserve compatibility for older exports

The static page will tolerate missing new fields and render existing C1x/C2 totals. New lines are rendered only when their corresponding global metric is available; no round-detail code will read or display new per-round fields.

## Risks / Trade-offs

- [Existing generated JSON files are numerous and currently dirty in the worktree] → Do not modify them during proposal creation; implementation should regenerate them only as an intentional follow-up.
- [Floating-point equality could split mathematically equal weighted maxima] → Compare normalized fractions using integer cross-products or a canonical exact key, while exporting decimal real/display values.
- [Null distances and non-zone-3 records can include attempts outside the existing C2 range] → Keep C2+ classification independent from `PuttingZone.C2` and test null, 33-foot, and above-65-foot cases.
- [Older player files lack the new fields] → Keep UI defaults backward-compatible and avoid treating missing metrics as zero-valued achievements.

## Migration Plan

No database migration is required. Implement the exporter and UI changes, run focused tests, regenerate the personal statistics export, and verify representative JSON plus browser rendering. Rollback consists of reverting the exporter/UI changes and regenerating the previous export format.

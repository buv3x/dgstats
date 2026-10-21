## Why

Personal putting round details currently show only C1x and C2 metrics, even though the exporter already calculates lost throws caused by three-plus-putt holes. Exposing that value makes round-level putting mistakes visible and helps users identify the rounds contributing to their aggregate three-plus-putt rate.

## What Changes

- Add the per-round three-plus-putt lost-throw count to exported putting round summaries.
- Display the metric as `3+putts` after the C2 metric, separated by a comma.
- Show `3+putts` only for rounds whose count is greater than zero.
- Include rounds with three-plus-putts in round-detail visibility even when their C1x and C2 totals are zero.
- Preserve existing incomplete-round styling and compatibility with older exports that lack the new field.
- Add exporter and browser-rendering coverage for the new field and conditional display.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `personal-basket-statistics`: extend round summaries and round-detail presentation with an optional `3+putts` count.
- `putting-statistics`: expose the already-calculated per-round three-plus-putt lost-throw count in round summaries.
- `static-basket-statistics-page`: render the optional `3+putts` value conditionally in round-detail lines.

## Impact

- `BasketStatisticsExportService` will add the existing round-builder count to `PuttingRoundSummary`.
- `docs/app.js` will render the optional field after C2 and include qualifying rounds in the details list.
- Personal-statistics JSON exports will gain an optional round-level field; no API, database, or leaderboard changes are required.

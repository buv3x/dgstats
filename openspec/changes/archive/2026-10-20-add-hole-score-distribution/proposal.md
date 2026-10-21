## Why

Hole stats currently show how scoring separation changes around a rating, but do not show which score outcomes make up that result. A weighted score-distribution chart will make rating-dependent scoring patterns visible while using the same sliding-window model and confidence cues as SPRW.

## What Changes

- Add a `Score Distribution` chart below SPRW in the Hole stats view, using the same chart size and rating midpoints as SPRW.
- Calculate weighted percentages for score buckets `1-2`, `3`, `4`, `5`, `6`, `7`, and `8+` at every rating midpoint with a 5-point gap.
- Use triangular SPRW weights over the same rating range when calculating each bucket percentage.
- Render one toggleable line per bucket that has at least one score anywhere in the selected hole's full sample set.
- Keep bucket colors consistent across all holes and default-enable only buckets whose overall percentage exceeds 5%.
- Reuse SPRW confidence-based point and line styling based on weighted sample count.
- Add the header tooltip text `Score distribution around a given rating`.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `static-basket-statistics-page`: Add the Hole stats Score Distribution chart, weighted bucket calculations, line toggles, colors, defaults, tooltip, and rendering behavior.

## Impact

- Updates the static Hole stats UI in `docs/index.html`, `docs/app.js`, and `docs/styles.css`.
- Extends the exported Hole stats JSON produced by `BasketStatisticsExportService` with weighted score-distribution data.
- Requires updates to the static-page specification and focused export/UI verification.
- Existing SPRW data and behavior remain compatible; regenerated files under `docs/data/basket-stats/` will contain the additional distribution fields.

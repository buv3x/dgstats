## Context

Basket Stats currently exports precomputed sliding-window statistics in `docs/data/basket-stats/*.json`. Each window uses a 50-rating-point inclusive range on a shared 5-point grid, exports the midpoint, raw sample count, count bucket, SPR, and VAR, and the static page renders only the SPR series.

SPR2 is intended to be an additional local slope metric for the same chart. It should preserve the current SPR calculation while adding a broader 100-rating-point source range centered on each displayed midpoint, where samples near the midpoint matter most.

## Goals / Non-Goals

**Goals:**

- Export SPR2 for every Basket Stats window that remains eligible for the existing SPR series.
- Calculate SPR2 with a triangular linear weight function centered on the window midpoint.
- Export SPR2 weighted count and bucket values so SPR2 line connectivity reflects its own effective sample strength.
- Render SPR and SPR2 together in the Basket Stats chart with compact metric-specific tooltips.

**Non-Goals:**

- Change Course Stats SPR/VAR scatter chart behavior.
- Replace or recalculate existing SPR semantics.
- Reintroduce VAR into the Basket Stats chart.
- Add runtime Basket Stats regression calculation in the browser.

## Decisions

### Keep SPR as the chart grid driver

Current Basket Stats windows are generated from the existing 50-point SPR grid. SPR2 will be computed for the same exported `ratingMidpoint` values rather than generating a separate set of SPR2-only windows.

This keeps the chart easy to compare:

```text
ratingMidpoint:       800
SPR sample range:     775..825
SPR2 sample range:    750..850
SPR2 weights:         0.0 -> 1.0 -> 0.0
```

Alternative considered: generate an independent 100-point SPR2 grid. That would create mismatched X positions and make the side-by-side chart harder to read.

### Use triangular weighted linear regression for SPR2

For midpoint `m` and sample rating `r`, SPR2 includes samples where `abs(r - m) <= 50` and uses:

```text
weight = max(0, 1 - abs(r - m) / 50)
```

Weighted regression uses weighted means and weighted covariance/variance:

```text
meanRating = sum(weight * rating) / sum(weight)
meanScore  = sum(weight * score) / sum(weight)
slope      = sum(weight * ratingDelta * scoreDelta)
           / sum(weight * ratingDelta * ratingDelta)
spr2       = -100 * slope
```

Samples exactly at midpoint +/- 50 have weight `0.0`. They may pass the inclusive range predicate, but they do not affect the regression or weighted count.

### Export weighted count as SPR2 effective count

SPR2 count will be the sum of sample weights over the 100-point weighted window. This count may be fractional. SPR2 count bucket uses the existing thresholds against the weighted count:

```text
50 <= spr2Count < 100  -> 50-99
100 <= spr2Count < 200 -> 100-199
200 <= spr2Count       -> 200+
```

SPR2 should be omitted for a window when the weighted count is below `50` or weighted rating variance is zero. Existing SPR window eligibility remains unchanged.

### Extend the exported window schema

Each Basket Stats window will keep all existing fields and add:

```json
{
  "spr2": 0.68,
  "spr2Count": 91.5,
  "spr2CountBucket": "50-99"
}
```

This is a backward-compatible schema extension for consumers that ignore unknown fields.

### Render SPR2 as a peer line, not a separate chart

The static page will draw SPR and SPR2 on the same Basket Stats chart using the existing SPR Y-axis domain. Each series should use its own count bucket when deciding whether to connect points and whether a segment is dotted or solid.

## Risks / Trade-offs

- Existing exported files lack SPR2 fields -> regenerate `docs/data` after implementation so the deployed static page can draw SPR2.
- Weighted count is fractional while current count is integer -> format internals consistently and only use bucket labels for line styling; tooltip can stay focused on metric/rating/value.
- SPR2 can be unavailable for a window where SPR exists -> render only available SPR2 points/segments while preserving SPR display.
- Additional line may clutter the chart -> use distinct but restrained styling and keep tooltip rows compact.

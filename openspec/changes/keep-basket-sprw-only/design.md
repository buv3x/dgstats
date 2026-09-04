## Context

Basket Stats currently has a transitional shape. The Java export computes raw sliding-window SPR and VAR, then adds optional weighted `spr2`, `spr2Count`, and `spr2CountBucket` fields. The static Basket Stats chart draws both raw SPR and SPR2, while Course Stats still independently calculates and displays its SPR/VAR scatter chart from course sample data.

The desired end state is narrower: Basket Stats should keep only the weighted slope metric and present it as `SPRW`. Basket Stats exported files should no longer expose raw SPR, VAR, or raw count-bucket fields. Course Stats SPR/VAR is a separate view and remains unchanged.

## Goals / Non-Goals

**Goals:**

- Make SPRW the sole exported Basket Stats sliding-window metric.
- Rename the weighted metric fields from `spr2*` to `sprw*`.
- Remove raw Basket Stats `spr`, `var`, and `countBucket` fields from generated basket stats JSON.
- Render a single Basket Stats line/point series labeled `SPRW`.
- Use `sprwCountBucket` for Basket Stats line connectivity and line pattern.
- Regenerate `docs/data/basket-stats/*.json` to match the new contract.

**Non-Goals:**

- Change Course Stats SPR/VAR scatter chart calculations, labels, or tooltip behavior.
- Change Basket Stats window grid size, grid step, weighted formula, or minimum weighted count threshold.
- Remove raw sample `count` from Basket Stats windows.
- Add browser-side regression calculation for Basket Stats.

## Decisions

### Treat SPRW as the canonical export field

The export should emit `sprw`, `sprwCount`, and `sprwCountBucket` directly instead of emitting `spr2*` and relabeling it in the browser.

```json
{
  "ratingFrom": 875,
  "ratingTo": 925,
  "ratingMidpoint": 900,
  "count": 138,
  "sprw": 0.68,
  "sprwCount": 91.5,
  "sprwCountBucket": "50-99"
}
```

Alternative considered: keep `spr2*` in JSON and only relabel the UI. That would keep generated data tied to an obsolete metric name and would not satisfy the request to remove/rename the metric from export.

### Keep the current weighted calculation

SPRW should use the existing SPR2 math: a 100-rating-point range centered on `ratingMidpoint`, triangular linear weights, weighted regression, and weighted count threshold of `50`.

```text
weight = max(0, 1 - abs(rating - midpoint) / 50)
sprw   = -100 * weighted regression slope
```

Alternative considered: recalculate SPRW from the old raw 50-point SPR window. That would change the intended weighted metric semantics rather than just promoting SPR2 to the canonical Basket Stats metric.

### Keep raw count but remove raw count bucket

Basket Stats windows should continue to export `count`, the number of raw samples in the original 50-point window. It is useful context for diagnostics and existing eligibility logic. However, `countBucket` should be removed because chart connectivity will be based only on the weighted effective count bucket.

Alternative considered: remove `count` as well. That would make exported windows less inspectable and is not necessary to remove raw SPR chart behavior.

### Keep Basket Stats and Course Stats separated

Only Basket Stats export files and Basket Stats chart rendering should change. Course Stats continues to calculate raw SPR/VAR in `docs/app.js` from selected course samples and does not consume Basket Stats window fields.

Alternative considered: globally rename SPR labels. That would incorrectly affect Course Stats, where raw SPR remains meaningful and visible.

## Risks / Trade-offs

- Existing generated Basket Stats JSON becomes stale -> Regenerate `docs/data/basket-stats/*.json` after code changes and verify no `spr`, `spr2`, `var`, `countBucket`, `spr2Count`, or `spr2CountBucket` fields remain in Basket Stats files.
- Consumers relying on old Basket Stats fields will break -> Treat this as an intentional breaking change scoped to Basket Stats window JSON.
- Main static spec contains older Basket Stats requirements from previous changes -> Delta specs should explicitly replace Basket Stats chart/export behavior while preserving Course Stats SPR/VAR requirements.
- Single-series chart may leave unused CSS -> Remove or repurpose obsolete Basket Stats SPR/SPR2 styles during implementation to avoid misleading dead code.

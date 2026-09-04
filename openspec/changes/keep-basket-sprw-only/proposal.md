## Why

Basket Stats now has two sliding-window slope metrics, but the weighted SPR2 metric is the one intended for continued use. Keeping raw SPR, VAR, and raw count-bucket data in the Basket Stats chart/export creates an unnecessary contract surface and makes the UI less direct.

## What Changes

- **BREAKING**: Replace Basket Stats exported window metric fields `spr`, `var`, `spr2`, `spr2Count`, `spr2CountBucket`, and `countBucket` with the weighted metric fields `sprw`, `sprwCount`, and `sprwCountBucket`.
- Rename the Basket Stats weighted metric label from `SPR2` to `SPRW` everywhere it is visible or accessible.
- Render only the SPRW series in the Basket Stats chart.
- Style SPRW chart connectivity using `sprwCountBucket`.
- Preserve raw sample `count` in Basket Stats windows for eligibility/debugging context, but do not export raw `countBucket`.
- Preserve Course Stats SPR/VAR chart behavior and course-level statistics exports.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `basket-statistics-export`: Basket sliding-window exports expose only the weighted SPRW metric and omit raw SPR, VAR, and raw count bucket fields.
- `static-basket-statistics-page`: Basket Stats chart renders and labels only the SPRW sliding-window metric while Course Stats SPR/VAR behavior remains unchanged.

## Impact

- `src/main/java/com/datascience/service/BasketStatisticsExportService.java`: update Basket Stats window schema, weighted metric field names, and exported fields.
- `docs/app.js`: draw only SPRW series, points, legend, axis label, and tooltip rows for Basket Stats.
- `docs/index.html`: update Basket Stats chart title and accessible label.
- `docs/styles.css`: remove or repurpose raw SPR-specific Basket Stats styling as needed for one SPRW series.
- `docs/data/basket-stats/*.json`: regenerate exported Basket Stats files with the new field names and removed fields.
- Specs and verification should cover the breaking Basket Stats export shape, SPRW labeling, and that Course Stats SPR/VAR remains unaffected.

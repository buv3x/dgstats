## 1. Export Contract

- [x] 1.1 Update `BasketStatisticsExportService` so Basket Stats windows emit `sprw`, `sprwCount`, and `sprwCountBucket` instead of `spr2`, `spr2Count`, and `spr2CountBucket`.
- [x] 1.2 Remove raw Basket Stats window exports for `spr`, `var`, and `countBucket` while preserving `ratingFrom`, `ratingTo`, `ratingMidpoint`, and raw `count`.
- [x] 1.3 Keep the existing weighted SPR2 calculation semantics for SPRW, including triangular weights, minimum weighted count of 50, and weighted rating variance eligibility.
- [x] 1.4 Ensure Basket Stats variations/windows are omitted when no eligible SPRW window remains.

## 2. Static Basket Stats Chart

- [x] 2.1 Update `docs/app.js` to read `sprw`, `sprwCount`, and `sprwCountBucket` for Basket Stats chart rendering.
- [x] 2.2 Remove raw SPR and SPR2 Basket Stats chart series, points, legend entries, and tooltip labels.
- [x] 2.3 Update Basket Stats chart title, accessible label, Y-axis label, legend, and tooltip text to use `SPRW`.
- [x] 2.4 Update Basket Stats line connectivity and line pattern to use only `sprwCountBucket`.
- [x] 2.5 Clean up obsolete Basket Stats SPR/SPR2 CSS selectors or repurpose them into a single SPRW style.

## 3. Generated Data

- [x] 3.1 Regenerate static basket statistics data so `docs/data/basket-stats/*.json` uses the SPRW-only window schema.
- [x] 3.2 Inspect generated basket stats JSON to confirm `sprw`, `sprwCount`, and `sprwCountBucket` are present for eligible windows.
- [x] 3.3 Inspect generated basket stats JSON to confirm `spr`, `spr2`, `var`, `countBucket`, `spr2Count`, and `spr2CountBucket` are absent from Basket Stats windows.

## 4. Verification

- [x] 4.1 Compile the project to confirm the Java export changes build.
- [x] 4.2 Inspect static chart code paths to confirm Course Stats SPR/VAR behavior remains unchanged.
- [x] 4.3 Inspect Basket Stats chart code paths to confirm only SPRW is rendered and labeled.

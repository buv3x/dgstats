## Why

Basket Stats currently shows only SPR, which uses an equally weighted 50-rating-point regression window. A second weighted metric can make the trend less sensitive to scores near the edge of the window while still using a broader local sample around each rating.

## What Changes

- Add SPR2 to exported Basket stats windows as a weighted regression metric.
- Calculate SPR2 over a 100-rating-point range centered on the exported window midpoint, with linearly decreasing weights from `1.0` at the midpoint to `0.0` at midpoint +/- 50.
- Export an SPR2 weighted count and count bucket using the same `50-99`, `100-199`, and `200+` bucket rules as current SPR count buckets.
- Display SPR2 on the Basket Stats chart alongside existing SPR.
- Style SPR2 connectivity independently from SPR using SPR2 weighted count buckets.
- Preserve Course Stats SPR/VAR behavior and the existing SPR calculation.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `basket-statistics-export`: Basket sliding-window exports include SPR2 weighted-regression values and weighted count buckets.
- `static-basket-statistics-page`: Basket Stats chart renders SPR and SPR2 together and uses each metric's own count bucket for line styling.

## Impact

- `src/main/java/com/datascience/service/BasketStatisticsExportService.java`: add weighted regression/count calculation and extend `BasketStatsWindow`.
- `docs/app.js`: render an additional Basket Stats series, points, legend, and tooltip metric.
- `docs/styles.css`: add visual styling for SPR2 line segments and points.
- `docs/index.html`: update Basket Stats chart title and accessible label.
- `docs/data/basket-stats/*.json`: regenerated export files will include SPR2 fields.
- Tests or focused verification should cover weighted regression math, export shape, and static chart rendering behavior.

## Why

The course-level basket statistics page shows aggregate behavior, but it does not let users inspect how one basket variation's SPR and VAR change across player rating bands. A basket-focused sliding-window chart will bring the spreadsheet's per-variation rating-range analysis into the static display.

## What Changes

- Add a top navigation menu with `Course stats` and `Basket stats`.
- Keep the current course-level display under `Course stats`.
- Add a new `Basket stats` view where the user selects a basket course, then a basket variation.
- Export precomputed sliding-window basket variation statistics under `docs/data/basket-stats/`.
- Add each course's basket stats file path to the statistics manifest.
- For every basket variation, calculate 50-rating-point sliding windows with a 5-point step over a shared rating grid.
- Use the rating window midpoint as the chart's X-axis label.
- Include only windows with at least 50 scores.
- For each included window, calculate SPR and VAR using the same definitions as the course-level SPR/VAR chart.
- Display SPR and VAR as two differently colored lines on a dual-axis line chart.
- Use a fixed left Y-axis for SPR from `-0.5` to `2.0`.
- Use a fixed right Y-axis for VAR from `0.0` to `1.5`.
- Style line segments by sample-count bucket: `50-99` dotted, `100-199` normal, and `200+` bold.
- When the count bucket changes between adjacent points, change line style at the midpoint between those points.
- Do not add a user-facing rating filter to `Basket stats`; the full precomputed rating-window series is shown.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `basket-statistics-export`: Add precomputed per-course basket variation sliding-window statistics files and manifest references.
- `static-basket-statistics-page`: Add a `Basket stats` view with course/variation selectors and a dual-axis SPR/VAR sliding-window chart.

## Impact

- Affects `BasketStatisticsExportService` records, calculation logic, file writing, diagnostics, and generated data under `docs/data`.
- Affects the export admin result display if generated file counts or diagnostics are expanded.
- Affects `docs/data/statistics.json` and adds `docs/data/basket-stats/{courseId}.json`.
- Affects `docs/index.html`, `docs/app.js`, and `docs/styles.css` to add tab navigation, basket stats selectors, data loading, and chart rendering.
- Does not affect database schema, PDGA import behavior, basket mapping behavior, or basket course administration behavior.

## Context

The current static basket statistics page is a GitHub Pages-ready frontend backed by JSON files exported by the Spring application. The manifest at `docs/data/statistics.json` lists basket courses, and each course has raw mapped score samples under `docs/data/courses/{courseId}.json`. The existing static page displays course-level aggregate statistics and an SPR/VAR scatter chart from those raw samples.

The new Basket stats view analyzes one basket variation at a time across fixed sliding rating windows. Unlike the existing Course stats rating filter, this analysis always uses the full exported rating range and fixed window rules, so the export can precompute the window series.

## Goals / Non-Goals

**Goals:**

- Add top-level navigation with `Course stats` and `Basket stats`.
- Preserve the current display under `Course stats`.
- Add a `Basket stats` view with course selection, basket variation selection, and a line chart.
- Export one basket stats file per basket course under `docs/data/basket-stats/{courseId}.json`.
- Add each course's basket stats file path to the manifest.
- Precompute 50-rating-point windows using a 5-point step over a shared rating grid.
- Use window midpoint as the X-axis value.
- Include only windows with at least 50 score samples.
- Calculate SPR and VAR per window using the existing regression definitions.
- Render SPR and VAR as separate colored lines with two fixed Y axes.
- Style line segments by sample-count bucket, with style changes at the midpoint between adjacent points when buckets differ.

**Non-Goals:**

- No database schema changes.
- No new PDGA import behavior.
- No basket course or mapping administration changes.
- No user-facing rating filter in `Basket stats`.
- No chart zooming or panning.
- No player-identifying data in exported basket stats files.

## Decisions

1. Precompute Basket stats during export.

   The Basket stats chart uses fixed window parameters and always covers the full available rating range. Precomputing avoids repeating regression work in the browser and keeps the static page responsible for loading and rendering a small selected series.

   Alternative considered: calculate windows client-side from existing course samples. That would avoid new export files, but it would duplicate heavier regression/window logic in JavaScript and require loading raw course samples even when the user only wants a single variation chart.

2. Add per-course basket stats files instead of per-variation files.

   Store all eligible variation window series for a course in `docs/data/basket-stats/{courseId}.json`. This keeps selection to two requests: manifest, then selected course's basket stats. It also avoids many small files for every basket variation.

   Alternative considered: write `docs/data/basket-stats/{courseId}/{variationId}.json`. That minimizes the selected variation payload but creates many files and makes export cleanup/reporting noisier.

3. Add `basketStatsPath` to each manifest course option.

   The manifest remains the single entrypoint for static data discovery. Course stats can keep using `path`, while Basket stats loads `basketStatsPath`.

   Alternative considered: infer the basket stats path from the course id. That would work, but it hides the published contract and makes future sharding/renaming harder.

4. Generate windows over a shared 5-point rating grid.

   For starts `0, 5, 10, ...`, each window is `[start, start + 50]`, and its label is `start + 25`. The export only retains windows with at least 50 scores. A shared grid makes window labels comparable across courses and variations.

   Alternative considered: start from each variation's minimum rating rounded locally. That may produce fewer empty scans, but it makes window labels less globally consistent.

5. Use the same SPR and VAR definitions as the course-level scatter chart.

   For each retained window, fit ordinary least-squares regression for score over rating. SPR is `-100 * slope`. VAR is the average absolute residual from the fitted line.

   Alternative considered: reuse average score or squared residual metrics. Those do not match the spreadsheet definition and would make the new chart inconsistent with the existing SPR/VAR chart.

6. Use two fixed Y axes.

   SPR uses the left Y-axis fixed from `-0.5` to `2.0`. VAR uses the right Y-axis fixed from `0.0` to `1.5`. Separate axes keep both metrics readable while fixed domains preserve comparability.

   Alternative considered: one shared Y-axis. That is visually simpler, but SPR and VAR occupy different natural ranges and would make one series harder to read.

7. Encode sample-count bucket as line segment style.

   Each window has a count bucket: `50-99` dotted, `100-199` normal, `200+` bold. When adjacent windows have different buckets, split the connecting line at the midpoint between their X positions and style each half by its endpoint's bucket.

   Alternative considered: style the whole segment by the lower bucket. That is conservative, but it does not match the spreadsheet-like behavior where style can change between points.

## Risks / Trade-offs

- Export payload growth -> Keep Basket stats in separate per-course files and list only the selected course's file in the manifest.
- Stale basket stats files after export -> Treat the manifest as authoritative and consider cleaning `docs/data/basket-stats` before writing current files during implementation.
- Regression code duplicated between Java export and JavaScript chart -> Prefer a small shared conceptual helper in Java export for Basket stats and keep JavaScript chart rendering separate.
- Dual-axis charts can be misread -> Use clear axis labels, distinct line colors, and tooltips showing exact SPR, VAR, count, and rating window.
- Sparse variations may disappear from Basket stats -> Omit variations with no windows meeting the 50-score threshold and show a clear empty state when no eligible variations exist for a course.
- Main specs may lag completed local changes -> Base this change on the currently implemented course-scoped export files and existing active `add-spr-var-chart` artifacts.

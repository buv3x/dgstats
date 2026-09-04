## Context

The static basket statistics page is a GitHub Pages-ready frontend made of `docs/index.html`, `docs/app.js`, and `docs/styles.css`. The currently implemented export shape is a manifest at `docs/data/statistics.json` plus course-scoped sample files under `docs/data/courses/`; each course file contains raw mapped score samples with basket, variation, rating, and score values.

The existing page already loads a selected basket course, applies open-ended rating bounds, and aggregates filtered samples into a basket/variation table. The SPR/VAR chart can reuse the same selected course snapshot and rating filter without changing the Java export path or generated JSON contract.

## Goals / Non-Goals

**Goals:**

- Render a scatter chart for the selected basket course using the current rating bounds.
- Calculate one point per basket variation from filtered score samples.
- Use ordinary least-squares linear regression to calculate score over player rating for each eligible variation.
- Display SPR on the horizontal axis as `-100 * slope`.
- Display VAR on the vertical axis as mean absolute residual from the regression line.
- Use fixed chart scales for visual comparison across basket courses: SPR `-0.25` to `1.75`, VAR `0.25` to `1.25`.
- Render off-scale points as small edge arrows that indicate the direction of the actual off-chart SPR/VAR value.
- Exclude chart groups with fewer than 10 filtered samples.
- Exclude chart groups where regression cannot be calculated because all included ratings are identical.
- Keep chart point and arrow labels in hover tooltips rather than persistent labels.
- Show one tooltip containing all points or arrows near the pointer when multiple rendered markers are close together.
- Provide a loading/calculating state for the chart.

**Non-Goals:**

- No export contract changes.
- No backend precomputation of regression or chart points.
- No database schema changes.
- No competition filter.
- No chart zooming, panning, or persistent point labels.

## Decisions

1. Calculate chart points client-side from filtered samples.

   Rating bounds are open-ended free numeric filters, so precomputing every possible window would either be impossible or would constrain the filter semantics. Reusing raw score samples keeps the chart consistent with the existing table.

   Alternative considered: export precomputed chart points. That would make rendering cheaper but would break open rating windows or require many generated variants.

2. Implement the chart as inline SVG in the existing static page.

   The current frontend has no build step and no charting dependency. SVG is enough for roughly dozens of basket variation points, axes, and pointer hit-testing while preserving GitHub Pages simplicity.

   Alternative considered: add a charting library. That would reduce some rendering code but introduce dependency, bundling, and versioning concerns for a small scatterplot.

3. Share filtering and grouping logic between the table and chart where practical.

   Both outputs use the same selected course snapshot and rating filter. The table needs score buckets while the chart needs full `[rating, score]` pairs, so the common boundary should be filtered samples and group identity rather than one table-specific aggregate structure.

   Alternative considered: derive chart points from table rows. That loses rating-level detail and cannot compute regression.

4. Use mean absolute residual for VAR after fitting the regression line.

   VAR is intentionally not mean squared error. For each sample, calculate `expectedScore = intercept + slope * rating`, then average `abs(score - expectedScore)` over the included samples.

   Alternative considered: standard deviation or root mean squared error. Those are common variability metrics but do not match the spreadsheet definition.

5. Use fixed chart domains matching the spreadsheet.

   The SPR axis should always render from `-0.25` to `1.75`, and the VAR axis should always render from `0.25` to `1.25`. Fixed scales make visual comparison between courses meaningful because the same chart position represents the same metric values everywhere.

   Alternative considered: derive domains from eligible point values. That maximizes use of space for a single course, but makes visual comparisons between courses misleading because the scale moves.

6. Clamp off-scale points to edge arrows.

   If a point's SPR or VAR is outside the fixed domain, keep its real metric values for sorting and tooltips, but render a small arrow at the nearest chart edge. Clamp the rendered coordinate to the plot rectangle, determine direction from which domain bounds were exceeded, and rotate/shape the arrow toward the actual off-chart direction. Points outside both axes should appear at the nearest corner with a diagonal direction.

   Alternative considered: hide off-scale points. That keeps the chart simple but loses potentially important outliers.

7. Use a pixel-radius hover hit-test that can return multiple rendered markers.

   The chart intentionally avoids point labels to reduce clutter. Pointer hover should scan rendered point positions and list every point inside a small screen-space radius so nearby/overlapping points remain discoverable.

   Alternative considered: show only the nearest point. That is simpler but hides details when several variations cluster together.

## Risks / Trade-offs

- Large course files could make recalculation feel slow -> Show a chart calculating/loading state and keep the algorithm linear over filtered samples plus groups.
- Fixed scales can place outliers beyond the visible plot -> Render edge arrows that preserve discoverability while keeping the shared scale.
- Arrow direction can be ambiguous at corners -> Use diagonal arrow orientation when both SPR and VAR are out of range, and keep exact values in the tooltip.
- Tooltips with many close points could become tall -> Limit formatting to compact point rows with basket, variation, count, SPR, and VAR.
- Locale-specific number formatting could make values inconsistent -> Use explicit decimal formatting for chart axis labels and tooltip metrics.
- Main specs are not fully synchronized with the already completed course-scoped export change -> Base implementation and this proposal on the current generated data contract inspected in `docs/data`.

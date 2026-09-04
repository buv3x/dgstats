## Why

The static basket statistics page currently shows table aggregates, but it does not visualize how scoring sensitivity to player rating relates to score variability for each basket variation. A course-level SPR/VAR scatter chart will make the bottom-chart analysis from the spreadsheet available directly from the exported static data.

## What Changes

- Add a course-level scatter chart to the static basket statistics page using the selected basket course and rating bounds.
- Compute one chart point per basket variation from filtered score samples using linear regression over `[player rating, hole score]`.
- Display SPR on the horizontal axis as `-100 * regression slope`, representing expected score difference per 100 rating points.
- Display VAR on the vertical axis as mean absolute distance between actual score and the regression-expected score.
- Use fixed spreadsheet-like chart scales for course comparison: SPR from `-0.25` to `1.75`, and VAR from `0.25` to `1.25`.
- Display off-scale chart points as small arrows on the nearest chart edge, pointing toward the point's actual off-chart direction.
- Omit basket variations with fewer than 10 filtered samples from the chart.
- Omit basket variations that cannot form a meaningful regression because they have no rating variance.
- Render chart points and edge arrows without persistent labels to reduce clutter, and show basket/variation details in hover tooltips.
- When a hover position is close to multiple points or arrows, show all matching point details in one tooltip.
- Show a chart loading/calculating state when selected course data is loading or chart values are being recalculated.
- Do not change the export contract; the chart uses the existing course-scoped score samples.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `static-basket-statistics-page`: Add an SPR/VAR scatter chart calculated from selected course samples and rating filters.

## Impact

- Affects `docs/index.html` to add the chart container alongside the existing controls/table.
- Affects `docs/app.js` to calculate regression statistics, render the fixed-scale scatter chart, clamp off-scale points to edge arrows, and handle hover tooltips.
- Affects `docs/styles.css` for chart layout, loading state, point and arrow styling, fixed axes, and tooltip presentation.
- Does not affect Spring export services, repository queries, database schema, or generated JSON shape.

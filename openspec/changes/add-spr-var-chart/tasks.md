## 1. Chart Structure and State

- [x] 1.1 Add an SPR/VAR chart section to `docs/index.html` with a chart container, status/message area, and tooltip element.
- [x] 1.2 Add chart styling to `docs/styles.css` for the SVG plot, axes, points, loading/calculating states, empty state, and tooltip.
- [x] 1.3 Wire chart rendering into the existing course loading and filter submit flow in `docs/app.js`.

## 2. Regression and Metric Calculation

- [x] 2.1 Reuse the selected course samples and rating bounds to build the filtered sample set used by both table and chart.
- [x] 2.2 Group filtered samples by basket and variation while preserving basket label, variation label, and sample count.
- [x] 2.3 Implement linear regression for score over rating for each group.
- [x] 2.4 Calculate SPR as `-100 * slope` for each eligible group.
- [x] 2.5 Calculate VAR as average absolute residual from the group's regression line.
- [x] 2.6 Omit chart groups with fewer than 10 samples or no rating variance.

## 3. SVG Rendering and Interaction

- [x] 3.1 Render axes and chart points with domains derived from eligible SPR/VAR values.
- [x] 3.2 Handle empty chart results with a no-chart-results message instead of an empty plot.
- [x] 3.3 Implement pointer hit-testing using rendered point positions and a pixel-radius threshold.
- [x] 3.4 Render tooltip details for one or more hovered points including basket label, variation label, count, SPR, and VAR.
- [x] 3.5 Hide the tooltip when the pointer leaves the chart or is not close to a point.

## 4. Verification

- [x] 4.1 Open the static page with generated `docs/data` files and confirm the chart renders for the selected course.
- [x] 4.2 Confirm rating filters update both the existing table and the SPR/VAR chart.
- [x] 4.3 Confirm groups with fewer than 10 filtered samples are absent from the chart.
- [x] 4.4 Confirm hovering clustered or nearby points shows all matching tooltip entries.
- [x] 4.5 Compile the project to confirm existing Spring resources and templates remain valid.

## 5. Fixed Scale and Edge Arrows

- [x] 5.1 Replace dynamic SPR/VAR chart domains with fixed domains: SPR `-0.25` to `1.75`, VAR `0.25` to `1.25`.
- [x] 5.2 Render in-range basket variations as regular chart points using the fixed domains.
- [x] 5.3 Render off-scale basket variations as small arrows clamped to the nearest chart edge.
- [x] 5.4 Orient edge arrows toward the actual off-chart SPR/VAR direction, including diagonal directions for corner outliers.
- [x] 5.5 Include edge arrows in the same hover hit-testing and multi-marker tooltip behavior as regular points.
- [x] 5.6 Verify fixed axes remain unchanged when switching rating filters and that off-scale arrows show exact SPR/VAR values in tooltips.

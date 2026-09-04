## Context

The static basket statistics page is a no-build GitHub Pages frontend implemented in `docs/index.html`, `docs/app.js`, and `docs/styles.css`. Course stats loads raw course score samples and computes aggregate rows plus an SPR/VAR scatter chart in the browser. Basket stats loads precomputed sliding-window data and currently renders SPR and VAR together on a dual-axis SVG chart.

The Course stats chart currently uses `CHART_MIN_SAMPLES = 10` after rating filtering. Basket stats export already uses 50 samples as the minimum retained sliding-window count, so the threshold change belongs to the Course stats chart calculation, not to the backend export window eligibility.

## Goals / Non-Goals

**Goals:**

- Require at least 50 matching score samples after the active Course stats rating filter before a basket variation appears in the Course stats SPR/VAR scatter chart.
- Keep Course stats as an SPR/VAR chart with VAR axis, values, edge arrows, and tooltips.
- Convert Basket stats chart rendering to SPR-only, removing all displayed VAR series, points, axis labels, legend entries, and tooltip rows.
- Keep Basket stats count-bucket connectivity semantics for the remaining SPR line and points.
- Keep existing static data files readable without requiring a backend data contract change.

**Non-Goals:**

- Removing `var` from exported basket stats JSON.
- Changing basket stats sliding-window size, step, or export eligibility.
- Changing the Course stats table threshold or filtering behavior.
- Adding new UI controls.

## Decisions

1. Apply the 50-result threshold after Course stats rating filtering.

   The existing Course stats chart is built from `filteredSamples`, so changing the chart threshold constant preserves the current mental model: rating bounds affect both which samples are considered and whether a variation has enough data to plot. Alternative considered: use exported lifetime sample counts regardless of rating bounds. That would make a point eligible even when the visible filtered subset is small, which conflicts with the requested "50 results after filtering" behavior.

2. Treat Basket stats as SPR-only rendering, not a backend data removal.

   The existing basket stats JSON may still contain `var` values, but the static page will ignore them for Basket stats rendering. This avoids unnecessary export contract churn and keeps older generated files compatible. Alternative considered: remove `var` from Java records and regenerated JSON. That would require broader export/spec changes without improving the requested chart behavior.

3. Collapse Basket stats chart infrastructure to one visible metric.

   The Basket stats chart should use the existing left SPR axis, rating X-axis, count-bucket line styling, point hit testing, and compact tooltips. VAR-specific code paths should be removed or bypassed: right axis, VAR line segments, VAR points, VAR legend item, and tooltip rows. Alternative considered: hide VAR with CSS only. That would leave invisible hit targets, stale accessible labels, and unnecessary chart logic.

4. Preserve Course stats VAR naming.

   The Course stats chart remains explicitly SPR/VAR because VAR still provides useful scatter-plot context there. Basket stats titles and accessible labels should be renamed so VAR does not appear in that view.

## Risks / Trade-offs

- Some Course stats charts may become empty under narrow rating filters -> Keep the existing no-chart-results message so the page communicates that no variation reached the 50-filtered-result threshold.
- Removing Basket stats VAR may leave unused CSS or helper constants -> Clean up only obviously unused VAR-specific Basket stats styles and constants while avoiding unrelated frontend refactors.
- Existing basket stats JSON still contains `var` -> This is intentional compatibility; implementation should document through tests/inspection that displayed Basket stats ignores it.
- Compact tooltips lose count and bucket detail in Basket stats -> This matches the existing refined tooltip direction and keeps hover focused on the SPR value.

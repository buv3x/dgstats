## Context

The static page now opens on the Description view, while Course stats data is still fetched and rendered during page initialization. `drawScatterPlot` obtains chart dimensions from `chartCanvas.getBoundingClientRect()`. Because the Course stats view is hidden at that moment, the measured width is zero and `chartMetrics` uses its minimum width fallback. Selecting Course stats later reveals a chart that is permanently sized from that hidden measurement.

Basket stats already follows the safe lifecycle: its data can load while hidden, but the chart is rendered when the Basket stats view becomes active. The Course stats lifecycle should use the same principle.

## Goals / Non-Goals

**Goals:**

- Ensure the first Course stats chart is measured only while its container is visible.
- Keep Course stats data loading independent from view activation.
- Handle both cases where data loads before the user opens Course stats and where data loads after the user opens it.
- Preserve existing chart calculations, filters, table rendering, loading messages, and subsequent recalculation behavior.

**Non-Goals:**

- Do not change chart domains, CSS dimensions, chart metrics, or statistical calculations.
- Do not change the default Description view or other tab behavior.
- Do not add a resize observer or general chart-layout framework for unrelated views.

## Decisions

### Gate Course stats rendering on view visibility

The course data-loading completion path will retain the selected snapshot but render Course stats only if the Course stats view is currently visible. Activating Course stats will render the retained snapshot after the view is shown. This mirrors the existing Basket stats activation path and ensures `getBoundingClientRect()` observes the real container width.

Alternative considered: call `drawScatterPlot` again after a fixed timeout or use `requestAnimationFrame` during initialization. Rejected because timing-based retries are less explicit and still couple rendering to hidden-view lifecycle timing.

### Keep data fetch and view activation separate

The selected Course stats snapshot remains available even when another tab is active. Switching to Course stats reuses that snapshot and recalculates the current table/chart state without issuing another data request.

Alternative considered: defer the entire Course stats fetch until the tab is opened. Rejected because it changes loading behavior unnecessarily and does not address the general rule that hidden views should not measure layout.

## Risks / Trade-offs

- [Course stats is selected while its data is still loading] → The completion callback checks visibility and renders once the snapshot arrives.
- [A user changes filters while Course stats is hidden] → Existing filter state remains intact; activation renders using the current controls and loaded snapshot.
- [Rendering on tab activation repeats inexpensive client-side calculations] → This is limited to the already loaded selected snapshot and is needed to obtain correct visible dimensions.


## Context

The static basket statistics page is a no-build GitHub Pages frontend implemented in `docs/index.html`, `docs/app.js`, and `docs/styles.css`. The Basket stats view renders a selected basket variation's precomputed sliding-window SPR and VAR series as an inline SVG dual-axis chart.

The Basket stats chart is currently loaded during page initialization while its parent view is hidden. Because chart sizing is based on `getBoundingClientRect()`, the first rendered chart can fall back to the minimum SVG width and appear small until the user changes the selected variation and triggers a redraw while the view is visible.

The chart also needs presentation refinements: count buckets should affect connectivity rather than line weight, tooltips should focus on the hovered metric value, and axes should use explicit spreadsheet-like tick sets.

## Goals / Non-Goals

**Goals:**

- Ensure the initially selected Basket stats variation renders at the visible chart width the first time the Basket stats tab is opened.
- Keep the current SVG/no-dependency chart implementation.
- Adjust count bucket styling so `50-99` windows are points only, `100-199` windows are dotted lines, and `200+` windows are normal solid lines.
- Use explicit Basket stats axis ticks and labels: rating every 20 points, SPR at `0`, `0.5`, `1`, and `1.5`, and VAR labels at `0.5` and `1`.
- Keep Basket stats tooltips compact by showing only rating and the hovered metric value.

**Non-Goals:**

- No changes to exported JSON data or Java export logic.
- No charting library or build pipeline.
- No changes to Course stats scatter chart behavior.
- No new chart zooming, panning, or filtering.

## Decisions

1. Redraw the Basket stats chart after the Basket stats view becomes visible.

   The existing chart measurement depends on the rendered canvas width. Re-rendering the selected variation when switching to the Basket stats view lets the chart use real layout dimensions while preserving eager data loading.

   Alternative considered: defer loading Basket stats data until the tab is opened. That also fixes the hidden-measurement issue, but it makes the first tab switch wait on network/file loading and changes the existing initialization flow more than necessary.

2. Keep point rendering independent from segment rendering.

   The chart already renders points separately from connecting segments. Treating `50-99` as "no connecting line" can be implemented in the segment path while leaving point drawing intact for all windows.

   Alternative considered: style `50-99` lines as nearly invisible. That keeps code simple but leaves hover/visual artifacts and does not truly make those windows unconnected dots.

3. Use explicit Basket stats tick helpers instead of the shared generic `ticks()` function.

   The Course stats scatter chart still benefits from generic fixed-domain ticks. Basket stats now has chart-specific tick rules, so rating, SPR, and VAR ticks should be passed or generated explicitly for the Basket stats grid.

   Alternative considered: change the shared `ticks()` function. That would risk unintended Course stats axis changes.

4. Preserve right-axis VAR labels without drawing VAR grid lines.

   SPR owns the horizontal grid lines on the left axis. VAR labels should remain visible on the right axis for value interpretation, but adding separate VAR grid lines would visually conflict with the SPR grid and imply a shared horizontal reference.

   Alternative considered: draw faint VAR grid lines. That adds clutter and weakens the distinction between the two Y axes.

## Risks / Trade-offs

- First-render fix may redraw the same chart more than once during initialization -> Keep redraw scoped to the selected Basket stats variation and only when data/windows are available.
- Skipping `50-99` line segments can make sparse stretches look discontinuous -> This is intentional and communicates low sample support directly.
- Explicit ticks can fall outside a narrow padded rating domain if generated carelessly -> Generate rating ticks from the next lower/upper multiples of 20 around the displayed rating domain.
- Compact tooltips remove count and bucket details -> Bucket confidence remains encoded visually, and the tooltip is now optimized for direct value lookup.

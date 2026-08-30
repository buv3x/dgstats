## Why

The Basket stats chart currently has a first-render sizing problem when it is initialized while hidden, and several chart details no longer match the intended spreadsheet-style presentation. These refinements make the chart stable on first display and make the axes, bucket styling, and tooltips easier to read.

## What Changes

- Fix the Basket stats chart so the initially selected variation renders at the correct full width when the Basket stats view is first opened.
- Change Basket stats count-bucket styling so `50-99` windows appear as unconnected dots, `100-199` windows use a dotted line, and `200+` windows use a normal solid line.
- Simplify Basket stats chart point tooltips to show only the hovered metric's rating and value.
- Render Basket stats rating X-axis labels and vertical grid lines every 20 rating points using whole-number labels.
- Render Basket stats SPR Y-axis labels and horizontal grid lines at `0`, `0.5`, `1`, and `1.5`.
- Render Basket stats VAR right-axis labels at `0.5` and `1` without associated grid lines.
- Rename the Basket stats X-axis label from `Rating midpoint` to `Rating`.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Refine Basket stats chart first-render behavior, bucket styling, axis ticks/grid lines, tooltip content, and X-axis label.

## Impact

- Affects the static frontend assets under `docs/`, primarily `docs/app.js` and `docs/styles.css`.
- No export JSON contract changes.
- No backend, database, dependency, or API changes.

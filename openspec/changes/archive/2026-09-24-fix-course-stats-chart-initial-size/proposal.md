## Why

After the Description tab became the default view, Course stats data is still loaded while the Course stats section is hidden. The first Course stats chart is therefore measured against a hidden container and rendered at the fallback minimum width instead of the page width.

## What Changes

- Render Course stats chart content only after the Course stats view is visible.
- Re-render the selected Course stats data when the Course stats tab is opened, using the now-visible chart container dimensions.
- Preserve the existing loading, filtering, table, and chart calculation behavior.
- Add regression coverage or focused verification for opening Course stats from the default Description tab and for data completing while Course stats is active.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Require the initially opened Course stats chart to use visible container dimensions, analogous to the existing Basket stats first-render sizing behavior.

## Impact

- Affects the static page lifecycle in `docs/app.js`.
- Updates the static statistics page specification and implementation tasks.
- No API, export format, calculation, dependency, or persisted-data changes.

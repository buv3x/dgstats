## Why

The static basket statistics page currently shows course-level SPR/VAR chart points for basket variations with as few as 10 filtered results, which can make low-sample points look more meaningful than they are. The Basket stats chart also duplicates VAR detail that is no longer wanted in that focused view.

## What Changes

- Raise the Course stats SPR/VAR scatter chart eligibility threshold from 10 to 50 matching score samples after the active rating filter is applied.
- Keep VAR in the Course stats SPR/VAR scatter chart, including the VAR axis, off-scale arrows, and tooltip values.
- Remove VAR from the Basket stats chart display entirely so the selected basket variation chart shows only SPR over rating.
- Update Basket stats chart labels, legend, points, hover behavior, and empty/loading states so they no longer mention or render VAR.
- Preserve exported basket stats data compatibility unless implementation work determines removing exported VAR is necessary.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `static-basket-statistics-page`: Update chart eligibility and Basket stats chart display requirements.

## Impact

- `docs/app.js`: Course stats chart point filtering threshold and Basket stats chart rendering, tooltip, and hover logic.
- `docs/index.html`: Basket stats chart title and accessible label text.
- `docs/styles.css`: Potential cleanup or adjustment for VAR-specific Basket stats styles.
- `openspec/specs/static-basket-statistics-page/spec.md`: Requirement deltas for chart threshold and SPR-only Basket stats display.
- No backend API or dependency changes are expected.

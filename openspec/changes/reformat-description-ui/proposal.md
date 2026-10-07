## Why

The Description view currently depends on a separately exported text asset and renders its headings as accordions. The content is stable page content, so embedding it in the page will simplify publishing and make the information immediately scannable while retaining dynamic statistics values from the export manifest.

## What Changes

- Move the Description navigation item to the end of the top-level menu.
- Keep Description as the default active view unless existing product behavior requires otherwise.
- Embed the reviewed Description content directly in `docs/index.html` as regular HTML.
- Render Description topics as ordinary headings and paragraphs instead of accordion sections.
- Replace only the three dynamic placeholders using metadata already present in `docs/data/statistics.json`.
- Remove the client-side fetch and parser for `docs/description.txt`.
- Stop copying/exporting `description/description.txt` to `docs/description.txt`.
- Preserve the existing unavailable fallback for missing manifest-backed placeholder values and keep the other statistics views unaffected.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Change Description navigation order, markup, rendering, and placeholder handling from an external accordion-rendered text asset to embedded regular HTML.
- `basket-statistics-export`: Stop publishing a separate Description asset while continuing to include the metadata required for Description placeholders in the statistics manifest.

## Impact

- Affects `docs/index.html`, `docs/app.js`, and `docs/styles.css`.
- Affects `BasketStatisticsExportService` and its export tests.
- Removes the generated `docs/description.txt` publishing step; no API, database, or dependency changes are expected.
- Existing uncommitted work in the repository must be preserved while applying the change.

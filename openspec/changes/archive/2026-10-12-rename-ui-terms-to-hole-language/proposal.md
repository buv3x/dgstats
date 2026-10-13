## Why

The public statistics page currently mixes “basket” terminology into labels that describe courses, holes, layouts, and SPRW. This makes the UI less consistent with the domain language users see elsewhere and obscures the meaning of several controls and table columns.

## What Changes

- Rename the browser tab title and global page header to `LT PDGA Statistics`.
- Rename the Course stats course filter to `Course`.
- Rename the Course stats table header from `Basket` to `Hole / Layout`.
- Replace `Basket` with `Hole` and `basket layout` with `hole layout` in Course stats table-header tooltip and accessible descriptions.
- Rename the `Basket stats` navigation tab to `Hole stats`.
- Rename the Hole stats filters to `Course` and `Hole Layout`.
- Rename the Hole stats chart heading from `Basket SPRW` to `SPRW`.
- Rename the Personal stats course filter to `Course`.
- Rename Personal stats table headers from `Basket course` to `Course` and `Basket` to `Hole`.
- Preserve existing element IDs, exported data fields, URLs, calculations, and behavior.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Update the public statistics page navigation, visible labels, table headings, and explanatory/accessibility tooltip wording.

## Impact

- Affects the static public UI in `docs/index.html`.
- Updates the existing static-page terminology requirements and their implementation tasks.
- No API, export format, data file, JavaScript calculation, selector behavior, or backend changes are required.
- Existing unrelated working-tree changes must remain untouched.

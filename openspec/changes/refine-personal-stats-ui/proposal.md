## Why

The public statistics page still shows an export timestamp that is now redundant because the Description view identifies the latest included competition. Its Personal stats table also applies browser-side basket ordering, which hides the intended best-to-worst rating order already produced by the export.

## What Changes

- Remove the `Exported at ...` metadata display from the static statistics page.
- Order Personal stats rows by their calculated rating from highest to lowest.
- Preserve deterministic course, basket, and variation ordering as tie-breakers after rating comparison.
- Keep the exported rating precision for sorting while displaying the existing rounded rating value.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: remove snapshot export-time display and update Personal stats row ordering.

## Impact

- Affects the static page assets under `docs/`, primarily `docs/index.html` and `docs/app.js`.
- No API, database, export format, or calculation changes are required.
- Existing Personal stats filters continue to operate on the same rows and preserve the new rating-based order.

## Why

The public statistics pages currently fall back to database IDs or alphabetical
ordering, so manually curated basket and variation order is not reflected in
the displayed statistics. Course selectors also use inconsistent ordering,
making the most data-rich courses harder to find.

## What Changes

- Map the nullable `sort_order` values for baskets and basket variations.
- Carry the ordering information, or an equivalent stable ordered representation,
  into the static export used by `docs/`.
- Use explicit basket and variation ordering in Course stats, Basket stats, and
  Personal stats wherever the value is present, with ID fallback and null values
  last.
- Sort every course select box on `docs/` pages by total exported course result
  count descending, without displaying the count in option labels.
- Keep the server-rendered administration UI and its course selectors unchanged.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `static-basket-statistics-page`: Define curated basket/variation ordering and
  result-count ordering for all public `docs/` selectors and displays.
- `basket-statistics-export`: Export the ordering metadata needed by the static
  page while preserving existing statistics and privacy fields.

## Impact

- Affects basket and basket-variation domain mappings and export projections.
- Affects JSON files under `docs/data/` and the static client in `docs/app.js`.
- May affect the static page HTML/CSS only where ordering or selector behavior is
  represented.
- Does not change database schema, calculation formulas, labels, or the
  server-rendered admin pages.

## Why

The page currently opens on the explanatory Description view, while the primary interactive experience is Course stats. The embedded Description is also out of date relative to the maintained `description/description.txt` source, so the default view and published explanation should be aligned with current usage and content.

## What Changes

- Make Course stats the default active view when the page opens.
- Show the Course stats view and hide Description in the initial HTML state before JavaScript initialization.
- Keep Description as the last navigation item.
- Replace the embedded Description content with the current content from `description/description.txt`.
- Preserve the three manifest-backed placeholders and their safe `unavailable` fallback.
- Preserve all existing Course stats, Basket stats, Personal stats, and Description navigation behavior after initialization.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Change the default view from Description to Course stats and update the embedded Description topics and content.

## Impact

- Affects `docs/index.html` and `docs/app.js`, with possible Description styling adjustments in `docs/styles.css`.
- No API, database, export-format, or dependency changes are expected.
- The maintained source remains `description/description.txt`; it is used to refresh the embedded static HTML, not fetched at runtime.

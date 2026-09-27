## Why

The public statistics page currently opens directly on Course stats and provides
no context about the dataset, terminology, or meaning of the visualizations.
A first-class Description tab will give visitors that context immediately and
will keep the displayed overview synchronized with the exported snapshot.

## What Changes

- Add a Description tab before Course stats, Basket stats, and Personal stats.
- Make the Description tab active by default when the page opens.
- Render the reviewed content from `description/description.txt` in the tab.
- Interpret the description's square-bracket placeholders from export metadata:
  competition count, included player count, and latest included competition.
- Interpret the supported inline `<italic>` and `<bold>` markers as formatting
  when rendering the description.
- Extend the statistics export manifest with the metadata needed by the
  description without requiring the client to scan all course files.
- Update `description/description.txt` for spelling, grammar, terminology, and
  style issues identified in `description/check_1.txt`.

## Capabilities

### New Capabilities

- `description-tab`: Provides a formatted, data-aware Description tab as the
  first and default view of the static statistics page.

### Modified Capabilities

- `basket-statistics-export`: Add manifest metadata required to resolve the
  Description tab's competition count, player count, and latest competition.

## Impact

- Affects `description/description.txt`, `docs/index.html`, `docs/app.js`, and
  `docs/styles.css`.
- Affects the statistics export manifest and its Java export service.
- Adds no database schema, external API, or dependency changes.
- Existing Course stats, Basket stats, and Personal stats behavior remains
  available, with only the initial active view and tab order changing.

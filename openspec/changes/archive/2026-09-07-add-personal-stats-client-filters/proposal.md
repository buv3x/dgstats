## Why

The Personal stats list can be long after a player is selected, making it hard to focus on a specific basket course or rows with enough personal attempts. The existing personal statistics export already includes course identity and count per row, so the page can add these filters without changing backend export behavior.

## What Changes

- Add Personal stats controls for optional basket course filtering and minimum personal result count filtering.
- Populate the Personal stats course selector from the selected player's loaded personal statistics rows only.
- Default the minimum count filter to `2` and prevent values lower than `2`.
- Apply both filters entirely in the static page client after the selected player's personal statistics file has loaded.
- Show an empty filtered-results message when the selected player has rows, but none match the active Personal stats filters.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `static-basket-statistics-page`: Adds client-side Personal stats course and minimum-count filters to the selected-player variation list.

## Impact

- Affects `docs/index.html`, `docs/app.js`, and likely `docs/styles.css`.
- No changes to generated JSON schema, backend services, repositories, or export calculations are expected.
- No new dependencies are expected.

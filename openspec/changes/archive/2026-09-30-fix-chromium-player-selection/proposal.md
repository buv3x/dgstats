## Why

The Personal stats player picker can leave Chrome and Edge showing the previous selection after a player is chosen from the native autocomplete list. The new value is processed only after an additional interaction with the input, while Firefox updates immediately, making the primary Personal stats workflow unreliable in Chromium-based browsers.

## What Changes

- Defer and coalesce Personal stats player-input processing until the browser has committed the selected autocomplete value.
- Keep the existing native player picker, player lookup, asynchronous statistics loading, and invalid/partial-input messaging unchanged.
- Ensure repeated `input` and `change` events do not trigger duplicate player-statistics requests.
- Verify mouse selection, keyboard selection, exact typing, rapid player changes, and behavior in Chrome, Edge, and Firefox.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: selecting a player from the Personal stats autocomplete control updates the displayed player statistics immediately and consistently across supported browsers.

## Impact

- Affects the static browser assets in `docs/app.js`, with possible focused updates to the Personal stats interaction requirement.
- No backend, database, export format, API, dependency, or generated statistics changes are required.
- The native `<datalist>` control remains in use; this proposal does not introduce a custom combobox.

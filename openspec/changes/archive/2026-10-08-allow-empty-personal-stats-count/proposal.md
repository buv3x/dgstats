## Why

The Personal Stats minimum-count field is currently repopulated with `2` as soon as the user deletes its value, because filtering normalizes the field during every input event. This prevents users from clearing the existing value and entering a replacement number naturally.

## What Changes

- Allow the minimum-count input to remain temporarily empty while the user edits it.
- Treat an empty minimum-count input as the default effective value of `2` when filtering Personal Stats rows.
- Normalize an empty, invalid, or below-minimum value to visible `2` when editing is committed.
- Add specification scenarios covering the editable empty state and its default filtering behavior.

## Capabilities

### New Capabilities

### Modified Capabilities

- `static-basket-statistics-page`: Clarify the Personal Stats minimum-count filter behavior while the input is being edited and when an empty value is applied.

## Impact

- Updates the Personal Stats client-side filtering and input-event handling in `docs/app.js`.
- Updates the static basket statistics page OpenSpec delta specification.
- No API, export format, or persisted data changes.

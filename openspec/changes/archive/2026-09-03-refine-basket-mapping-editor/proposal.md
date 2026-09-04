## Why

Basket variation mapping currently requires manual inspection to know which competitions already have mappings, and repeated division layouts require editing every division column even when all divisions used the same baskets. This change reduces mapping mistakes and speeds up common competition setup.

## What Changes

- Mark competitions that already have at least one basket variation mapping by prefixing their selector label with `* `.
- Add a `Same layout for all divisions` checkbox to the mapping editor near the competition controls.
- When same-layout mode is checked, keep only the first displayed division per round editable and show the remaining divisions as copied, display-only values.
- Persist same-layout saves by copying the first division's submitted basket mappings to corresponding baskets in the other divisions for that same round.
- Preserve existing per-division editing, round copy controls, validation, and atomic save behavior when same-layout mode is not checked.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `basket-variation-round-division-mapping`: Adds mapped competition indicators and same-layout division editing behavior to the existing basket variation mapping administration page.

## Impact

- Affects the basket variation mapping administration template, controller request handling, and mapping admin service model/save logic.
- Requires repository support for detecting whether a competition has any existing basket variation mapping.
- No external APIs or dependencies are added.

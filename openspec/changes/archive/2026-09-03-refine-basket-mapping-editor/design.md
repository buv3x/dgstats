## Context

The basket variation mapping editor currently loads competitions into a selector, loads a basket course selector after a competition is selected, and renders one mapping table per round. Each table uses round divisions as columns and basket ordinals as rows. The page already supports copying pending browser values between rounds by matching division identity and basket ordinal.

The requested refinements are local administration conveniences. They should not change the persisted data model, import behavior, or basket course maintenance behavior.

## Goals / Non-Goals

**Goals:**

- Show which competitions already have at least one persisted basket variation mapping.
- Add a same-layout editing mode that lets the user edit only the first displayed division per round.
- In same-layout mode, copy first-division values to corresponding baskets in the remaining divisions for that same round.
- Keep validation and saving transactional.
- Preserve the existing default per-division editor when same-layout mode is off.

**Non-Goals:**

- Persisting same-layout mode as a competition or basket-course setting.
- Changing the `basket_variation_round_division` schema.
- Changing PDGA import behavior.
- Changing how round copy controls match cells between rounds.

## Decisions

### Detect mapped competitions from persisted mappings

Competition options will include a boolean mapped flag. The flag will be true when any `basket_variation_round_division` record exists for a round division whose round belongs to that competition.

Alternative considered: infer mapped state from currently selected editor data. That would only work for the selected competition and would not support marking all options in the selector.

### Keep the marker in the option label

Mapped competitions will render as `* ` plus the existing competition label. This keeps the requested visual signal simple and works in a native HTML select.

Alternative considered: adding a separate badge next to the select. Native select options cannot contain rich markup, and an external legend would make scanning the dropdown slower.

### Treat same-layout as request-scoped editor mode

The checkbox will be submitted with GET control changes and POST saves, but the mode itself will not be stored. A selected competition can be edited normally or in same-layout mode on different visits.

Alternative considered: store a competition-level same-layout flag. That would add state and migration concerns without a clear need.

### Apply same-layout independently per round

For each round table, the first displayed division is the editable source. Remaining divisions in that same round are display-only mirrors. Saving expands the source division's submitted basket ordinal values to the other divisions in that same round when those divisions have the corresponding basket ordinal.

Alternative considered: use the first division in the first round as the source for all rounds. That would cross round boundaries and conflict with the existing UI model where each round table owns its own mapping values.

### Make save expansion server-authoritative

The browser may mirror first-division values into display-only columns for user feedback, but the server will be responsible for expanding same-layout submissions before persistence. This avoids relying on disabled controls being submitted and keeps no-JavaScript form submission coherent.

Alternative considered: implement only browser-side copying. Disabled inputs are not submitted, and client-only expansion is easier to bypass or break.

## Risks / Trade-offs

- First displayed division may not be the division the user expects as the source -> Use existing stable division ordering and make only the first displayed division editable so the source is visually explicit.
- Divisions may have different available basket ordinals -> Copy only ordinals that exist for the target division; leave non-corresponding target slots unchanged unless normal submitted cells specify otherwise.
- Existing non-source mappings can be overwritten in same-layout mode -> This is the point of the mode, so the UI should make display-only copied values clear before save.
- Repository mapped detection may introduce an extra query -> Use a single aggregate query returning mapped competition ids rather than checking each competition individually.

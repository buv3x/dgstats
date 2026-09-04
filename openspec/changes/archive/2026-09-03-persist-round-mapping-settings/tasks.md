## 1. Schema And Persistence

- [x] 1.1 Add a database changelog for `datas.basket_variation_round_settings` with `round_id`, `basket_course_id`, and `same_layout`.
- [x] 1.2 Add a JPA entity for the round mapping settings table with a one-to-one association to `Round` and a many-to-one association to `BasketCourse`.
- [x] 1.3 Add a Spring Data repository for loading settings by round and by a collection of rounds.

## 2. Editor Model Loading

- [x] 2.1 Replace editor-level selected basket course and same-layout state with per-round state in `MappingEditorModel` and `RoundTable`.
- [x] 2.2 Load saved round settings for all displayed rounds and default missing settings to the first available basket course with same-layout off.
- [x] 2.3 Load variation options per round based on each round's selected basket course.
- [x] 2.4 Update empty-state checks so mapping tables can render when each round has a valid selected basket course and variation options.

## 3. Save Behavior

- [x] 3.1 Update controller parameter parsing to collect submitted round settings using round-scoped field names.
- [x] 3.2 Save round settings and mapping cells in one transaction.
- [x] 3.3 Validate submitted round settings against the selected competition and existing basket courses before persisting any changes.
- [x] 3.4 Validate each submitted mapping value against the basket course selected for that mapping cell's round.
- [x] 3.5 Apply same-layout expansion independently for each submitted round whose same-layout value is enabled.

## 4. Template And Browser Copy

- [x] 4.1 Move the basket course selector and same-layout checkbox from the global controls into each round section.
- [x] 4.2 Render each round table's mapping selects using that round's variation options and same-layout state.
- [x] 4.3 Update hidden/input names and DOM metadata so each round submits its selected basket course and same-layout value.
- [x] 4.4 Update round-copy JavaScript to copy source round basket course, same-layout, and current matching mapping cell values into the target round without submitting.
- [x] 4.5 Ensure same-layout browser mirroring only affects cells within the round whose same-layout setting is enabled.

## 5. Verification

- [x] 5.1 Compile the project with Maven.
- [x] 5.2 Review the generated page markup paths for global control removal, per-round controls, and copy metadata consistency.

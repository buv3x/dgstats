## 1. Personal Stats Controls

- [x] 1.1 Add Personal stats course and minimum count controls to the existing personal filters form.
- [x] 1.2 Configure the minimum count input with default value `2` and minimum value `2`.
- [x] 1.3 Adjust Personal stats control styling so the added controls remain usable on desktop and mobile layouts.

## 2. Client-Side Filter Behavior

- [x] 2.1 Wire JavaScript references and change/input listeners for the new Personal stats controls.
- [x] 2.2 Rebuild the Personal stats course selector from the selected player's loaded `variations` rows after each successful player data load.
- [x] 2.3 Reset player-scoped course filtering when player selection is cleared or a different player is loaded.
- [x] 2.4 Normalize empty, invalid, or below-minimum count input values to `2`.
- [x] 2.5 Filter rendered Personal stats rows by selected `basketCourseId` and minimum `count` while preserving exported relative order.
- [x] 2.6 Show a filtered-empty message when a selected player has rows but none match the active filters.

## 3. Verification

- [x] 3.1 Check that Course stats and Basket stats behavior remains unchanged by the Personal stats filter changes.
- [x] 3.2 Run the project compilation check required for static page changes.

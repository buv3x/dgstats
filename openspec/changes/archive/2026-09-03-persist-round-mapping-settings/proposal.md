## Why

The basket variation mapping editor currently treats basket course selection and same-layout mode as page-level request state. Competitions can have rounds played on different manual basket courses or layouts, so these settings need to live at the round level and survive future editor visits.

## What Changes

- Add persistent round-level mapping settings for selected basket course and same-layout mode using a separate settings table.
- Move basket course selection and same-layout controls from the global editor area into each round table.
- Load each round with its saved settings, defaulting to the first available basket course and same-layout off when no setting exists.
- Save round settings together with mapping cells in the existing single save action.
- Update same-layout expansion so it is applied independently only to rounds whose saved/submitted same-layout setting is enabled.
- Update browser round-copy behavior so copying a round also copies the source round's current basket course and same-layout values into the target round.

## Capabilities

### New Capabilities
- `basket-mapping-round-settings`: Persistent local settings for basket variation mapping rounds, including selected basket course and same-layout mode.

### Modified Capabilities
- `basket-variation-round-division-mapping`: Basket course selection and same-layout mode move from editor-level behavior to per-round behavior.
- `basket-mapping-round-copy`: Round copy includes round-level course and same-layout settings as well as mapping cell values.

## Impact

- Adds a database changelog for a new `datas.basket_variation_round_settings` table.
- Adds a JPA entity and repository for round mapping settings.
- Updates `BasketVariationMappingAdminService` editor loading, validation, save expansion, and persistence.
- Updates `BasketVariationMappingAdminController` request parsing for per-round settings.
- Updates `basket-variation-mappings.html` controls and copy script.
- Verifies the change with project compilation after implementation.

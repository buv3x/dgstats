## Why

The mapping editor needs flexible basket course scoping without persisting editor preferences. Most competitions can use one course and same-layout mode at competition level, but mixed-course events need round-level or group-level course choices while keeping those choices as submitted form state.

## What Changes

- Move the default basket course selector and same-layout checkbox back to competition level.
- Add a competition-level `Course selection by round` checkbox, unchecked by default.
- When course selection by round is enabled, move basket course selection and same-layout controls to each round and hide round-copy controls.
- Add a round-level `Course selection by group` checkbox when course selection by round is enabled.
- When course selection by group is enabled for a round, move basket course selection to each division/group column header and force that round's same-layout mode off and unavailable.
- Validate submitted basket variation mappings against the basket course selected at the active scope: competition, round, or group.
- Remove the persisted round mapping settings behavior introduced by `persist-round-mapping-settings`.
- Add a forward Liquibase changeset that drops `datas.basket_variation_round_settings`, because that table has already been applied locally.

## Capabilities

### New Capabilities

### Modified Capabilities
- `basket-variation-round-division-mapping`: Basket course and same-layout controls become scope-dependent form state rather than saved round settings.
- `basket-mapping-round-copy`: Round copy is available only when course selection is competition-scoped.

## Impact

- Updates `BasketVariationMappingAdminService` editor loading, validation, and same-layout expansion around competition, round, and group course scopes.
- Updates `BasketVariationMappingAdminController` request parsing for the new scope fields.
- Updates `basket-variation-mappings.html` controls, table headers, option filtering, same-layout disabling, and copy-control visibility.
- Removes `BasketVariationRoundSettings` entity and repository usage.
- Adds a Liquibase changeset to drop the obsolete settings table.
- Verifies the change with project compilation after implementation.

## Why

The yearly putting export includes years where every represented round is incomplete, so players see accordions with no usable yearly statistics. Hiding those years makes the available-year list reflect years with at least one complete round while preserving global totals and the underlying exported evidence.

## What Changes

- Show a player's year accordion only when that year contains at least one round with `complete: true`.
- Continue showing complete rounds and incomplete rounds with recorded attempts inside an eligible year.
- Hide all year accordions when the player has no year containing a complete round.
- Preserve global putting totals and the exported year/round data unchanged.

## Capabilities

### New Capabilities

### Modified Capabilities

- `personal-basket-statistics`: Filter displayed year accordions by the presence of at least one complete round.

## Impact

- Update the Personal stats putting renderer and its regression coverage.
- No exporter, JSON schema, database, or API changes are required.

## Why

Mapping basket variations round by round is repetitive when later rounds use the same division and basket assignments as an earlier round. The existing mapping page already keeps edits unsaved until the user clicks "Save Mappings", so a local copy action can speed up data entry without changing persistence behavior.

## What Changes

- Add copy controls near each round label on the basket variation mapping page.
- For every displayed round, show a "Copy Round N" button for each other displayed round.
- Copy mappings from the selected source round into the target round's currently loaded form values without submitting or saving.
- Match copied cells by division identity and basket/hole ordinal rather than by table column position.
- Leave target cells unchanged when the source round does not have a matching division or basket/hole slot.
- Preserve the existing single "Save Mappings" action as the only persistence operation.

## Capabilities

### New Capabilities

- `basket-mapping-round-copy`: Unsaved UI copying of basket variation mapping values between round tables.

### Modified Capabilities

- None.

## Impact

- Updates the basket variation mapping read model if needed to expose stable copy metadata for rounds, divisions, and editable cells.
- Updates `basket-variation-mappings.html` to render copy controls, cell metadata, and client-side copy behavior.
- Does not add backend save endpoints, database changes, or persistence-side copy behavior.
- Existing validation and save behavior in `BasketVariationMappingAdminService` remains the source of truth when mappings are eventually submitted.

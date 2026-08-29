## 1. Read Model Metadata

- [x] 1.1 Inspect `BasketVariationMappingAdminService.RoundTable` and `DivisionColumn` to identify existing values available to Thymeleaf for round and division copy metadata.
- [x] 1.2 Add a stable division copy key to the mapping read model, preferring `competitionDivision.id` and falling back to `divisionCode` or label when needed.
- [x] 1.3 Ensure each rendered round table can expose a source/target round identifier and display label for copy controls.

## 2. Template Controls and Behavior

- [x] 2.1 Update `basket-variation-mappings.html` to render a copy-control area beside each round label.
- [x] 2.2 Render one `Copy Round N` button for every other round table, and no button when only one round is displayed.
- [x] 2.3 Add data attributes to editable mapping selects for round id, division copy key, and basket/hole ordinal.
- [x] 2.4 Add client-side copy behavior that copies current source select values into matching target selects without submitting the form.
- [x] 2.5 Ensure matching uses division copy key plus basket/hole ordinal, leaves unmatched target selects unchanged, and copies blank source values for matched cells.

## 3. Verification

- [x] 3.1 Compile the application successfully.
- [x] 3.2 Manually inspect the rendered Thymeleaf bindings for valid copy button and select metadata expressions.
- [x] 3.3 Manually verify the script does not change the existing form action, submit button, or backend save contract.

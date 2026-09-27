## 1. Domain and export ordering data

- [x] 1.1 Map nullable decimal `sort_order` fields on `Basket` and `BasketVariation` as nullable numeric values.
- [x] 1.2 Extend the statistics export projection to read basket and basket-variation sort orders without changing the database schema.
- [x] 1.3 Add ordering metadata to the relevant course samples, Basket stats variations, and Personal stats rows while preserving existing labels, calculations, and player-identity boundaries.
- [x] 1.4 Keep `CourseOption.sampleCount` as the total rated/mapped exported sample count used by public course selectors.

## 2. Public static UI ordering

- [x] 2.1 Add shared JavaScript comparators for nullable explicit sort order with null-last and ID fallback behavior.
- [x] 2.2 Update Course stats grouping and chart ordering to use basket and variation sort order instead of unconditional ID ordering.
- [x] 2.3 Update Basket stats variation selector and rendered data ordering to use basket and variation sort order.
- [x] 2.4 Update Personal stats course selector to use manifest course sample counts descending, with name and ID tie-breakers.
- [x] 2.5 Update all other public course selectors to use total result count descending while keeping counts out of visible labels.
- [x] 2.6 Update Personal stats row ordering to honor explicit basket and variation ordering while preserving existing fallback semantics.
- [x] 2.7 Keep the server-rendered administration UI and its course selectors unchanged.

## 3. Static export refresh and compatibility

- [x] 3.1 Regenerate or update the checked-in `docs/data/` export files with the new optional ordering fields.
- [x] 3.2 Confirm older export files without ordering fields fall back to ID/name ordering and remain loadable.
- [x] 3.3 Confirm course selector labels do not display the result count.

## 4. Verification

- [x] 4.1 Compile the Java application and confirm the export changes compile successfully.
- [x] 4.2 Run a JavaScript syntax check and inspect the static JSON shape for ordering fields and existing fields.
- [x] 4.3 Review public `docs/` selectors and displays for the required ordering scope and confirm no admin templates were changed.

## 1. Database and Domain Model

- [x] 1.1 Add a Liquibase changeset that creates `datas.basket_variation_round_division` with `round_division_id`, `hole_ordinal`, `basket_variation_id`, foreign keys, and a unique constraint on `(round_division_id, hole_ordinal)`.
- [x] 1.2 Decide whether to migrate existing non-null `hole_score.basket_variation_id` values or document that the column was unused, then include the chosen path in the changeset.
- [x] 1.3 Drop `datas.hole_score.basket_variation_id` in the same forward migration.
- [x] 1.4 Add a `BasketVariationRoundDivision` JPA entity mapped to the new table and relationships.
- [x] 1.5 Remove the `BasketVariation` relationship from `HoleScore`.
- [x] 1.6 Add a repository for `BasketVariationRoundDivision` with lookup methods for round divisions and hole ordinals.

## 2. Query Support and Read Models

- [x] 2.1 Add competition query support for listing competitions by `start_date` descending with null dates last if practical.
- [x] 2.2 Add round and round-division query support for loading a selected competition's rounds and divisions in stable display order.
- [x] 2.3 Add round-result and hole-score query support for deriving hole ordinals from the first imported player result per round division.
- [x] 2.4 Add basket option query support for loading baskets and variations for a selected basket course.
- [x] 2.5 Create mapping admin read-model classes for competitions, basket course options, variation options, round tables, division columns, and cell values.

## 3. Mapping Administration Service

- [x] 3.1 Add a transactional service for loading the mapping editor model for no selection, selected competition, and selected competition plus basket course.
- [x] 3.2 Implement hole ordinal derivation using the first round result per round division, with layout hole count fallback when no scores exist.
- [x] 3.3 Load existing `basket_variation_round_division` rows and preselect variation ids in the read model.
- [x] 3.4 Implement a save method that accepts selected competition id, selected basket course id, and submitted cell values.
- [x] 3.5 Validate that submitted round division ids belong to the selected competition before saving.
- [x] 3.6 Validate that submitted hole ordinals are known editable slots for their round divisions before saving.
- [x] 3.7 Validate that submitted basket variation ids belong to the selected basket course before saving.
- [x] 3.8 Upsert non-empty submitted cells and delete existing mappings for submitted empty cells in one transaction.

## 4. Web Controller and Template

- [x] 4.1 Add Spring MVC routes for viewing the mapping admin page with optional competition and basket course selections.
- [x] 4.2 Add a POST route for saving all submitted mapping cells and redirecting back to the selected editor view.
- [x] 4.3 Add a Thymeleaf template with a competition selector showing competition name and start date in brackets.
- [x] 4.4 Add a basket course selector above the round tables.
- [x] 4.5 Render one horizontally scrollable table per round, with divisions as columns and basket-number rows.
- [x] 4.6 Render each editable cell as a select whose submitted value is a `basket_variation_id`, including an empty option for clearing a mapping.
- [x] 4.7 Display success and validation feedback without losing the selected competition and basket course context.

## 5. Preservation and Verification

- [x] 5.1 Confirm PDGA import code does not create, update, or delete `basket_variation_round_division` records.
- [x] 5.2 Confirm existing basket course administration pages still compile and remain routed as before.
- [x] 5.3 Update local cleanup SQL or related scripts if they need to delete from the new mapping table before deleting dependent round/division or basket data.
- [x] 5.4 Compile the application successfully.
- [x] 5.5 Manually inspect the implemented routes, template bindings, and save validation against the spec.

## Why

Basket variation assignment currently belongs on `hole_score`, which repeats the same variation choice for every player in a round/division. All players in the same round and division play the same basket variation for a given hole ordinal, so the mapping should be stored once at the round-division hole level.

This change adds the administration workflow for assigning manual basket variations to imported round/division hole slots and removes the denormalized score-level basket variation relationship.

## What Changes

- Add a `basket_variation_round_division` persistence model that maps `round_division_id` and `hole_ordinal` to a `basket_variation_id`.
- **BREAKING** Remove `basket_variation_id` from `hole_score` and remove the corresponding `HoleScore` entity relationship.
- Add a local admin page for selecting a competition, sorted by competition start date descending with the date shown in brackets.
- After a competition is selected, show a basket course selector and one editable mapping table per round.
- Render each round table with divisions as columns and basket numbers as rows, using hole ordinals from the first imported player result in each round/division.
- Render each editable cell as a basket/variation select whose options come from the selected basket course.
- Add a single save action that atomically saves all mapping changes for the selected competition and basket course.
- Preserve existing basket course, basket, and basket variation maintenance pages.
- Preserve PDGA import behavior and do not infer these manual mappings from PDGA layout data.

## Capabilities

### New Capabilities

- `basket-variation-round-division-mapping`: Local administration and persistence for mapping manual basket variations to imported round/division hole slots.

### Modified Capabilities

- None.

## Impact

- Adds a Liquibase migration for the new mapping table and removal of `datas.hole_score.basket_variation_id`.
- Adds JPA entity and repository support for `datas.basket_variation_round_division`.
- Updates `HoleScore` to remove the `BasketVariation` relationship.
- Adds service/read-model logic for loading competitions, rounds, round divisions, hole ordinals, basket courses, baskets, variations, and existing mappings.
- Adds Spring MVC routes and a Thymeleaf template for the mapping administration workflow.
- Adds validation to ensure submitted variation ids belong to the selected basket course and submitted mapping cells belong to the selected competition context.
- May update cleanup SQL or other local scripts if they reference score-level basket variation data.

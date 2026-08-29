## Context

The application imports PDGA competition structure into `competition`, `round`, `competition_division`, `round_division`, `round_result`, and `hole_score`. Separately, Part 1 added a manual basket model with `basket_course`, `basket`, and `basket_variation` plus local Thymeleaf administration pages for maintaining those records.

`hole_score` currently has an optional `basket_variation_id` column and `HoleScore` has a `BasketVariation` relationship. That places the basket variation choice at the player score level, even though every player in a given `round_division` plays the same basket variation for a given `hole_ordinal`. The desired assignment belongs one level higher than individual player hole scores.

## Goals / Non-Goals

**Goals:**

- Store each manual basket variation assignment once per `round_division` and hole ordinal.
- Remove score-level basket variation storage from the database and Java domain model.
- Provide a local Spring MVC/Thymeleaf page for selecting a competition, selecting a basket course, editing all round/division/hole mappings, and saving in one action.
- Derive editable hole rows from imported scoring data, using the first available `round_result` for each round/division as the primary source of hole ordinals.
- Validate submitted mappings against the selected competition and basket course before saving.
- Preserve existing PDGA import behavior and existing basket-course maintenance behavior.

**Non-Goals:**

- Do not infer basket mappings automatically from PDGA layouts.
- Do not modify score import behavior beyond removing the obsolete score-level basket variation field.
- Do not add delete operations for basket courses, baskets, or basket variations.
- Do not add authentication, authorization, audit history, or production admin infrastructure.
- Do not build a client-side application framework or JSON API unless needed by the server-rendered page.

## Decisions

1. Use `round_division_id` plus `hole_ordinal` as the mapping key.

   Add `datas.basket_variation_round_division` with a foreign key to `datas.round_division`, `hole_ordinal`, and `basket_variation_id`. `round_division` already represents the imported round/division combination, so using it avoids duplicating `round_id` and `competition_division_id` in the new table. Add a unique constraint on `(round_division_id, hole_ordinal)` so each playable slot has at most one manual variation assignment.

   Alternative considered: store `round_id` and `competition_division_id` directly. That recreates `round_division` and introduces the possibility that a mapping exists for a round/division pair the importer has not materialized.

2. Treat empty submitted cells as deleted mappings.

   The mapping page is a complete editor for the selected competition/basket course view. When a cell is submitted without a variation id, the service should remove any existing mapping for that `round_division` and `hole_ordinal`. This keeps the single save action predictable and lets users clear mistakes without separate delete controls.

   Alternative considered: leave blank submitted cells unchanged. That makes it impossible to clear a mapping through the grid and makes repeated saves harder to reason about.

3. Build a read model for the grid instead of exposing JPA graphs directly to Thymeleaf.

   The controller should load a purpose-built model containing competitions, selected competition, basket courses, selected basket course, basket/variation options, rounds, round-division columns, hole ordinals, and selected variation ids. This avoids lazy-loading surprises in templates and keeps validation logic out of the view.

   Alternative considered: pass entities and maps directly to the template. That matches the small Part 1 pages but becomes fragile for the nested competition/round/division/hole grid.

4. Derive hole rows from the first imported round result per round division, with layout fallback.

   For each `round_division`, use the first `round_result` ordered by id and its `hole_score` ordinals to determine available basket-number rows. If no hole scores exist, fall back to `round_division.layout.holes` when present. For a round table, render enough rows to cover the maximum hole ordinal count among that round's divisions.

   Alternative considered: always use `layout.holes`. Imported scores are the source requested for the workflow and can reflect what was actually scored, while layout metadata may be missing or less specific.

5. Validate submitted mappings against both competition context and basket course context.

   The save service should accept the selected competition id, selected basket course id, and a set of cell submissions. It must verify that every submitted `round_division_id` belongs to a round in the selected competition, every submitted hole ordinal is part of the rendered/known slot set for that round division, and every non-empty `basket_variation_id` belongs to a basket in the selected basket course.

   Alternative considered: rely on hidden form fields and foreign keys. Foreign keys prevent nonexistent ids, but they do not prevent cross-competition or cross-course assignments from crafted requests.

6. Remove `hole_score.basket_variation_id` through a forward migration.

   Since the current importer does not populate `HoleScore.basketVariation`, migration can remove the column after creating the new mapping table. If local databases may already contain score-level values, implementation should either document that the old column was unused or migrate only values that are consistent for every player within the same `(round_division_id, hole_ordinal)` group.

   Alternative considered: keep the old column nullable for compatibility. That leaves two sources of truth and preserves the denormalization this change is meant to remove.

## Risks / Trade-offs

- Existing local score-level variation data may exist -> Check whether `hole_score.basket_variation_id` has values before dropping it; if needed, migrate only unambiguous grouped values into the new table.
- First-player hole counts may be incomplete -> Use layout hole count as fallback and consider max hole ordinal across any existing score rows if the first result is incomplete.
- Large competitions can create wide tables -> Keep the UI simple for localhost use, but make the table horizontally scrollable so many divisions remain usable.
- Cross-course selections can corrupt analysis assumptions -> Validate that every variation belongs to the selected basket course before committing.
- Concurrent admin edits can overwrite mappings -> Use last-write-wins for this local workflow; a future change can add optimistic locking or audit history if needed.

## Migration Plan

1. Create `datas.basket_variation_round_division` with foreign keys to `round_division` and `basket_variation`, plus `UNIQUE (round_division_id, hole_ordinal)`.
2. Optionally migrate existing non-null score-level variation ids into the new table only when all scores in a `(round_division_id, hole_ordinal)` group agree on the same variation id.
3. Drop `datas.hole_score.basket_variation_id`.
4. Remove the `HoleScore.basketVariation` mapping.
5. Add the new admin service, controller routes, repository queries, and template.

Rollback would require recreating `hole_score.basket_variation_id`; only one representative mapping per round/division/hole could be copied back to every matching score row, so rollback is lossy if the new table is edited after deployment.

## Open Questions

- Should the migration preserve any existing local score-level mapping data, or can the implementation assume the old column was unused because no current code writes it?
- Should the competition selector display `name` or prefer `simple_name` when present?

## Context

The basket variation mapping editor is a server-rendered Thymeleaf page backed by `BasketVariationMappingAdminService`. It currently has one selected basket course and one same-layout checkbox for the whole competition editor. Saved mappings are stored per `round_division_id` and `hole_ordinal` in `datas.basket_variation_round_division`; the editor's selected basket course and same-layout mode are not persisted.

Rounds can use different manual basket courses or require different same-layout behavior. Treating these choices as global page state makes mixed-course competitions awkward and loses useful editor context after a save or later revisit.

## Goals / Non-Goals

**Goals:**

- Persist selected basket course and same-layout mode per imported round.
- Keep these settings separate from imported PDGA `round` data.
- Render basket course and same-layout controls inside each round section.
- Use each round's selected basket course to populate and validate that round's mapping options.
- Save all round settings and mappings atomically through the existing save action.
- Extend browser round copy so it copies current source round settings plus current mapping values.

**Non-Goals:**

- Do not alter the meaning of `basket_variation_round_division` mapping rows.
- Do not add settings at competition level.
- Do not change PDGA import behavior or derive settings from imported PDGA course/layout data.
- Do not persist browser copy immediately; copied values remain pending until Save Mappings.
- Do not support multiple basket courses within a single round.

## Decisions

### Store round mapping preferences in a separate table

Add `datas.basket_variation_round_settings` with one row per imported round:

- `round_id BIGINT PRIMARY KEY REFERENCES datas.round(id)`
- `basket_course_id BIGINT REFERENCES datas.basket_course(id)`
- `same_layout BOOLEAN NOT NULL DEFAULT false`

These settings are local administration metadata for basket variation mapping, not imported PDGA round data. A separate table avoids coupling manual editor state to the imported `round` entity while still giving a simple one-to-one relationship.

Alternative considered: add nullable columns directly to `datas.round`. That is simpler but makes imported round data carry UI/workflow preferences that are not part of the PDGA source model.

### Default missing settings without eagerly creating rows

When a round has no settings row, the editor should display the first available basket course and same-layout off. A settings row is created or updated only when the user saves the mapping form.

Alternative considered: create settings rows while loading the editor. That would make a GET request mutate the database and could create rows for competitions the user only inspected.

### Load variation options per round

Each `RoundTable` should carry its selected basket course id, same-layout value, and variation options for that course. Mapping cell options are therefore scoped to the round table instead of shared across the whole editor.

Alternative considered: render every basket variation in every select and validate after submit. That would make invalid round/course combinations easy to choose and harder to review visually.

### Validate submitted mappings against each round's selected course

During save, submitted round settings should be parsed first. For each submitted cell, the service should identify the cell's round through its round division, then validate the submitted basket variation against that round's selected basket course. Same-layout expansion should use only rounds whose submitted same-layout setting is enabled.

Alternative considered: keep a single global allowed variation id set. That cannot support rounds that select different basket courses.

### Copy settings before copying cell values in the browser

The copy script should set the target round's basket course selector and same-layout checkbox from the current source round values before applying copied cell values. The target round's mapping options must be consistent with the copied values; if changing the course requires option lists to change, the implementation should update target row options in the browser before setting copied values or use a submit/reload interaction that preserves pending copy intent.

Alternative considered: copy only cell values and leave target settings unchanged. That can place source course variation ids into a target round configured for a different course, producing confusing or invalid form state.

## Risks / Trade-offs

- Existing saved mappings may point to variations from multiple basket courses within one round -> The editor should infer a best initial course only when no settings row exists if practical, otherwise use the default first course and rely on validation on the next save.
- Per-round option lists increase rendered page size -> Expected local admin data size is small; keep the plain Thymeleaf form and avoid a frontend framework.
- Browser copy with different source/target courses is more complex -> Keep settings and cell metadata explicit in the DOM and test the generated markup/script behavior.
- Same-layout can overwrite non-source division mappings for a round -> This remains intentional behavior, but now it is limited to rounds with same-layout enabled.

## Migration Plan

Create the new settings table with no backfill required. Existing mappings remain valid. On first editor load after deployment, rounds without settings use the default basket course and same-layout off until the user saves.

Rollback can remove the new entity/repository and UI controls, then ignore or drop `datas.basket_variation_round_settings`; existing `basket_variation_round_division` rows are unaffected.

## Open Questions

- Should initial settings for rounds with existing mappings be inferred from the most common mapped basket course, or should every missing setting use the first available basket course?

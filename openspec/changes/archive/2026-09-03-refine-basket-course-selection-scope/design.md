## Context

The current working tree includes the completed `persist-round-mapping-settings` implementation: basket course and same-layout state are round-level and persisted in `datas.basket_variation_round_settings`. The revised desired behavior removes persistence and makes mapping controls request/form state with three possible scopes:

- Competition scope by default.
- Round scope when `Course selection by round` is enabled.
- Group/column scope for individual rounds when `Course selection by group` is enabled.

The existing mapping persistence remains `datas.basket_variation_round_division`; selected course and layout controls only determine available options, validation, and same-layout expansion during the current request/save.

## Goals / Non-Goals

**Goals:**

- Restore competition-level basket course and same-layout controls as the default mode.
- Add opt-in round-level course selection.
- Add opt-in group-level course selection per round when round-level selection is enabled.
- Disable round copy whenever course selection is not competition-scoped.
- Force same-layout off and unavailable for rounds using group-level course selection.
- Remove round settings persistence from Java code.
- Add a forward Liquibase changeset that drops the already-applied settings table.

**Non-Goals:**

- Do not persist editor course/same-layout scope choices.
- Do not change `basket_variation_round_division` schema or semantics.
- Do not change PDGA import behavior.
- Do not add copy support for round-scoped or group-scoped course selection.
- Do not infer group/course settings from PDGA layout metadata.

## Decisions

### Treat all control choices as submitted form state

The controller should accept competition-level fields plus optional round/group scoped fields. The service should build an effective course assignment for each editable cell from the submitted scope mode, validate against that assignment, and persist only mapping rows.

Alternative considered: keep the settings table for defaults. That conflicts with the request to remove save-state functionality and would make UI behavior harder to predict.

### Use a single active scope hierarchy

Course assignment resolves in this order:

1. If `courseSelectionByRound` is false, every cell uses the competition-level basket course.
2. If `courseSelectionByRound` is true and a round's `courseSelectionByGroup` is false, every cell in that round uses the round-level basket course.
3. If a round's `courseSelectionByGroup` is true, each cell uses its division/group column basket course.

Same-layout resolves similarly:

1. Competition-level same-layout applies only when `courseSelectionByRound` is false.
2. Round-level same-layout applies only when `courseSelectionByRound` is true and that round is not group-scoped.
3. Group-scoped rounds always have same-layout false.

Alternative considered: allow same-layout with group-scoped course selection. That would be internally inconsistent because same-layout copies one source division's variation ids into divisions that may use different basket courses.

### Hide or disable unavailable controls rather than saving impossible states

Round copy controls should render only when course selection by round is off. Round-level basket course, same-layout, and course-selection-by-group controls should render only when course selection by round is on. Same-layout controls should be disabled and unchecked in group-scoped rounds. Unavailable fields should not drive backend behavior even if submitted manually.

Alternative considered: keep controls visible and reject invalid combinations on save. That makes the local admin workflow slower and less clear.

### Drop the settings table with a new changeset

Because Liquibase already applied the table creation, the implementation should not edit or remove changeset `010`. Add a later changeset that drops `datas.basket_variation_round_settings`.

Alternative considered: delete the earlier changeset. That would break databases where Liquibase has already recorded it.

## Risks / Trade-offs

- Switching scope can invalidate selected variations -> Clear or ignore values that do not belong to the newly effective basket course and validate server-side.
- Group-level selectors can make table headers dense -> Keep labels compact and reuse existing native selects.
- No saved control state means revisiting the editor resets controls to defaults -> This is intentional per the revised request.
- Existing local DB still has the obsolete table until migration runs -> Add a forward drop-table changeset.

## Migration Plan

Add a new Liquibase changeset after `010`:

```sql
DROP TABLE IF EXISTS datas.basket_variation_round_settings;
```

Remove the Java entity and repository for round settings. Existing mapping rows are unaffected. Rollback would require reintroducing the settings table and code, but the desired behavior no longer depends on saved settings.

## Open Questions

- None.

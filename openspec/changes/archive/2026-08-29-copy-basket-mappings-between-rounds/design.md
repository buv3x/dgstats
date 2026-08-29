## Context

The basket variation mapping page is a server-rendered Thymeleaf form. A selected competition and basket course produce one round table per imported round, with divisions as columns, basket/hole ordinals as rows, and each editable cell represented by a select named `cell_{roundDivisionId}_{holeOrdinal}`. The existing POST route persists the submitted form atomically; until that save action is used, changes live only in the browser form.

This feature adds a convenience action for copying visible mapping values between round tables. It should behave like manual select edits: visible immediately in the form, reversible by further manual changes, and persisted only by the existing save button.

## Goals / Non-Goals

**Goals:**

- Add copy buttons near each round label for copying from every other displayed round into that target round.
- Copy current form values, including values changed by the user since the page loaded.
- Match source and target cells by division identity and basket/hole ordinal.
- Preserve the existing save route, validation, and persistence model.
- Keep the enhancement usable on the existing plain Thymeleaf page without introducing a frontend framework.

**Non-Goals:**

- Do not add a backend copy endpoint or persist copied values immediately.
- Do not add database changes or new mapping entities.
- Do not infer mappings across different divisions, basket numbers, basket courses, or competitions.
- Do not implement undo history beyond allowing users to manually edit copied select values before saving.

## Decisions

1. Implement copy as client-side form mutation.

   The page already contains every loaded mapping select and all available variation options. A button can copy selected values between existing select elements without contacting the server. This directly satisfies "without saving" and keeps the existing POST route as the only persistence path.

   Alternative considered: add a backend copy route. That would either save immediately, which contradicts the desired behavior, or return a rebuilt form, which is more complex than needed for local form-state copying.

2. Match cells using rendered metadata rather than column position.

   Each editable select should expose metadata for round id, a stable division copy key, and hole ordinal. The copy script should find source and target cells by matching `(division copy key, hole ordinal)` across different round ids.

   The preferred division copy key is `competitionDivision.id` when present because imported round divisions for the same PDGA division should share that identity across rounds. If no competition division exists, fall back to `divisionCode`; if that is unavailable, use the rendered division label or round-division id as a last-resort non-matching identity.

   Alternative considered: copy by table column index. That is fragile if one round has missing divisions or a different display order.

3. Leave unmatched target cells unchanged.

   If the source round lacks a matching division or hole ordinal for a target select, the copy action should not change that target select. This avoids accidental clearing when rounds have different layouts or division participation.

   Alternative considered: clear unmatched target cells. That would make copying destructive in competitions where later rounds have different layouts or incomplete imported scoring data.

4. Copy blank values when a matching source cell exists.

   A source select set to "No mapping" should copy its empty value into the matching target select. This mirrors manual editing and lets a user intentionally clear a target round by copying a blank source round.

   Alternative considered: skip blank values. That would make "copy this round exactly" less predictable.

## Risks / Trade-offs

- JavaScript disabled means copy controls cannot operate -> Keep the existing form fully usable and hide or inert the enhancement when scripting is unavailable.
- Division matching fallback can be imperfect when imported division identity is missing -> Prefer `competitionDivision.id` and use fallbacks only for incomplete data.
- Users may accidentally overwrite unsaved target edits -> The action remains unsaved until the existing save button, and users can manually adjust copied values before saving.
- Different hole counts between rounds may produce partial copies -> Leave unmatched target cells unchanged and rely on visible form state to show the result before save.

## Migration Plan

No data migration is required. Deployment is a template/read-model enhancement only. Rollback removes the copy controls and script while preserving all existing saved mappings.

## Open Questions

- Should the UI show a lightweight count of copied cells after a copy action, or is visible select state enough for the local admin workflow?

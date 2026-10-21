## Context

The static statistics page currently defines its initial state in two places: the active/hidden classes in `docs/index.html` and the `setActiveView("course")` call in `docs/app.js`. Personal stats similarly has a `personalView = "hole"` state, Hole stats marked active in the markup, and reset logic that restores Hole stats. The putting metric is rendered by the shared `appendPuttingHighlights` function for both overall and yearly summaries.

## Goals / Non-Goals

**Goals:**

- Make the top-level navigation order Leaderboards, Personal stats, Course stats, Hole stats, Description.
- Make Leaderboards the initial view consistently before and after JavaScript initialization.
- Make Putting stats the first and default Personal stats subview, including reset paths.
- Update the visible putting metric label in all summary contexts.
- Preserve all existing data, calculations, category ordering, and view-selection behavior.

**Non-Goals:**

- No changes to exported JSON field names or putting calculations.
- No changes to leaderboard categories, ranking rules, or year selection.
- No URL routing or persistent user-preference state.

## Decisions

### Set defaults in both markup and runtime initialization

Update the initial HTML classes and `hidden` attributes, then update `initializePage()` to call `setActiveView("leaderboards")`. This prevents a flash of the old Course stats view while the manifest loads and keeps no-script/initial-render behavior aligned with runtime behavior.

### Keep the remaining top-level views in their existing relative order

Place Leaderboards first and Personal stats second; retain Course stats, Hole stats, and Description after them. This limits the change to the requested prioritization rather than redesigning the navigation.

### Make Putting stats the canonical personal default

Reorder the Personal stats subnav, initialize `personalView` to `putting`, and update `resetPersonalResults()` plus active/hidden markup state. Player selection and render paths will therefore show putting summaries first without changing how Hole stats is selected explicitly.

### Change presentation text only

Keep `threePlusPuttPerRound` as the compatibility-preserving data property and change only the shared display string to `Lost 3+putting per round`. Because both overall and yearly summaries use the shared renderer, the label remains consistent.

## Risks / Trade-offs

- [Existing browser tests assume Course stats or Hole stats is initially active] → Update those assertions to the new defaults while retaining explicit-view coverage for the old tabs.
- [Only one of the HTML and JavaScript defaults is changed] → Test both initial markup and post-initialization state.
- [The new label is applied in only one summary scope] → Cover both overall and expanded yearly putting summaries, which share the renderer.

## Migration Plan

Update the static page markup, initialization/reset state, and focused browser/spec assertions. No data regeneration or migration is required. Rollback consists of reverting the navigation/default and presentation-only changes.

## Open Questions

None.

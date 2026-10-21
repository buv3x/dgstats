## Context

The leaderboard exporter currently uses one pair of eligibility constants for every scope, so yearly and Overall leaderboards receive the same thresholds. The static page currently builds its period selectbox with Overall first and selects Overall by default. Leaderboard data already carries separate `overall` and year scopes, and the page already rerenders every category from the selected scope without another request.

## Goals / Non-Goals

**Goals:**

- Apply 100 C1x putts and 180 played holes only to the Overall scope.
- Apply 50 C1x putts and 90 played holes to every yearly scope.
- Make the period selector list years from the current calendar year down to 2025, followed by Overall.
- Select the current calendar year by default for every leaderboard category.
- Keep threshold notes accurate for the selected scope.
- Regenerate the consolidated leaderboard export.

**Non-Goals:**

- Changing putting classification, complete-round detection, ranking formulas, tie handling, or Personal stats.
- Changing the set of exported years or adding a new data source.
- Reordering the categories within the Leaderboards view.
- Changing the meaning of the Overall scope.

## Decisions

### Select thresholds by scope

Keep separate named constants for Overall and yearly C1x/C2+ thresholds. The exporter chooses the threshold pair from the scope passed to each leaderboard metric method: `overall` uses 100/180, while any calendar-year scope uses 50/90. Filtering remains before ranking so excluded candidates cannot affect places or the top-ten boundary.

An alternative would be to change the data after ranking or to infer eligibility in the browser. Both would produce incorrect ranking boundaries or duplicate business rules, so eligibility remains in the exporter.

### Keep scope data order independent from UI option order

The exporter may retain its existing scope serialization order because the browser finds scopes by their `scope` key. The UI will own presentation order and build options by iterating from the current year down to 2025, then appending Overall. This avoids coupling JSON serialization order to the selectbox requirement.

### Derive the default from the calendar year

Initialize the selected leaderboard period to the current calendar year, using the same dynamic year range as the options. The default is represented by the year value rather than a hard-coded year so the page continues to work when a new calendar year begins.

### Make threshold notes scope-aware

The existing note renderer will choose both the message and threshold values from the selected category and scope. C1x will display 100 for Overall and 50 for a year; C2+ per round will display 180 for Overall and 90 for a year. Other categories will continue to hide the note.

## Risks / Trade-offs

- [A stricter Overall threshold and looser yearly threshold can show different players] → Keep the scope-specific rule visible in the affected category note.
- [The current year may have no qualifying entries] → Keep the current year selectable and show the existing empty-state message rather than falling back to Overall.
- [Future years could be absent from the generated payload] → Preserve the existing selector range and let the normal empty-state handling represent missing qualifying entries.
- [Scope order in JSON and option order differ] → Resolve scopes by their explicit key, not array position.

## Migration Plan

1. Update exporter threshold selection and UI period initialization/order/note text.
2. Regenerate `docs/data/leaderboards.json`.
3. Verify Overall and yearly boundary behavior, current-year default, descending option order, and empty-year rendering.

Rollback is limited to restoring the prior constants, Overall-first selector initialization, and generated leaderboard asset.

## Open Questions

None. The requested thresholds and selector ordering are specific.

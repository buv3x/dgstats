## Why

The statistics page currently opens on Course stats and places Leaderboards after the other statistics views, while Personal stats opens on Hole stats. This makes the most comparison-oriented view and the most relevant personal putting view less discoverable, and the new putting metric's wording does not clearly describe the lost throws it measures.

## What Changes

- Move the `Leaderboards` top-level tab to the first position and make it the initial default view.
- Move `Personal stats` to the second top-level position while preserving the relative order of the remaining views.
- Move `Putting stats` before `Hole stats` inside Personal stats and make Putting stats the default personal subview.
- Rename the displayed `3+putt per round` metric to `Lost 3+putting per round` in overall and yearly putting summaries.
- Preserve the existing `threePlusPuttPerRound` export field and calculation semantics.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: update top-level and Personal stats tab ordering, default views, and the putting metric label.
- `leaderboards`: update the Leaderboards navigation placement and initial default-view behavior.

## Impact

- Affects the static page markup and client-side initialization/reset state in `docs/index.html` and `docs/app.js`.
- Affects browser/UI assertions covering navigation order, initial visibility, Personal stats subview defaults, and putting-summary labels.
- No API, export schema, database, leaderboard calculation, or external dependency changes are required.

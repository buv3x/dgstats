## 1. Update top-level navigation and default view

- [x] 1.1 Reorder the top-level tabs in `docs/index.html` to Leaderboards, Personal stats, Course stats, Hole stats, and Description.
- [x] 1.2 Update the initial HTML active/hidden state so Leaderboards is visible and active while all other top-level views are hidden.
- [x] 1.3 Update `initializePage()` in `docs/app.js` to initialize the page with `setActiveView("leaderboards")`.

## 2. Update Personal stats default and label

- [x] 2.1 Reorder the Personal stats subnav so Putting stats precedes Hole stats and update its initial active/hidden state.
- [x] 2.2 Set the personal view state and reset paths to default to Putting stats while preserving explicit switching to Hole stats.
- [x] 2.3 Rename the shared three-plus-putting display label to `Lost 3+putting per round` for both overall and yearly summaries without changing the `threePlusPuttPerRound` data field.

## 3. Verify the change

- [x] 3.1 Update affected static-page checks/spec assertions for top-level order, Leaderboards default state, Personal stats subview order/default, and the new metric label.
- [x] 3.2 Confirm the modified JavaScript remains syntactically valid and existing data-loading, leaderboard, Hole stats, and explicit view-switching paths remain intact.

## 1. Export eligibility and ranking

- [x] 1.1 Add 100-putt C1x and 180-hole C2+ per-round eligibility thresholds to leaderboard aggregation for Overall and yearly scopes.
- [x] 1.2 Track summed complete-round expected holes per player and scope for the C2+ per-round threshold.
- [x] 1.3 Extend Best C1x leaderboard candidates with best-round putt count and exact-result occurrence count.
- [x] 1.4 Update Best C1x ranking and tie-place calculation to sort by percentage, putts thrown, exact-result count, and name.
- [x] 1.5 Regenerate `docs/data/leaderboards.json` and verify threshold filtering and Best C1x ordering in each scope.

## 2. Threshold explanations

- [x] 2.1 Render the C1x threshold note below the table when the C1x category is selected.
- [x] 2.2 Render the C2+ per-round threshold note below the table when the C2+ per-round category is selected.
- [x] 2.3 Add small italic responsive styling for the threshold notes without changing other category presentation.

## 3. Verification

- [x] 3.1 Compile the application with the project’s Java 17 toolchain.
- [x] 3.2 Validate generated JSON and JavaScript syntax, including empty and below-threshold scopes.

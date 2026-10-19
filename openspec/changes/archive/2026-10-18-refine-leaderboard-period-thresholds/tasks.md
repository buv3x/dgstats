## 1. Scope-specific leaderboard eligibility

- [x] 1.1 Split leaderboard eligibility thresholds into Overall values of 100 C1x putts and 180 holes and yearly values of 50 C1x putts and 90 holes.
- [x] 1.2 Apply the appropriate threshold pair independently for Overall and each yearly scope before ranking candidates.

## 2. Leaderboard period selection

- [x] 2.1 Initialize the selected leaderboard period to the current calendar year.
- [x] 2.2 Populate the period selectbox with years from the current year down to 2025, followed by Overall.
- [x] 2.3 Preserve the same period ordering and current-year default when switching among all leaderboard categories.
- [x] 2.4 Update C1x and C2+ per-round threshold notes to reflect whether the selected scope is Overall or yearly.

## 3. Generated data and verification

- [x] 3.1 Regenerate `docs/data/leaderboards.json` using the updated scope-specific eligibility rules.
- [x] 3.2 Verify Overall and yearly boundary filtering, descending period options, current-year default, and empty-year handling.
- [x] 3.3 Compile the application and validate the generated JSON and JavaScript syntax.

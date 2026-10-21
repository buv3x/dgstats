## Why

The current leaderboard export applies the stricter sample-size requirements uniformly, even though yearly leaderboards should retain the original 50-putt and 90-hole coverage. The period selector also prioritizes Overall, which makes the current season less discoverable and does not match the desired leaderboard workflow.

## What Changes

- Keep the Overall leaderboard thresholds at 100 total C1x putts and 180 played holes.
- Restore yearly leaderboard thresholds to 50 total C1x putts and 90 played holes.
- Apply each threshold independently to its scope: Overall uses the strict thresholds, while every yearly scope uses the original thresholds.
- Order leaderboard period selectbox options by year descending, with the current calendar year first and Overall last.
- Select the current calendar year by default when the Leaderboards view is initialized.
- Apply the period ordering and default consistently across all leaderboard categories.
- Regenerate leaderboard data and update threshold explanations to reflect the selected scope.

## Capabilities

### New Capabilities

- `leaderboard-period-thresholds`: Defines scope-specific eligibility thresholds and period-selection behavior for all leaderboard categories.

### Modified Capabilities

<!-- No archived main capability spec currently defines these leaderboard requirements. -->

## Impact

- `BasketStatisticsExportService` leaderboard scope filtering and generated `docs/data/leaderboards.json`.
- Leaderboard period selectbox initialization and ordering in `docs/app.js`.
- Threshold explanation text in the static leaderboard UI.
- No changes to putting classification, Personal stats calculations, database schema, or external APIs.

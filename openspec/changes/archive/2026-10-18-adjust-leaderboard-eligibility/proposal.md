## Why

The initial leaderboards expose very small samples, which can make extreme percentages and per-round averages look authoritative. Adding minimum sample thresholds will make these rankings more stable, while refining Best C1x ordering will favor results supported by more attempts before considering repeat frequency.

## What Changes

- Require at least 100 C1x putts in the selected Overall or yearly scope for a player to appear in the C1x leaderboard.
- Require at least 180 played holes in the selected Overall or yearly scope for a player to appear in the C2+ per round leaderboard.
- Display a small italic explanatory note below the leaderboard table for each thresholded category.
- Sort Best C1x round entries by percentage descending, then putts thrown descending, then the number of times the exact result occurred descending.
- Regenerate the leaderboard export so threshold filtering and ordering apply to Overall and yearly data.

## Capabilities

### New Capabilities

- `leaderboard-eligibility`: Defines sample-size requirements, explanatory labels, and refined Best C1x ranking.

### Modified Capabilities


## Impact

- `BasketStatisticsExportService` leaderboard aggregation and generated `docs/data/leaderboards.json`.
- Leaderboard table rendering in `docs/index.html`, `docs/app.js`, and `docs/styles.css`.
- No changes to putting classification, Personal stats, database schema, or external APIs.

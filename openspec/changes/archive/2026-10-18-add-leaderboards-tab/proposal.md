## Why

The static statistics page currently lets users inspect course, hole, and individual-player statistics, but it does not provide a way to compare the strongest putting performances across players. A dedicated Leaderboards tab will make the existing complete-round putting data discoverable as concise, year-filterable top-player rankings.

## What Changes

- Add a top-level `Leaderboards` tab between `Personal stats` and `Description`.
- Add second-level tabs for C1x, C2+ per round, Most C2+ in a round, Best C1x round, and Longest throw-in.
- Add a year selector with `Overall` selected by default and yearly options from 2025 through the current year.
- Display ranked player lists with place, name, and value columns, without a table header.
- Enforce one leaderboard entry per player, deterministic tie handling, alphabetical ordering for tied players, and inclusion of all entries tied at the top-10 boundary.
- Reuse existing putting value formatting and contextual tooltips for Most C2+, Best C1x round, and Longest throw-in.
- Keep C1x and C2+ per round values free of tooltips; C2+ per round has no occurrence-count suffix.
- Mark Most C2+ values with an asterisk and hole-count tooltip context when round length affects their ranking.
- Export aggregated leaderboard data for the static page so the browser does not need to load every personal-statistics file.

## Capabilities

### New Capabilities

- `leaderboards`: Defines leaderboard categories, year filtering, ranking, tie handling, value presentation, and contextual explanations.

### Modified Capabilities


## Impact

- Static page markup and navigation in `docs/index.html`.
- Leaderboard rendering, filtering, ranking presentation, and tooltip behavior in `docs/app.js` and `docs/styles.css`.
- Statistics export model and generated assets in `BasketStatisticsExportService` and `docs/data`.
- Existing putting calculations remain the source of truth; no external API or database schema changes are expected.

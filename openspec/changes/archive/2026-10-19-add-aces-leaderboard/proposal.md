## Why

The Leaderboards view currently covers putting and throw-in highlights, but it does not expose hole-in-one results. Aces are a distinct event whose authoritative data is the recorded hole score and mapped basket variation distance, so they need their own leaderboard behavior without depending on putting-throw completeness.

## What Changes

- Add an `Aces` leaderboard category with the same place/name/value table layout as the existing categories.
- Export every ace occurrence where `hole_score.score = 1`, including multiple occurrences for the same player.
- Use the mapped `basket_variation.distance` value in meters and rank entries by distance only.
- Include competition, round, and hole in each ace value tooltip.
- Show every ace in the selected scope rather than limiting the category to ten ranks.
- Make ace year options data-driven, including years before 2025 and excluding years without an ace.
- Keep existing year behavior for other leaderboard categories.
- Append `m` to Longest throw-in leaderboard values as well as Aces values.
- Do not change personal statistics or other leaderboard categories.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `leaderboards`: Add the Aces category, ace occurrence export/ranking/display rules, and meter suffix behavior.
- `leaderboard-period-thresholds`: Allow the Aces category to use ace-derived year options independently of the existing 2025-to-current period list.

## Impact

- `BasketStatisticsExportService` and the hole-score/basket-variation export query or projection.
- Generated `docs/data/leaderboards.json` data.
- Leaderboard markup and rendering in `docs/index.html` and `docs/app.js`.
- Existing leaderboard specifications and tests.
- No database schema, API, personal-statistics, or `round_result_throws` changes are required.

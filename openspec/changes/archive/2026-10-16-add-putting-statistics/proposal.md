## Why

The imported PDGA throw timelines contain enough information to measure player putting performance, but the statistics export currently ignores them. Adding C1x and C2 putting percentages will make the existing Personal stats view useful for both hole performance and putting performance.

## What Changes

- Calculate per-player C1x and C2 putting totals and successful-putt counts from imported `round_result_throws`.
- Ignore invalid holes whose final throw record does not land in zone 5.
- Classify C1x and C2 attempts using zone and distance rules, including the explicit bullseye handling for C1x.
- Export putting statistics in each player’s personal statistics JSON file.
- Include players with imported throw data in the player lookup, even when they have no eligible regression-based hole rows.
- Restructure the Personal stats view so player selection is shared by second-level `Hole stats` and `Putting stats` views.
- Display C1x and C2 percentages rounded to two decimal places with successful and total counts.

## Capabilities

### New Capabilities

- `putting-statistics`: Defines C1x/C2 attempt classification, aggregation, percentage calculation, and putting-statistics display.

### Modified Capabilities

- `personal-basket-statistics`: Extends personal player eligibility and the Personal stats navigation/export model with putting statistics.
- `basket-statistics-export`: Extends personal JSON exports and player lookup generation with throw-derived putting statistics.

## Impact

- `BasketStatisticsExportService` and repository queries will read `RoundResultThrows` alongside existing hole-score export data.
- Personal statistics JSON files and the player lookup file will gain putting-related data and may include additional players.
- The static `docs` Personal stats UI and styles will gain second-level navigation and a Putting stats view.
- No external API contract changes are expected; the existing static export remains the integration boundary.

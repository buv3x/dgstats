## Why

The Personal putting statistics view currently reports aggregate C1x and C2 performance but does not surface the player's longest successful throw-in. Adding this highlight makes unusually long successful putts discoverable while preserving the existing round-based context and tooltip conventions.

## What Changes

- Export a longest-throw-in highlight for overall and yearly putting summaries.
- Consider successful putts from C1, C2, and outside C2 only when a distance is recorded.
- Select the maximum using the exact distance in feet; convert the displayed result to meters and round it to a whole number.
- Preserve all occurrences of the exact maximum distance and display an occurrence suffix using the existing highlight convention.
- Add accessible tooltip details listing the competition, round, and hole for every occurrence.
- Add regression coverage for distance fallback, successful-putt filtering, exact-feet tie handling, conversion/rounding, and tooltip data.

## Capabilities

### New Capabilities

### Modified Capabilities

- `putting-statistics`: Define successful throw-in distance collection and exact-feet maximum/tie behavior.
- `personal-basket-statistics`: Export and display overall/yearly longest throw-in highlights with occurrence tooltips.

## Impact

- `BasketStatisticsExportService` and `RoundResultThrowsRepository` projection data.
- Personal putting export DTOs and generated JSON under `docs/data/personal-stats/`.
- Personal putting rendering and tooltip behavior in `docs/app.js`.
- Putting statistics unit tests and generated-data verification.
- No database schema or external API changes.

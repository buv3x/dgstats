## Why

Global personal putting statistics currently expose only combined C1x and C2 totals. Players need normalized round-level indicators for performance from C2 and beyond, their best C2+ round, and their strongest C1x round so results can be compared across rounds with different hole counts.

## What Changes

- Add global `C2+ per round`, calculated from successful C2+ attempts and normalized to 18 holes.
- Add global `Most C2+ in a round`, preserving the exact weighted value, rounded display value, and tie count in the export.
- Add global `Best C1x round`, selected by C1x success percentage with total-putt tie-breaking and tie count.
- Count only complete rounds for all three metrics; leave existing per-round export entries and metrics unchanged.
- Display the new metrics in the global Personal stats putting summary only.
- Add focused export and static-page regression coverage.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `putting-statistics`: Define C2+ classification and normalized global round aggregation for complete rounds.
- `personal-basket-statistics`: Extend the personal putting export and global putting presentation with the new metrics.

## Impact

- `BasketStatisticsExportService` putting aggregation and exported `PuttingSummary` model.
- Personal statistics JSON files under `docs/data/personal-stats/`.
- `docs/app.js` and related putting-summary styles/content.
- Putting export and browser/static-page tests.
- No database schema, import, or external API changes.

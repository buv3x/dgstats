## Why

Putting statistics currently accept valid holes from an otherwise incomplete player round. This makes the overall totals appear more complete than the underlying score data, especially when a hole has missing or unfinished throw data. The export should distinguish complete player rounds from incomplete ones while preserving the round evidence in the Personal statistics UI.

## What Changes

- Determine putting-round completeness independently for each player’s `round_result`.
- Require every expected layout hole to have a throw sequence whose ordered final throw has `zone_id = 5`.
- Exclude all classified putts from incomplete player rounds when calculating overall C1x/C2 totals.
- Preserve incomplete rounds in round-level putting details with an explicit completeness flag.
- Render incomplete round-detail rows in italic text with strikethrough styling.
- Include player-round records with no throw rows so completely missing throw data can still be represented as incomplete.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `putting-statistics`: Change hole and player-round validity rules and aggregate only complete rounds into overall totals.
- `personal-basket-statistics`: Export completeness metadata and display incomplete putting rounds while retaining the existing round-details interaction.
- `basket-statistics-export`: Update the personal JSON export contract to include complete/incomplete putting-round data, including rounds with no classified attempts.

## Impact

- `BasketStatisticsExportService` putting aggregation and export records.
- `RoundResultThrowsRepository` putting projection, which must expose expected layout-hole information and retain round results without throw rows.
- Static Personal statistics rendering in `docs/app.js` and `docs/styles.css`.
- Regenerated files under `docs/data/personal-stats/` and related export metadata.
- No database schema, public endpoint, or import behavior changes are required.

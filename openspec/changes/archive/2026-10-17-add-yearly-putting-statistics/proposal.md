## Why

Personal putting statistics currently combine all of a player's rounds into one summary and expose one global round-details list. This makes it difficult to compare seasons and forces users to scan a long list of rounds; the existing totals should remain available while dated rounds gain a year-based view.

## What Changes

- Retain the existing global putting totals and aggregate metrics.
- Add a separate year section for every year represented by the player's dated putting rounds.
- Calculate each year's C1x, C2, C2+ per-round, most C2+ round, and best C1x round metrics using only complete rounds from that year.
- Render year sections as accordions, all closed when the player view is initially rendered.
- Move round-detail controls inside their corresponding year accordion and show only rounds from that year.
- Exclude rounds without a year from year sections and year-level round details while retaining them in global aggregates where they currently contribute.
- Remove the global round-details section.

## Capabilities

### New Capabilities

### Modified Capabilities

- `personal-basket-statistics`: Add yearly putting aggregates and year-scoped round-detail presentation while preserving global totals.

## Impact

- `BasketStatisticsExportService` and putting export data models need yearly aggregate data and round-date/year handling.
- Personal statistics JSON files under `docs/data/personal-stats/` will contain the additional yearly putting data.
- `docs/app.js` and `docs/styles.css` will render closed year accordions and nested year-level round details.
- Putting export and UI regression tests will cover year filtering, global handling of undated rounds, accordion state, and round-detail placement.
- No database schema, external API, or authentication changes are required.

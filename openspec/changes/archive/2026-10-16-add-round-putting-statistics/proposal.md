## Why

Personal putting statistics currently show only a player's combined totals, making it difficult to compare performance between competitions or rounds. The export and UI should retain the combined totals while exposing round-level detail on demand.

## What Changes

- Extend personal putting statistics with an overall total and separate aggregates for each competition and round.
- Include each round's competition simple name and abbreviated round label in the exported data.
- Keep round details collapsed by default in the Personal stats putting view and provide an accessible show/hide link.
- Render total and round metrics in the format `C1x: stats, C2: stats`.
- Render competition names, round labels, and both `C1x` and `C2` labels in bold for total and round summaries.
- Compute overall percentages from overall successful and total counts, not by averaging round percentages.

## Capabilities

### New Capabilities

### Modified Capabilities

- `personal-basket-statistics`: Add round-level putting aggregates, their exported competition/round descriptors, and collapsed detail presentation in the Personal stats UI.

## Impact

- `BasketStatisticsExportService` and `RoundResultThrowsRepository` putting export query and aggregation model.
- Personal statistics JSON files under `docs/data/personal-stats/`.
- Personal putting markup, rendering, accessibility behavior, and styles in `docs/index.html`, `docs/app.js`, and `docs/styles.css`.
- Putting aggregation and static export regression tests.
- No database schema or external API changes.

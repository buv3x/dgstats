## Why

Personal putting statistics currently show C1x, C2, and several round-based highlights, but do not quantify throws lost after a player first reaches C1 or C2. A `3+putt per round` rate provides a direct measure of putting mistakes, including penalty throws, while preserving the existing complete-round and partial-round weighting model.

## What Changes

- Add an overall `3+putt per round` metric to each player's putting summary.
- Add the same metric to each yearly putting summary.
- Identify the first C1/C2 landing on each valid hole using the existing effective zone and distance/drop-distance rules.
- Count throws and penalties after that initial landing; count only throws beyond the expected two as lost throws.
- Normalize each complete round to an 18-hole equivalent and average normalized round values, matching `C2+ per round`.
- Display the metric as the last personal putting metric with two decimal places.
- Keep the metric out of leaderboard exports and leaderboard UI.
- Add focused exporter, data-contract, and browser-rendering coverage.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `personal-basket-statistics`: extend overall and yearly putting summaries with the `3+putt per round` metric.
- `putting-statistics`: define initial C1/C2 landing detection, lost-throw calculation, penalty handling, and complete-round weighting for the new metric.
- `static-basket-statistics-page`: display the new metric as the final line in overall and yearly putting summaries with two-decimal precision.

## Impact

- `BasketStatisticsExportService` and its putting timeline projection will carry penalty counts and calculate the new aggregate.
- Personal-statistics JSON files will gain a new optional metric field in overall and yearly putting objects.
- `docs/app.js` will render the field while remaining compatible with older exports that do not contain it.
- Existing C1x, C2, C2+ per round, highlight, round-detail, and leaderboard behavior remains unchanged.
- No database migration, API endpoint, leaderboard category, or new external dependency is required.

## Why

The Personal putting statistics view currently compresses aggregate C1x and C2 metrics onto one line, uses oversized typography for round details, and exposes highlight occurrence counts without enough context. These changes improve scanability and let users identify the exact rounds behind the most notable C2+ and C1x results.

## What Changes

- Render aggregate C1x and C2 metrics on separate lines for overall and yearly summaries.
- Reduce round-detail text to 14px while preserving the existing round line format and emphasis.
- Make the complete displayed value for “Most C2+ in a round” an accessible tooltip trigger.
- Make the complete displayed value for “Best C1x round” an accessible tooltip trigger.
- Show tooltip triggers for both highlights whenever the corresponding highlight exists, including a single matching occurrence.
- Include the matching round names and abbreviated labels in each tooltip.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `personal-basket-statistics`: Update putting-summary layout, round-detail typography, and highlight tooltip behavior/data requirements.

## Impact

- Static putting statistics UI in `docs/app.js` and `docs/styles.css`.
- Personal putting export DTOs and aggregation logic in `BasketStatisticsExportService` to provide matching round descriptors for the two highlights.
- Personal putting statistics requirements and related tests/fixtures.
- No external API or database schema changes are expected.

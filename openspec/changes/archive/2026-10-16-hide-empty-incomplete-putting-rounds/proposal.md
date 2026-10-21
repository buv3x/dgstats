## Why

The Personal putting view currently renders incomplete rounds that contain no classified putting attempts. These empty rows add noise and can make the round-details section appear meaningful when there is no recorded putting data; only incomplete rounds with recorded attempts need an exclusion indicator.

## What Changes

- Hide incomplete round entries whose C1x and C2 totals are both zero.
- Continue displaying incomplete rounds when either metric has at least one recorded attempt.
- Preserve strikethrough and italic styling for displayed incomplete rounds with recorded attempts.
- Show the round-details toggle only when at least one round remains after filtering.
- Keep complete rounds and legacy rounds without a `complete` field visible, including zero-count complete rounds.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `personal-basket-statistics`: Filter empty incomplete putting rounds from the Personal putting round-details presentation while retaining non-empty incomplete rounds with exclusion styling.

## Impact

- `docs/app.js` putting-round rendering and round-details toggle creation.
- Personal statistics UI behavior only; export data and putting aggregation remain unchanged.
- Personal basket statistics presentation specification and regression coverage.

## Context

The exporter intentionally retains incomplete player rounds, including rounds with no classified putting attempts, so the data remains auditable. The Personal putting UI currently renders every exported round and applies strikethrough to every incomplete one. This creates empty, crossed-out rows that do not communicate useful putting information.

## Goals / Non-Goals

**Goals:**

- Filter empty incomplete rounds before rendering round details.
- Define a recorded putting attempt as a positive C1x or C2 total.
- Preserve visible strikethrough styling for incomplete rounds with recorded attempts.
- Avoid rendering the round-details toggle when filtering removes all rounds.
- Keep complete rounds and legacy rounds without completeness metadata visible.

**Non-Goals:**

- Changing the export schema or removing empty incomplete rounds from JSON.
- Changing overall totals or round-level aggregation.
- Changing the existing styling of displayed incomplete rounds.

## Decisions

### Filter at the UI boundary

`renderPuttingStatistics` will derive a display list from `putting.rounds` before deciding whether to create the details toggle. A round is displayed when it is complete, when its `complete` field is absent (legacy compatibility), or when it is incomplete and `c1x.total > 0 || c2.total > 0`.

Filtering in the renderer keeps the export authoritative and preserves empty incomplete rounds for diagnostics or future consumers. Filtering in the exporter was rejected because it would destroy evidence and alter the data contract.

### Reuse existing incomplete styling

`puttingRoundLine` will continue adding the `incomplete` class to displayed rounds whose `complete` value is explicitly false. No CSS change is needed; only the display-list selection changes.

### Base visibility on total attempts

The filter uses metric totals rather than successful counts. An unsuccessful recorded putt still makes the round meaningful and must remain visible; a positive successful count is already included in a positive total.

## Risks / Trade-offs

- [A malformed metric may contain non-numeric totals] → Normalize totals to finite numbers before applying the positive-attempt check, treating invalid or missing totals as zero.
- [All exported rounds may be filtered] → Create neither the toggle nor the details container when the display list is empty, while keeping overall totals visible.
- [Legacy rounds lack `complete`] → Treat missing completeness metadata as complete and retain existing visibility behavior.

## Migration Plan

No data migration is required. Deploy the static JavaScript change and regenerate no exports. Rollback consists of restoring the prior round-list rendering logic.

## Open Questions

None.

## Why

The putting export currently drops unsuccessful zone 3 (C1) putts when no distance is recorded, even though the zone identifies them as putting attempts. This undercounts total C1x putts and can inflate the reported success percentage.

## What Changes

- Make unsuccessful zone 3/C1 attempts count toward the C1x total regardless of whether distance is present or within the normal C1x distance range.
- Preserve zone-first precedence for zone 4/C2 attempts, which already count regardless of distance.
- Keep successful zone 3 attempts subject to the existing 12–32 foot C1x distance rule.
- Add regression scenarios covering missing, short, and out-of-range distances on unsuccessful zone 3 attempts.
- Keep distance-based fallback behavior unchanged for records outside zones 3 and 4.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `putting-statistics`: Clarify that an unsuccessful zone 3 attempt always contributes to the C1x total, while successful zone 3 attempts still require a qualifying distance.

## Impact

- `BasketStatisticsExportService` putting classification and aggregation logic.
- Putting-statistics specification and automated regression coverage.
- Exported personal putting totals and percentages may change for players with unsuccessful zone 3 throws lacking qualifying distance.
- No database schema, import format, UI, or external API changes.

## Why

Putting attempts that follow a penalty or other drop can start from a different area than the throw's recorded `zone_id`. The importer already stores `drop_zone_id` and `drop_distance_to_target`, but putting statistics currently omit the drop zone, causing valid C1/C2 attempts to be misclassified or ignored.

## What Changes

- Include `drop_zone_id` in the putting throw projection used by the statistics export.
- Treat a provided `drop_zone_id` as the effective starting area for putting classification.
- Preserve the existing zone-3 bullseye rules when `drop_zone_id` is `3`.
- Continue using `distance_to_target` first and `drop_distance_to_target` as the fallback distance.
- Add regression coverage for drop-zone C1, drop-zone C2, bullseye, and drop-distance cases.
- Update the putting-statistics specification to document drop-zone precedence and behavior.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `putting-statistics`: Use an explicitly provided drop zone as the putting attempt's starting area while retaining existing distance and bullseye rules.

## Impact

- `RoundResultThrowsRepository.PuttingThrowRow` and its JPQL projection.
- Putting classification and aggregation in `BasketStatisticsExportService`.
- Putting statistics unit tests and the OpenSpec putting-statistics delta specification.
- No database migration or import-format change is required; the fields already exist and are populated.

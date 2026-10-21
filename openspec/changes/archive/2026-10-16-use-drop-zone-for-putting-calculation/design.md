## Context

The putting exporter evaluates each throw immediately before a basket throw. It currently selects `zone_id` for zone classification and falls back from `distance_to_target` to `drop_distance_to_target` only for records without a recognized zone. The normalized `round_result_throws` entity already stores `drop_zone_id`, but the repository projection used by the exporter does not select it.

PDGA penalty/drop records can have a throw `zone_id` that describes the pre-drop location while `drop_zone_id` describes the location from which the next putt is actually taken. Ignoring the latter can omit valid C1/C2 attempts.

## Goals / Non-Goals

**Goals:**

- Make `drop_zone_id`, when present, the authoritative starting area for putting classification.
- Preserve the existing zone-3 distance and bullseye rules for both `zone_id = 3` and `drop_zone_id = 3`.
- Preserve the existing zone-4 behavior and distance fallback behavior.
- Keep basket detection and success determination based on the recorded throw timeline.

**Non-Goals:**

- No database schema or import changes.
- No changes to hole completeness, round aggregation, UI, or exported JSON shape.
- No reinterpretation of `drop_zone_id` as the final basket zone.

## Decisions

### Select drop-zone data in the existing putting projection

Add `rrt.dropZoneId as dropZoneId` to `findPuttingThrowRows()` and expose `getDropZoneId()` on `PuttingThrowRow`. This reuses the existing normalized column and keeps classification in the exporter.

### Resolve an effective zone before classification

The classifier will use `drop_zone_id` when it is non-null; otherwise it will use `zone_id`. This makes an explicit drop area authoritative without changing legacy records that have no drop zone.

The effective zone is used only by `puttingZone()` and the special unsuccessful zone-3 count rule. `processHole()` will continue to require the final record's `zone_id` to be the basket zone and will continue to determine success from the next record's `zone_id`.

### Preserve zone-3 bullseye semantics

An effective zone of `3` follows the existing rules exactly:

- distance below 12 feet is a bullseye;
- a successful bullseye attempt contributes neither a C1x success nor total;
- an unsuccessful bullseye attempt contributes one C1x total;
- distances from 12 through 32 feet classify as C1x.

Distance selection remains `distance_to_target` first, then `drop_distance_to_target` when the primary distance is null. Thus a drop-zone-3 record can use its drop distance for bullseye/C1x decisions.

### Verify behavior at the metrics boundary

Tests will exercise `PuttingMetricsBuilder.add()` directly for effective drop zones and the complete putting path for a drop-zone record in an ordered hole. This isolates classification regressions while retaining existing tests for final-zone validation and aggregation.

## Risks / Trade-offs

- [A non-null drop zone may disagree with zone_id] → Treat the explicit drop zone as authoritative only for the starting-area classifier; retain zone_id for basket detection and timeline success.
- [Drop-zone-3 records may lack both distances] → Preserve the existing unsuccessful zone-3 counting rule and omit successful attempts that cannot satisfy C1x distance requirements.
- [Projection and proxy test data can drift] → Add the accessor and update test fixtures together, with explicit coverage for null and non-null drop zones.

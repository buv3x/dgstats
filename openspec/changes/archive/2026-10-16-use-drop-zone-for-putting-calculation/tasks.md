## 1. Expose drop-zone data

- [x] 1.1 Add `rrt.dropZoneId as dropZoneId` to the putting throw repository projection.
- [x] 1.2 Add `getDropZoneId()` to `PuttingThrowRow` and update projection-backed test fixtures to provide it.

## 2. Update putting classification

- [x] 2.1 Resolve the effective zone as `drop_zone_id` when non-null, otherwise `zone_id`.
- [x] 2.2 Apply the effective zone to C1/C2 classification and the unsuccessful zone-3 special case while preserving distance-first/drop-distance-fallback behavior.
- [x] 2.3 Keep `zone_id = 5` as the final basket marker and retain existing adjacent-throw success determination.

## 3. Verify behavior

- [x] 3.1 Add regression coverage for drop-zone C2 classification overriding a C1x distance.
- [x] 3.2 Add regression coverage for drop-zone C1 classification, including successful and unsuccessful bullseye behavior.
- [x] 3.3 Add regression coverage for drop-distance fallback and a drop-zone record in an ordered hole timeline.
- [x] 3.4 Run the relevant compilation and putting-statistics checks and confirm existing classification behavior remains unchanged for null drop zones.

## 1. Putting classification correction

- [x] 1.1 Update the putting aggregation logic so every unsuccessful zone 3 attempt increments the C1x total even when distance is null or outside 12–32 feet.
- [x] 1.2 Preserve successful zone 3 distance classification, including exclusion of successful bullseye and missing-distance attempts.
- [x] 1.3 Preserve unconditional zone 4/C2 classification and distance fallback behavior for all other zones.

## 2. Regression coverage

- [x] 2.1 Add coverage for unsuccessful zone 3 attempts with null, below-12-foot, and above-32-foot distances.
- [x] 2.2 Retain or add coverage for successful qualifying zone 3 attempts, successful bullseye attempts, unsuccessful bullseye attempts, zone 4 attempts without distance, and non-zone distance fallback.

## 3. Export verification

- [x] 3.1 Verify the putting totals and percentages for representative affected player exports.
- [ ] 3.2 Regenerate static personal-statistics output and confirm corrected C1x totals are reflected without changes to unrelated statistics.

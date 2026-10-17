## Context

The putting export evaluates each throw in a valid hole and determines success from the following throw. The current implementation classifies zone 3 records using distance before it knows whether the attempt was successful. As a result, an unsuccessful zone 3 attempt with missing, short, or out-of-range distance is discarded, although the miss itself is still a putting attempt.

Zone 4 already has the desired zone-first behavior: it is counted as C2 regardless of distance. The fix is limited to the zone 3/C1x path and must preserve the existing rules for successful attempts and non-zone-based distance fallback.

## Goals / Non-Goals

**Goals:**

- Count every unsuccessful zone 3 attempt in the C1x total.
- Preserve successful zone 3 classification at 12–32 feet inclusive.
- Preserve zone 4/C2 precedence and all fallback classification for other zones.
- Add regression coverage for the affected distance states.

**Non-Goals:**

- No changes to throw import, database schema, hole validity, or success determination.
- No changes to C2 totals, UI rendering, export shape, or percentage rounding.
- No reinterpretation of successful zone 3 attempts without a qualifying distance.

## Decisions

### Apply success-aware precedence for zone 3

The aggregation step will handle zone 3 with the success flag available. If the attempt is unsuccessful, it increments C1x total immediately, regardless of distance. If it is successful, it continues through the existing distance-based zone 3 rule and only counts a 12–32 foot attempt as C1x successful and total.

This is preferred over changing `puttingZone` to classify all zone 3 records as C1x because a successful bullseye or otherwise unmeasured zone 3 record must remain excluded under the existing contract.

### Keep zone 4 classification unchanged

Zone 4 remains an unconditional C2 classification. This preserves the already-correct precedence for unsuccessful and successful C2 attempts when distance is null.

### Test the boundary and missing-data cases

Regression tests will cover unsuccessful zone 3 attempts with null distance, distance below 12 feet, and distance above 32 feet. Existing tests for successful qualifying zone 3, successful bullseye, unsuccessful bullseye, zone 4, and distance fallback remain required to guard against over-broad classification.

## Risks / Trade-offs

- [Historical exports change for previously omitted zone 3 misses] → This is the intended correction; regenerate affected static personal-statistics data when implementation is applied.
- [A zone 3 record may be marked unsuccessful due to an unusual timeline] → Reuse the existing adjacent-record success determination and change only total-count classification.
- [Tests may rely on private service helpers] → Exercise the behavior through the export path or use the project’s established test strategy rather than exposing new production APIs.

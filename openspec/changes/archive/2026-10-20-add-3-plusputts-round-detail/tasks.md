## 1. Export round-level metric

- [x] 1.1 Extend `PuttingRoundSummary` with an optional `threePlusPuttLostThrows` field and populate it from `PuttingRoundBuilder.threePlusPuttLostThrows`.
- [x] 1.2 Confirm the generated personal-statistics JSON preserves zero for rounds without three-plus-putts and does not alter aggregate or leaderboard fields.

## 2. Render conditional round detail

- [x] 2.1 Update `displayablePuttingRound` so positive three-plus-putt counts keep otherwise empty incomplete rounds visible.
- [x] 2.2 Update `puttingRoundLine` to append `, 3+putts: N` after C2 only for positive numeric values, while tolerating missing legacy fields.
- [x] 2.3 Preserve existing bold labels, comma separation, ordering, incomplete styling, and collapsed round-detail behavior.

## 3. Verification

- [x] 3.1 Compile the affected Java sources and inspect generated/exported data for positive, zero, incomplete, and legacy round cases.
- [x] 3.2 Validate the OpenSpec requirements against the changed exporter and renderer behavior before implementation handoff.

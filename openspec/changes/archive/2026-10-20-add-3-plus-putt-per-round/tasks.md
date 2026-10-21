## 1. Export data and domain model

- [x] 1.1 Extend the `PuttingThrowRow` repository projection and query with `penalty_count`, preserving existing throw ordering and zone/distance fields.
- [x] 1.2 Add nullable `threePlusPuttPerRound` fields to overall and yearly putting-summary records without adding fields to per-round or leaderboard records.

## 2. Three-plus-putt aggregation

- [x] 2.1 Implement initial C1/C2 landing detection using effective drop-zone/zone and distance/drop-distance precedence, with zone IDs 3/4 and the 12–65 foot fallback range.
- [x] 2.2 Implement per-hole lost-throw calculation from the first qualifying landing through the basket, including post-landing penalty counts and the two-throw allowance.
- [x] 2.3 Accumulate lost throws in valid round builders and calculate the 18-hole-weighted value for complete rounds only.
- [x] 2.4 Add overall and yearly averaging using the same complete-round filtering and rounding boundary as `C2+ per round`, while leaving leaderboard aggregation unchanged.

## 3. Personal-statistics UI

- [x] 3.1 Render `3+putt per round` after the existing longest-throw-in line in overall and yearly putting summaries with exactly two decimal places.
- [x] 3.2 Preserve backward compatibility by omitting the line when older personal-statistics data lacks the new field.
- [x] 3.3 Confirm no leaderboard category or leaderboard row consumes or displays the new metric.

## 4. Verification and generated data

- [x] 4.1 Add focused exporter coverage for one-/two-putt finishes, three-plus-putt finishes, penalties, first-landing precedence, distance fallback, incomplete-round exclusion, and 18-hole weighting.
- [x] 4.2 Add data-shape and rendering coverage for overall and yearly placement, two-decimal formatting, missing-field compatibility, and leaderboard exclusion.
- [x] 4.3 Compile the application, regenerate personal-statistics JSON, and inspect representative overall and yearly player files for the new metric.

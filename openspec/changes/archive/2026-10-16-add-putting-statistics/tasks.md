## 1. Throw data access and aggregation

- [x] 1.1 Add a repository projection/query that loads the throw fields required for putting aggregation together with player identity and round-result ownership.
- [x] 1.2 Implement per-round-result, per-hole timeline grouping ordered by `score_ordinal`, omitting holes whose final record is not zone 5.
- [x] 1.3 Implement successful and unsuccessful attempt classification, including zone-first rules, distance fallback, inclusive 12–32 and 33–65 foot ranges, and bullseye handling.
- [x] 1.4 Aggregate C1x and C2 successful/total counts per player and calculate two-decimal percentages with zero-denominator handling.

## 2. Personal statistics export

- [x] 2.1 Merge throw-derived players with existing regression-derived personal players while preserving existing hole-row eligibility and ordering.
- [x] 2.2 Extend personal player snapshot records with C1x and C2 putting summaries and keep throw-only players’ hole-statistics collections empty.
- [x] 2.3 Update player lookup and personal-file generation so players with imported throw data receive selectable personal JSON files.
- [x] 2.4 Preserve existing manifest paths and export diagnostics while ensuring putting-only players are included in the generated personal export set.

## 3. Personal stats interface

- [x] 3.1 Replace the current personal filter layout with a shared player selector labeled `Select a person to view statistics`.
- [x] 3.2 Add second-level `Hole stats` and `Putting stats` tabs that appear after player selection and default to Hole stats.
- [x] 3.3 Keep course and minimum-count filters and the current table behavior only in Hole stats.
- [x] 3.4 Render Putting stats with only the C1x and C2 lines, two-decimal percentages, successful/total counts, and the zero-total em dash representation.
- [x] 3.5 Update styles and accessibility state handling for the nested tabs and hidden pre-selection state.

## 4. Verification and regenerated output

- [x] 4.1 Review representative exported JSON for successful putts, missed C1/C2 putts, bullseye misses, distance fallback, penalty ordinal gaps, invalid holes, and throw-only players.
- [x] 4.2 Run the project compilation check and resolve any export or static-page integration errors.
- [x] 4.3 Regenerate the static statistics export so committed `docs/data` artifacts reflect the new personal putting fields and player lookup eligibility.

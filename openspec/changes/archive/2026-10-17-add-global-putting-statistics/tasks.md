## 1. Extend putting aggregation

- [x] 1.1 Add C2+ attempt tracking to the putting round builder using successful attempts only, effective zone precedence, and inclusive 33-foot distance handling without changing existing C1x/C2 classification.
- [x] 1.2 Track complete-round hole counts and calculate normalized C2+ values only after round completeness has been established.
- [x] 1.3 Add player-level calculations for C2+ per-round average, most C2+ weighted maximum with real/display/tie values, and best C1x round with percentage, total-putt tie-breaking, and tie count.

## 2. Update export contract

- [x] 2.1 Extend `PuttingSummary` with nullable global metric fields while preserving the existing C1x, C2, and per-round fields.
- [x] 2.2 Ensure unavailable metrics serialize as absent or null consistently and that incomplete rounds never contribute to the new global values.
- [x] 2.3 Regenerate personal statistics JSON exports and inspect representative complete, incomplete, 18-hole, and non-18-hole player cases.

## 3. Update Personal stats presentation

- [x] 3.1 Render the three new global putting summaries beside the existing C1x/C2 totals, using exported display values and tie counts.
- [x] 3.2 Keep round-detail rendering limited to existing C1x/C2 fields and preserve current collapsed, incomplete-round, and legacy-file behavior.
- [x] 3.3 Add or adjust styling only as needed for readable global metric lines without changing unrelated Personal stats views.

## 4. Verify

- [x] 4.1 Compile the application and verify the exporter model and aggregation changes compile cleanly.
- [x] 4.2 Validate the generated OpenSpec change and inspect the final export shape against the C2+ threshold, weighting, tie, and incomplete-round rules.

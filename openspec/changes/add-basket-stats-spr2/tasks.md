## 1. Export Calculation

- [x] 1.1 Extend Basket Stats window export data to support optional `spr2`, `spr2Count`, and `spr2CountBucket` fields.
- [x] 1.2 Add weighted regression support for SPR2 using the midpoint-centered 100-rating-point range and linear triangular weights.
- [x] 1.3 Calculate `spr2Count` as the sum of SPR2 weights and derive `spr2CountBucket` from the existing bucket thresholds.
- [x] 1.4 Keep existing SPR, VAR, raw count, and raw count bucket calculation unchanged.
- [x] 1.5 Omit SPR2 fields for exported windows when weighted count is below 50 or weighted rating variance is zero.

## 2. Static Basket Stats Chart

- [x] 2.1 Update Basket Stats chart labels and accessible text to mention SPR and SPR2 without mentioning VAR.
- [x] 2.2 Draw an SPR2 line and point series on the same chart and Y-axis as SPR.
- [x] 2.3 Apply SPR connectivity styles from `countBucket` and SPR2 connectivity styles from `spr2CountBucket`.
- [x] 2.4 Keep low-bucket windows as unconnected points for each metric independently.
- [x] 2.5 Update Basket Stats tooltip rows so hovered SPR and SPR2 points show only metric name, rating, and metric value.
- [x] 2.6 Add distinct SPR2 line and point styling while preserving existing SPR styling.

## 3. Generated Data And Verification

- [x] 3.1 Regenerate static basket statistics data so `docs/data/basket-stats/*.json` includes SPR2 fields where eligible.
- [x] 3.2 Compile the application successfully.
- [x] 3.3 Inspect representative generated Basket Stats JSON to confirm SPR2 value, weighted count, and bucket fields are present when eligible.
- [x] 3.4 Inspect the static chart code paths to confirm Course Stats SPR/VAR behavior remains unchanged.

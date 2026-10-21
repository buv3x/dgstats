## 1. Export weighted distribution data

- [x] 1.1 Extend the Hole stats export model with variation-level bucket presence and overall unweighted percentages for `1-2`, `3`, `4`, `5`, `6`, `7`, and `8+`.
- [x] 1.2 Extend each eligible sliding-window record with triangular-weighted percentages for all seven score buckets, reusing the existing SPRW midpoint, radius, eligibility, and weighted-count logic.
- [x] 1.3 Preserve deterministic bucket ordering and regenerate the exported `docs/data/basket-stats/*.json` files with the new fields.

## 2. Add the Score Distribution view

- [x] 2.1 Add the Score Distribution section, header tooltip, SVG canvas, chart status, and accessible labels below the existing SPRW section in `docs/index.html`.
- [x] 2.2 Add chart rendering for the exported weighted bucket percentages using the same visible-container sizing and rating domain as SPRW, with a fixed 0–100% percentage axis.
- [x] 2.3 Implement bucket presence filtering from full-variation metadata, default visibility for overall percentages greater than 5%, and independent accessible legend toggles.
- [x] 2.4 Implement fixed bucket colors for all series and points: dark blue, light blue, green, yellow, orange, magenta, and dark red in the specified bucket order.
- [x] 2.5 Reuse SPRW confidence bucket behavior for distribution points and segments, including unconnected `50-99`, dotted `100-199`, solid `200+`, and midpoint splitting when confidence changes.
- [x] 2.6 Add distribution point hit-testing and tooltips showing bucket, rating midpoint, and weighted percentage without changing existing SPRW tooltip behavior.
- [x] 2.7 Handle variation changes, empty windows, missing legacy distribution fields, and chart visibility so the new chart follows the existing Hole stats lifecycle.

## 3. Styling and verification

- [x] 3.1 Add CSS for the distribution chart, interactive legend controls, bucket colors, percentage axis, and responsive stacked chart layout while preserving existing SPRW styling.
- [x] 3.2 Compile the application and verify the generated export model and static assets contain the new distribution fields without changing existing SPRW fields.

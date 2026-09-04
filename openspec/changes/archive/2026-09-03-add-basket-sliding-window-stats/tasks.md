## 1. Export Data Model and Paths

- [x] 1.1 Add basket stats output path handling for `docs/data/basket-stats/{courseId}.json`.
- [x] 1.2 Extend manifest course options to include `basketStatsPath` when basket stats data is available.
- [x] 1.3 Add export records for basket stats course snapshots, variation descriptors, and sliding-window points.
- [x] 1.4 Update export diagnostics and result display if generated basket stats file counts need to be reported.

## 2. Sliding-Window Calculation

- [x] 2.1 Group exported rated score samples by basket course, basket, and basket variation for basket stats calculation.
- [x] 2.2 Generate inclusive 50-rating-point windows on a shared 5-point rating grid.
- [x] 2.3 Omit windows with fewer than 50 score samples.
- [x] 2.4 Omit windows that have no rating variance.
- [x] 2.5 Calculate SPR as `-100 * slope` using score-over-rating linear regression for each retained window.
- [x] 2.6 Calculate VAR as average absolute residual from each retained window's regression line.
- [x] 2.7 Assign count buckets `50-99`, `100-199`, and `200+` for retained windows.
- [x] 2.8 Omit basket variations with no retained windows from basket stats files.

## 3. Static Page Navigation and Loading

- [x] 3.1 Add top navigation items for `Course stats` and `Basket stats`.
- [x] 3.2 Keep the current course-level controls, SPR/VAR scatter chart, and table under `Course stats`.
- [x] 3.3 Add Basket stats controls for course selection and basket variation selection.
- [x] 3.4 Load selected course basket stats data from manifest-provided `basketStatsPath`.
- [x] 3.5 Populate the basket variation selector from eligible exported basket stats variations.
- [x] 3.6 Add missing-file, empty-course, and empty-variation states for Basket stats.

## 4. Basket Stats Chart Rendering

- [x] 4.1 Render the Basket stats chart using rating midpoint on the X-axis.
- [x] 4.2 Render SPR as a colored line against the left Y-axis fixed from `-0.5` to `2.0`.
- [x] 4.3 Render VAR as a different colored line against the right Y-axis fixed from `0.0` to `1.5`.
- [x] 4.4 Style line segment portions by count bucket: dotted for `50-99`, normal for `100-199`, and bold for `200+`.
- [x] 4.5 Split line style changes at the midpoint between adjacent points with different count buckets.
- [x] 4.6 Add Basket stats chart hover tooltips showing rating range, midpoint, count, count bucket, SPR, and VAR.

## 5. Generated Data and Verification

- [x] 5.1 Regenerate the static statistics export so manifest and basket stats files match the new contract.
- [x] 5.2 Inspect generated basket stats JSON for expected course, variation, window, SPR, VAR, and count bucket fields.
- [x] 5.3 Manually verify Course stats navigation still shows the existing display.
- [x] 5.4 Manually verify Basket stats course and variation selection renders the dual-axis sliding-window chart.
- [x] 5.5 Manually verify count bucket style changes occur at midpoints between adjacent windows.
- [x] 5.6 Compile the project to confirm Spring export code and templates remain valid.

## 1. Course Stats Chart Threshold

- [x] 1.1 Update the Course stats SPR/VAR chart minimum sample threshold from 10 to 50 matching samples.
- [x] 1.2 Confirm the threshold is applied after Course stats rating bounds filter the selected course samples.
- [x] 1.3 Confirm the Course stats table still displays basket variation rows with one or more matching samples, independent of the 50-sample chart threshold.
- [x] 1.4 Confirm Course stats VAR remains rendered in the scatter chart axis, markers, off-scale arrows, and tooltips.

## 2. Basket Stats SPR-Only Chart Rendering

- [x] 2.1 Rename Basket stats chart title and accessible label text so they no longer mention VAR.
- [x] 2.2 Remove Basket stats VAR line rendering while keeping SPR line rendering and count-bucket connectivity behavior.
- [x] 2.3 Remove Basket stats VAR point rendering and hover targets while keeping SPR point rendering.
- [x] 2.4 Remove Basket stats right-side VAR axis, VAR tick labels, and VAR axis label.
- [x] 2.5 Remove Basket stats VAR legend entry and keep the remaining SPR legend clear.

## 3. Basket Stats Tooltip And Styling Cleanup

- [x] 3.1 Update Basket stats tooltip behavior so only hovered SPR points can produce tooltip rows.
- [x] 3.2 Confirm Basket stats tooltip rows show only metric name, rating, and SPR value.
- [x] 3.3 Remove or leave harmless any now-unused VAR-specific Basket stats constants/styles without changing Course stats VAR styles.
- [x] 3.4 Confirm Basket stats empty, loading, missing-data, and selected-variation states still display correctly.

## 4. Verification

- [x] 4.1 Compile the project to catch Java/resource regressions.
- [x] 4.2 Inspect the static page files to confirm Course stats still references SPR/VAR while Basket stats references SPR only.
- [x] 4.3 Manually verify the implementation against the OpenSpec requirements without running browser tests.

## 1. Course stats rendering lifecycle

- [x] 1.1 Update `docs/app.js` so Course stats data loading retains the selected snapshot without drawing the chart while the Course stats view is hidden.
- [x] 1.2 Update Course stats view activation to render the retained snapshot after the view becomes visible, using the current filters and visible chart container dimensions.
- [x] 1.3 Handle data completion while Course stats is already active so the chart renders once with visible dimensions and does not require a second tab switch.

## 2. Verification

- [x] 2.1 Inspect the initialization and tab-activation paths to confirm no Course stats chart measurement occurs while its view is hidden.
- [x] 2.2 Check the modified JavaScript for syntax or compilation errors and confirm existing Basket stats, filtering, table, and loading behavior remains unchanged.

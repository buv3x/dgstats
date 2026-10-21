## 1. Export yearly putting aggregates

- [x] 1.1 Extend the putting export model with a year-summary type containing the year, yearly metrics, yearly aggregate highlights, and nested round summaries.
- [x] 1.2 Update putting player/round builders to assign dated rounds to calendar-year builders while preserving global aggregation for complete dated and undated rounds.
- [x] 1.3 Finalize year summaries in newest-first order, keep rounds chronologically ordered within each year, and omit undated rounds from year output.
- [x] 1.4 Preserve existing completeness, percentage, C2+ weighting, highlight tie, and displayability semantics for both global and year-scoped aggregates.

## 2. Update export and regression coverage

- [x] 2.1 Add exporter regression coverage for multiple years, year-scoped totals, year-scoped highlights, incomplete rounds, and undated rounds contributing only to global aggregates.
- [x] 2.2 Regenerate the personal statistics JSON files and verify the new yearly structure for representative single-year, multi-year, incomplete-round, and undated-round cases.

## 3. Render year accordions and nested details

- [x] 3.1 Replace the global putting round-details renderer with year-summary rendering while keeping global metrics visible above the year sections.
- [x] 3.2 Render each available year as an independently accessible accordion initialized closed, ordered newest first, with its yearly metrics inside the expanded content.
- [x] 3.3 Add a separate round-details toggle inside each year accordion, initialized closed and limited to that year's displayable rounds.
- [x] 3.4 Preserve incomplete-round styling, empty-incomplete filtering, metric formatting, unique ARIA ids, and keyboard-accessible expanded-state updates.
- [x] 3.5 Keep older total-only personal-statistics files loadable and ensure no global round-details section or unknown-year bucket is rendered.

## 4. Styling and verification

- [x] 4.1 Add or adjust styles for year accordions and nested round-details controls without changing the existing metric and incomplete-round presentation.
- [x] 4.2 Compile the affected Java sources and inspect the generated static data and changed UI files for consistency with the specification.

## Why

Basket statistics are currently selected and aggregated by competition, which makes repeated play on the same manually maintained basket course appear as separate views. The static data file also grows as one global snapshot, so future mapped competitions will increase the initial page download even when the user only wants one course.

## What Changes

- **BREAKING**: Replace the static statistics snapshot contract with a manifest file plus per-basket-course statistics files.
- Change the static Basket Statistics page main selector from competition to basket course.
- Aggregate selected course statistics across all exported competitions that used mappings for that basket course.
- Keep rating bounds as the only user-facing filter besides course selection.
- Do not add or retain a competition filter in the static statistics UI.
- Export basket course identity and names for mapped rated score samples.
- Update export diagnostics and export result messaging to report included basket courses and generated files.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `basket-statistics-export`: Change the export from one competition-indexed snapshot to a course manifest plus course-scoped sample files.
- `static-basket-statistics-page`: Change controls and aggregation semantics from selected competition to selected basket course with no competition filter.
- `project-root-statistics-export-path`: Change the project-root export path contract from a single snapshot file to a manifest and course files under the project `docs/data` directory.

## Impact

- Affects `HoleScoreRepository` statistics export projection and joins.
- Affects `BasketStatisticsExportService` snapshot records, file writing, diagnostics, and export result model.
- Affects `BasketStatisticsExportController` and `basket-statistics-export.html` result display if the result model changes.
- Affects `docs/index.html` and `docs/app.js` static page loading, selector state, empty messages, and aggregation flow.
- Affects generated files under `docs/data`, including replacing or supplementing the existing `docs/data/statistics.json`.
- Requires focused tests or verification for exported JSON shape, file generation, and static page behavior.

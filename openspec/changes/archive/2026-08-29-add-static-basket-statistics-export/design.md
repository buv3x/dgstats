## Context

The application already imports PDGA competition data into a local database and stores player round results with per-hole scores. Basket variation assignment is stored separately at the `round_division` plus `hole_ordinal` level, which lets the system connect imported hole scores to manually maintained physical basket variations without repeating variation data on every score row.

The static visualization stage should not query the database. A local admin action will export a human-readable JSON snapshot into the GitHub Pages `docs/` tree, and the static page will load that snapshot in the browser.

## Goals / Non-Goals

**Goals:**

- Provide a local Spring MVC export page with one Export button.
- Generate `docs/data/statistics.json`, creating the directory when missing and overwriting the file on each export.
- Export only minimal score sample data required for dynamic rating filtering and basket variation aggregation.
- Ignore samples whose player rating is null or whose round-division hole has no basket variation mapping.
- Report export diagnostics after the local export completes.
- Provide a GitHub Pages-ready page at `docs/index.html` with separate CSS and JavaScript files.
- Let users choose a competition, enter optional inclusive rating bounds, and apply the filter with a button.
- Aggregate all rounds for the selected competition in the browser.
- Display basket group rows sorted by basket id, with child basket variation rows sorted by variation id.
- Display row-level sample count, average score, and spreadsheet-style score bucket percentages.

**Non-Goals:**

- Do not add a live statistics API for the static page.
- Do not deploy GitHub Pages or automate publishing.
- Do not change PDGA import behavior.
- Do not change basket course, basket, basket variation, or mapping persistence.
- Do not expose player-identifying data in the exported snapshot.
- Do not add precomputed rating bands or server-side rating-filtered exports.
- Do not add charts beyond the initial table view.

## Decisions

1. Export minimal individual score samples rather than pre-aggregated rows.

   Rating filters are dynamic number inputs with inclusive open-ended bounds, so the static page must be able to recompute statistics for arbitrary ranges. Each exported sample should include the minimum fields needed to group and filter: competition id, basket id, basket label, variation id, variation label with distance in brackets, player rating from `round_result.rating`, and score.

   Alternative considered: export pre-aggregated percentages. That cannot support arbitrary rating bounds without creating many predefined slices, which the UI explicitly does not need.

2. Store the snapshot directly under `docs/data/statistics.json`.

   The static page lives at `docs/index.html`, so `data/statistics.json` is a natural relative fetch path for GitHub Pages. This avoids copying between `src/main/resources` and `docs` and makes the published artifact obvious.

   Alternative considered: export to `src/main/resources/data` and copy to `docs/data`. That was discarded once `docs/data` was accepted as the canonical export location.

3. Keep export local and explicit.

   Add a new local admin page instead of folding export into the existing mapping page. The page should perform the export on POST, then show counts for exported samples, ignored unrated scores, ignored unmapped scores, and included competitions.

   Alternative considered: export automatically during import or mapping saves. Explicit export keeps the static snapshot refresh moment visible and avoids surprising file changes during unrelated local admin work.

4. Keep the static UI file-based and framework-free.

   Implement `docs/index.html`, `docs/styles.css`, and `docs/app.js` as plain static files. The page should fetch `data/statistics.json`, populate the competition select with competition names, and render a table when the user applies filters.

   Alternative considered: use a frontend framework. The initial UI is small enough that a framework would add build and deployment complexity without useful leverage.

5. Compute spreadsheet-style table values in the browser.

   For the selected competition and rating range, group samples by basket and variation. Hide variations and basket groups with zero samples after filtering. Display `Count` as the number of included hole-score samples, `Average` rounded to 3 decimals, and score bucket percentages rounded to 1 decimal for `1-2`, `3`, `4`, `5`, `6`, `7`, and `8+`. Bucket `1-2` includes scores less than or equal to 2; `8+` includes scores greater than or equal to 8.

   Alternative considered: show actual counts in score buckets. The spreadsheet-style display uses percentages, so the first static page should match that behavior.

6. Include snapshot metadata for freshness and diagnostics.

   The JSON should include export time and enough diagnostic metadata to understand what was included and ignored. The static UI should show basic snapshot freshness and should show a clear empty state if the JSON file is missing, cannot be loaded, or the current filter removes all rows.

   Alternative considered: omit metadata to keep the file smaller. The metadata is small and important because static pages otherwise imply live freshness they do not have.

## Risks / Trade-offs

- Large snapshots can slow browser loading -> Export only minimal fields and omit player-identifying data.
- Ignoring unmapped scores can hide mapping gaps -> Report ignored unmapped counts on the local export result and include diagnostics in metadata.
- Arbitrary rating filtering requires client-side computation -> Keep the first table calculation simple and avoid charts or extra analyses in this change.
- Static file fetch can fail when opening `docs/index.html` directly from the filesystem -> Show a clear missing-data state and support normal GitHub Pages/static server usage.
- Basket id ordering may not match real course order -> Use id ordering for now as requested; a future change can add explicit basket ordering.

## Migration Plan

No database migration is required. The change adds local export behavior and static files only. Rollback is deleting the new admin route/service, generated JSON file, and static `docs/` page assets.

## Open Questions

- None blocking. Future changes may add explicit basket ordering, shareable filter URLs, charts, or additional statistics.

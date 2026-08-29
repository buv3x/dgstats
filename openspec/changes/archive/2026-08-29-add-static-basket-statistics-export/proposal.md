## Why

Basket variation mappings now make it possible to analyze scoring by physical basket variation rather than only by imported tournament hole ordinal. The first visualization slice should publish those statistics as static files so the public page can run on GitHub Pages without database access.

## What Changes

- Add a local Spring MVC administration page with one export action for generating basket statistics data.
- Export a human-readable JSON snapshot to `docs/data/statistics.json`, overwriting the previous snapshot.
- Include only competitions that have at least one mapped basket variation score sample with a non-null player rating.
- Export minimal per-hole score samples needed for dynamic client-side rating filtering.
- Ignore score samples with null ratings or without basket variation mappings.
- Report export diagnostics such as exported sample counts, ignored unrated scores, and ignored unmapped scores.
- Add a static GitHub Pages-ready statistics UI at `docs/index.html`.
- Add separate static CSS and JavaScript files under `docs/`.
- Provide competition selection plus inclusive, open-ended rating-from and rating-to number inputs applied by a Filter button.
- Aggregate all rounds for the selected competition and display basket group rows with child basket variation rows.
- Show row-level sample count, average score rounded to 3 decimals, and score bucket percentages rounded to 1 decimal for `1-2`, `3`, `4`, `5`, `6`, `7`, and `8+`.

## Capabilities

### New Capabilities

- `basket-statistics-export`: Local administration and JSON snapshot export for mapped basket variation score samples.
- `static-basket-statistics-page`: Static GitHub Pages-ready UI for displaying basket variation scoring statistics from the exported snapshot.

### Modified Capabilities

- None.

## Impact

- Adds a local export controller/service and repository queries over competitions, round results, hole scores, and basket variation mappings.
- Adds generated snapshot output under `docs/data/statistics.json`.
- Adds static site files under `docs/`, including `index.html`, CSS, and JavaScript.
- Does not change PDGA import behavior, mapping persistence behavior, database schema, or the existing basket course and mapping administration pages.

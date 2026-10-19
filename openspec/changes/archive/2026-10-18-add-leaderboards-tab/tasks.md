## 1. Export model and aggregation

- [x] 1.1 Add leaderboard export records and manifest path metadata for category entries, scopes, rendered values, comparable values, tie counts, and tooltip context.
- [x] 1.2 Extend the putting aggregation model with the round hole-count metadata needed to explain weighted Most C2+ results.
- [x] 1.3 Implement Overall and 2025-through-current-year leaderboard aggregation from complete player rounds, reusing existing C1x, C2+ per-round, Most C2+, Best C1x, and Longest throw-in calculations.
- [x] 1.4 Implement category ranking with unrounded comparisons, category-specific occurrence tie-breakers, alphabetical tied-name ordering, competition places, and top-ten boundary expansion.
- [x] 1.5 Mark Most C2+ entries whose weighted round length changes placement and include the relevant actual hole counts in tooltip metadata.
- [x] 1.6 Write the leaderboard asset during statistics export and expose its relative path in `docs/data/statistics.json`.

## 2. Leaderboards page UI

- [x] 2.1 Add the Leaderboards top-level tab between Personal stats and Description and add the five second-level category tabs.
- [x] 2.2 Add the year selector with Overall selected by default and options from 2025 through the current calendar year.
- [x] 2.3 Load the manifest-referenced leaderboard asset and render the selected category as a headerless place/name/value table.
- [x] 2.4 Render category values using the established formatting, with no tooltips for C1x and C2+ per round and existing contextual tooltips for the other three categories.
- [x] 2.5 Render Most C2+ asterisks and `Round with N holes` tooltip context when round length affects placement.
- [x] 2.6 Add responsive styling and empty/error states consistent with the existing static statistics views.

## 3. Generated assets and verification

- [x] 3.1 Regenerate the static statistics export so the manifest and leaderboard asset are present alongside existing data.
- [x] 3.2 Inspect the generated JSON and affected JavaScript/CSS for valid syntax, expected paths, category scopes, tie behavior, and backward-compatible missing-asset handling.
- [x] 3.3 Compile the application to verify the exporter changes build successfully.

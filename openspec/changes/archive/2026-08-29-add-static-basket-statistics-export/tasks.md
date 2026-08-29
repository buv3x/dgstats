## 1. Export Backend

- [x] 1.1 Add a basket statistics export model for snapshot metadata, competition options, and minimal score samples.
- [x] 1.2 Add repository query support to load mapped hole-score samples with competition, round result rating, score, basket, and basket variation data.
- [x] 1.3 Implement an export service that writes pretty JSON to `docs/data/statistics.json`, creates `docs/data` when missing, and overwrites any previous file.
- [x] 1.4 Ensure the export service ignores null-rated samples and unmapped samples while collecting exported, unrated, unmapped, and included-competition diagnostics.
- [x] 1.5 Ensure exported score samples exclude player-identifying fields and include variation labels with distance in brackets when distance is present.

## 2. Local Export Admin Page

- [x] 2.1 Add a new Spring MVC controller route for the basket statistics export admin page.
- [x] 2.2 Add a Thymeleaf template with one Export button and a clear export-result diagnostics section.
- [x] 2.3 Add navigation to the export page from an existing local admin page where appropriate.

## 3. Static GitHub Pages UI

- [x] 3.1 Add `docs/index.html` with competition select, Rating from input, Rating to input, Filter button, snapshot metadata area, empty states, and statistics table container.
- [x] 3.2 Add separated styling in `docs/styles.css` for the controls, table, basket group rows, variation rows, diagnostics, and empty states.
- [x] 3.3 Add separated behavior in `docs/app.js` to fetch `data/statistics.json`, populate competitions by exported name, and render selected competition statistics.
- [x] 3.4 Implement inclusive open-ended rating filtering using exported `round_result.rating` sample values.
- [x] 3.5 Implement all-round aggregation by basket id and variation id, sorted by basket id then variation id.
- [x] 3.6 Render Count as row-level sample count, Average to 3 decimals, and `1-2`, `3`, `4`, `5`, `6`, `7`, `8+` as percentages to 1 decimal.
- [x] 3.7 Hide variation rows and basket group rows with no samples after filtering, and show a no-results message instead of an empty table.
- [x] 3.8 Show a clear missing-data message when `data/statistics.json` cannot be loaded.

## 4. Verification

- [x] 4.1 Compile the Spring application successfully.
- [x] 4.2 Verify the OpenSpec change status shows all required artifacts complete.

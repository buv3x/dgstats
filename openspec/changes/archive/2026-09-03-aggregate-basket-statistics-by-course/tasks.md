## 1. Export Data Contract

- [x] 1.1 Extend the statistics export repository projection to include basket course id and basket course name for mapped score rows.
- [x] 1.2 Replace the single-snapshot export records with manifest, course option, course snapshot, competition descriptor, score sample, diagnostics, and export result records needed for split output.
- [x] 1.3 Group eligible mapped rated samples by basket course and build one course snapshot per included course.
- [x] 1.4 Keep competition identity in exported course samples and include course-level competition descriptors for provenance.
- [x] 1.5 Write `docs/data/statistics.json` as the manifest and write course files under `docs/data/courses/`.
- [x] 1.6 Ensure export creates both data directories and overwrites the manifest plus included course files.

## 2. Export Administration UI

- [x] 2.1 Update export diagnostics to report exported samples, ignored unrated scores, ignored unmapped scores, included basket courses, and generated course files.
- [x] 2.2 Update the export result model and Thymeleaf template to display the absolute manifest path and generated course file count.
- [x] 2.3 Keep the existing explicit export action and error handling behavior.

## 3. Static Basket Statistics Page

- [x] 3.1 Update `docs/index.html` controls so the main selector is Basket course and no competition filter is present.
- [x] 3.2 Update `docs/app.js` startup to load the `data/statistics.json` manifest and populate the course selector from exported courses.
- [x] 3.3 Load the selected course statistics file from the manifest-provided relative path when the selected course changes.
- [x] 3.4 Recalculate the table using selected course samples and rating bounds only.
- [x] 3.5 Preserve existing basket and variation grouping, sorting, bucket, average, count, and formatting behavior.
- [x] 3.6 Update missing manifest, missing course file, empty course list, and empty filter result messages to use course language.

## 4. Generated Data and Verification

- [x] 4.1 Regenerate the static statistics export so `docs/data/statistics.json` and `docs/data/courses/*.json` match the new contract.
- [x] 4.2 Compile the project to verify the Spring export code and templates are valid.
- [x] 4.3 Manually inspect the static page behavior with the regenerated files to confirm course selection, rating filtering, and course-wide aggregation work without a competition filter.

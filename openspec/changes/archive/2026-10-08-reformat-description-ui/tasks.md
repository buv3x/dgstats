## 1. Embed and reformat Description content

- [x] 1.1 Move the Description navigation button after Personal stats while retaining Description as the initial active view.
- [x] 1.2 Replace the generated Description article content in `docs/index.html` with the reviewed prose as regular headings and paragraphs.
- [x] 1.3 Add safe, identifiable elements for the competition count, player count, and latest competition placeholders, including semantic emphasis markup where needed.
- [x] 1.4 Remove accordion-specific Description DOM generation from `docs/app.js` and remove obsolete accordion styling from `docs/styles.css`.

## 2. Resolve manifest-backed values

- [x] 2.1 Remove the `docs/description.txt` fetch and Description loading/error path from the client script.
- [x] 2.2 Update manifest initialization to populate the embedded Description values from `metadata.description`, using `unavailable` for missing values or a missing manifest.
- [x] 2.3 Preserve the existing statistics-manifest failure messages and behavior for Course stats, Basket stats, and Personal stats.

## 3. Stop exporting the Description asset

- [x] 3.1 Remove Description source/output path constants and the copy operation from `BasketStatisticsExportService`.
- [x] 3.2 Preserve the existing `DescriptionMetadata` fields in `statistics.json` and verify no other export paths depend on the removed copy method.
- [x] 3.3 Remove or leave no longer referenced generated `docs/description.txt` output according to repository export-output conventions.

## 4. Verification

- [x] 4.1 Inspect the static HTML and JavaScript to confirm the four navigation items are ordered correctly, Description remains the default view, and all topics are visible under regular headings.
- [x] 4.2 Inspect placeholder handling for populated, missing, and unavailable manifest metadata, confirming values are inserted as text.
- [x] 4.3 Run the relevant project compilation/check command and confirm the export service and static asset references remain consistent.

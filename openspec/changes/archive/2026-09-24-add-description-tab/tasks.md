## 1. Export contract

- [x] 1.1 Extend the statistics export query/model to retain competition start-date information needed to identify the latest included competition.
- [x] 1.2 Add explicit manifest metadata for included competition count, included player count, and latest included competition using the defined rated-and-mapped export scope.
- [x] 1.3 Preserve existing manifest fields, export diagnostics, relative paths, and player-identifying data boundaries.

## 2. Description source and static asset

- [x] 2.1 Verify `description/description.txt` contains the reviewed wording, placeholders, headings, and supported formatting markers.
- [x] 2.2 Make the description source available to the static `docs/` page at a relative published path during the export/build workflow.
- [x] 2.3 Ensure the asset remains readable when the page is opened from the repository's GitHub Pages context.

## 3. Description tab UI

- [x] 3.1 Add the Description navigation button before the existing statistics tabs and add its content section.
- [x] 3.2 Update page initialization and view selection so Description is active and visible by default.
- [x] 3.3 Preserve Course stats, Basket stats, and Personal stats navigation and their existing loading behavior.
- [x] 3.4 Add Description view styling consistent with the existing page typography, spacing, headings, and messages.

## 4. Description rendering

- [x] 4.1 Load the description asset with a relative fetch and show loading, missing-asset, and missing-manifest states without blocking other views.
- [x] 4.2 Replace the supported bracketed placeholders from manifest metadata, including deterministic unavailable values for missing fields.
- [x] 4.3 Render headings, paragraphs, `<italic>`, and `<bold>` safely without injecting arbitrary executable HTML.
- [x] 4.4 Keep unknown placeholders and unsupported markup deterministic and readable.

## 5. Verification

- [x] 5.1 Compile the application and confirm the export service changes compile successfully.
- [x] 5.2 Inspect the generated manifest shape and confirm the Description metadata is present alongside existing metadata.
- [x] 5.3 Review the static asset paths and source references for consistency with the Description tab requirements.

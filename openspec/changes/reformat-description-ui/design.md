## Context

The static page currently puts Description first in the navigation, fetches `docs/description.txt`, parses Markdown-like headings in `docs/app.js`, and renders each heading as a native `details` accordion. The export service creates `docs/description.txt` by copying the repository description source, while the statistics manifest already contains the three values needed by the Description placeholders.

The requested change spans the static page and the export workflow. The page must keep its manifest-driven statistics behavior, but Description itself should be self-contained HTML and no longer be a generated export asset.

## Goals / Non-Goals

**Goals:**

- Place Description last in the top-level navigation while retaining it as the default view.
- Embed the reviewed Description prose in `docs/index.html` using ordinary headings and paragraphs.
- Resolve only the competition count, player count, and latest competition from the loaded statistics manifest.
- Remove accordion behavior, Description text fetching, and Description asset copying.
- Preserve readable fallback values when manifest metadata is missing.

**Non-Goals:**

- Changing the Description wording or the statistics calculations.
- Changing the manifest metadata schema or adding an API.
- Changing Course stats, Basket stats, or Personal stats navigation behavior beyond the Description item's position.
- Removing the repository-maintained description source file; it remains available as editorial source unless separately retired.

## Decisions

### Embed semantic Description markup in the page

Place the reviewed Description content directly inside the existing `description-content` article in `docs/index.html`. Use `h2` elements for top-level topics and `p` elements for prose. Represent supported emphasis with actual `em` and `strong` elements instead of carrying parser-specific marker syntax into the page.

This keeps the published page self-contained and makes the intended visual structure explicit. An alternative would be to keep fetching a text or HTML asset, but that would retain a second publishable asset and the failure/loading path that the change is intended to remove.

### Resolve placeholders after manifest loading

Give the three dynamic values dedicated elements or stable placeholder attributes in the embedded markup. When `statistics.json` loads, update those elements with text content from `manifest.metadata.description`, using `unavailable` when a value is absent. If the manifest fails, the embedded prose remains visible and its dynamic values use the same fallback.

This reuses the existing manifest request and avoids parsing arbitrary HTML or injecting generated strings into the page.

### Remove the Description asset export step

Delete the export service's Description source/output path constants and `copyDescriptionAsset()` invocation and method. The export continues to write `statistics.json` with its existing `DescriptionMetadata`, because the page still needs those values.

The generated `docs/description.txt` file should no longer be produced by future exports. Existing generated files can be removed as part of implementation if the repository's normal export-output policy permits it; otherwise they are simply unused legacy output.

### Keep Description as the initial view

Change only the DOM order of the navigation button. Retain `setActiveView("description")` and the initial Description visibility so the page still opens with the overview, now located at the last menu position.

## Risks / Trade-offs

- [Risk] Embedded prose can drift from `description/description.txt` → Copy the reviewed content deliberately during implementation and document the HTML page as the published source for this view.
- [Risk] Existing browser tests may expect a fetched Description asset or accordion elements → Update them to assert embedded headings, regular paragraphs, and manifest placeholder replacement.
- [Risk] A failed manifest request can leave dynamic values unresolved → Initialize embedded placeholder elements to `unavailable` and update them only after valid metadata is available.
- [Risk] Removing the export copy step may leave stale `docs/description.txt` in existing deployments → Ensure the page no longer references it and document that it is obsolete generated output.

## Migration Plan

1. Replace the Description navigation and rendering structure in the static page.
2. Remove Description fetching/parsing and update manifest handling for embedded placeholders.
3. Remove the export copy step and update export tests.
4. Verify the generated page and export output; remove the obsolete generated asset if required by repository conventions.

Rollback consists of restoring the previous navigation, fetch/parser, and export-copy code. The manifest metadata remains compatible in either version.

## Open Questions

- None blocking. The proposal assumes Description remains the default view because the request changes menu position but does not request a default-view change.

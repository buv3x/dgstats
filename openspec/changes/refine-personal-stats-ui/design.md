## Context

The GitHub Pages-ready statistics UI in `docs/` loads exported personal variation rows and currently re-sorts them in the browser by course, basket, and variation order. The export already provides calculated ratings, including the unrounded value used by the backend ordering. The page header also renders manifest `exportedAt` metadata even though the Description view now identifies the latest included competition.

## Goals / Non-Goals

**Goals:**

- Remove the redundant export timestamp from the visible page header.
- Make the Personal stats table display the highest-rated variation rows first.
- Keep filtering behavior and deterministic tie-breaking intact.

**Non-Goals:**

- Do not change personal rating calculations, exported JSON, manifest metadata, or backend ordering.
- Do not change the Description content or its latest-competition placeholder.
- Do not alter course, basket, or course-statistics ordering outside the Personal stats table.

## Decisions

- Remove the `snapshot-meta` element and the associated metadata rendering path from the static page. This removes the UI contract rather than leaving an empty layout placeholder. Keeping the manifest field is unnecessary for this change because it may still be consumed by other tooling.
- Update the browser-side `comparePersonalRows` comparator to compare the exported decimal personal rating in descending order first. Use course, basket, and variation ordering as deterministic tie-breakers. This aligns client-side filtering with the existing backend/export ordering and avoids relying on rounded display values.
- Keep `personalFilteredRows` sorting after filtering so every active course/minimum-count filter re-establishes the same rating-first order.

Alternatives considered:

- Sorting only the exported files was rejected because the browser currently sorts filtered rows and would continue to override that order.
- Sorting by displayed rounded rating was rejected because distinct decimal ratings can round to the same integer; the unrounded exported value provides stable best-to-worst ordering.

## Risks / Trade-offs

- [Risk] Existing consumers may rely on the header metadata element for styling or scripting → Mitigation: restrict the change to the page UI assets and verify the page remains usable without the element.
- [Risk] Rows with equal ratings could appear unstable → Mitigation: retain deterministic course, basket, and variation tie-breakers.


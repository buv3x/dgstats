## Context

The public statistics experience is a static page composed of `docs/index.html`, `docs/app.js`, and `docs/styles.css`. The requested terminology is currently rendered directly in `docs/index.html`; JavaScript uses the existing element IDs and exported basket-oriented field names to load and calculate data.

This is a presentation-only change. The repository also contains unrelated uncommitted source and generated-data changes, so implementation must be restricted to the UI wording and its focused verification.

## Goals / Non-Goals

**Goals:**

- Use `LT PDGA Statistics` consistently in the browser title and global header.
- Use Course, Hole, and Hole Layout terminology in the relevant filters, tabs, table headings, chart heading, and table-header tooltip/accessibility descriptions.
- Preserve all existing selectors, data contracts, calculations, navigation behavior, and exported files.

**Non-Goals:**

- Renaming JavaScript variables, DOM IDs, JSON properties, URLs, backend entities, or internal domain terminology.
- Changing descriptive prose outside the requested labels and table-header descriptions.
- Renaming administrative pages or backend/export screens.

## Decisions

### Keep internal identifiers stable

Update only user-facing text nodes and tooltip/accessibility strings in `docs/index.html`. Existing IDs such as `basket-stats-tab`, `basket-course`, and `basket-variation` remain stable because they are implementation details used by `docs/app.js`; changing them would add regression risk without improving the visible terminology.

### Update the existing static-page contract

Create a delta spec for `static-basket-statistics-page`, modifying the existing navigation, tooltip, and terminology requirements. This keeps the archived requirement history intact while making the new labels testable.

### Treat tooltip and accessible text as one terminology surface

Where the Course stats table describes a basket layout, update both `data-tooltip` and `aria-label` values. The visible header becomes `Hole / Layout`, and descriptions use `hole layout` / `Hole` consistently so pointer and keyboard users receive the same terminology.

### Verify presentation without regenerating data

Verification should inspect the rendered/static HTML and, where available, run the existing project checks. No statistics export regeneration is needed because no data or calculation behavior changes.

## Risks / Trade-offs

- [Stale basket wording remains in a requested UI location] → Search `docs/index.html` for the explicitly replaced phrases and review all changed labels, including both tooltip attributes.
- [A label edit accidentally changes behavior] → Preserve all element IDs, form names, and JavaScript; limit the diff to visible text and tooltip/accessibility attributes.
- [Unrelated working-tree changes are included] → Review the final diff and status, and touch only the new OpenSpec artifacts during proposal creation and only `docs/index.html` during implementation.

## Migration Plan

No data migration or deployment migration is required. Deploy the updated static HTML with the existing JavaScript and data assets. Rollback is a one-file revert of `docs/index.html` if the terminology needs to be reconsidered.

## Open Questions

None. The requested labels and scope are explicit.

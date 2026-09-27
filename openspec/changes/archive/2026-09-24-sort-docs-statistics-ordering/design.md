## Context

The database now has nullable decimal `sort_order` columns on `datas.basket`
and `datas.basket_variation`, but the Java entities and static export do not
expose them. The public page currently reconstructs groups in JavaScript and
explicitly sorts baskets and variations by ID. Its Course stats and Basket stats
course selectors use manifest order, while the Personal stats course selector
sorts alphabetically.

The requested scope is limited to the static `docs/` pages. The server-rendered
administration pages and their course selectors are not part of this change.

## Goals / Non-Goals

**Goals:**

- Apply nullable basket and variation `sort_order` values to all relevant
  public statistics displays and selectors.
- Provide deterministic fallback ordering when `sort_order` is null or tied.
- Sort every public course selector by total exported course result count,
  descending, without showing that count in its label.
- Keep existing statistics calculations, labels, filters, and admin UI behavior
  unchanged.

**Non-Goals:**

- Do not change the database schema or add an admin editor for sort order.
- Do not reorder server-rendered admin course, basket, or variation selectors.
- Do not expose result counts in the visible course option text.
- Do not change Personal stats' rating calculations or score ordering.

## Decisions

### Map decimal ordering values as nullable numeric fields

Add nullable `BigDecimal` fields to `Basket` and `BasketVariation`. Export
projections will read both values from the existing statistics query. Explicit
sort order is ascending; null values sort after non-null values; ID is the final
tie-breaker.

Alternative considered: keep ordering only in database repository methods.
Rejected because the public static page is driven by JSON and cannot see
server-side ordering after export.

### Export ordering metadata with static records

Include basket and variation sort-order values in the exported sample and
variation records needed by `docs/app.js`. This makes ordering available after
client-side filtering and grouping, including Course stats charts/tables,
Basket stats selectors, and Personal stats rows.

Alternative considered: rely only on array order in the export files. Rejected
because the client currently reconstructs Maps and applies ID-based sorts, and
because Personal stats files combine multiple courses.

### Use a shared public comparator

The client will use one comparator for nullable explicit order values followed by
ID. Course selectors will use manifest `sampleCount` descending, then course
name and ID for deterministic ties. The Personal stats course selector will map
its course IDs to manifest course options so it uses the same total-result count
as the other selectors.

Alternative considered: display the count to explain the ordering. Rejected by
the requirement; the count remains a hidden sorting value.

### Preserve existing personal row semantics where no sort order applies

Personal rows will continue to respect their existing exported rating order for
otherwise equivalent items, while explicit basket/variation order takes
precedence where the UI is grouping or listing those entities. Existing score
chronology and rating calculations remain unchanged.

## Risks / Trade-offs

- [Older JSON files lack sort-order fields] -> Treat missing values as null and
  fall back to the existing ID order, so old snapshots remain readable.
- [Sort order values are tied or sparse] -> Use null-last ordering and ID
  fallback for deterministic output.
- [Export payload grows because order values repeat in samples] -> Keep fields
  numeric and limited to the records already loaded by the static page; avoid
  adding unrelated database fields.
- [Course sample counts differ from all database scores] -> Define the count as
  `CourseOption.sampleCount`, the total rated/mapped samples represented in the
  public export, and use it consistently across all public selectors.

## Migration Plan

1. Map sort-order fields and extend the export projection/records.
2. Regenerate the static export files so current public data has ordering
   metadata.
3. Update the public JavaScript comparators and all three course selectors.
4. Verify old snapshots without ordering fields still use ID/name fallbacks.

Rollback is a source revert. The client remains backward-compatible with
snapshots that do not contain the new optional fields.

## Open Questions

- None for the requested scope. The public course-result count is defined as
  the exported rated/mapped sample count.

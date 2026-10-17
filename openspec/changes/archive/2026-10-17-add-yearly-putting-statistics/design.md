## Context

The Personal stats putting view currently receives one `PuttingSummary` per player. It contains global C1x/C2 metrics, global aggregate highlights, and one flat list of round summaries. The exporter already has round dates and complete/incomplete state, so the year dimension can be added at the existing round-aggregation boundary without changing persistence or the putting classification rules.

The requested presentation keeps the global metrics visible, groups dated rounds by calendar year, and places each year's round-details control inside that year's accordion. Rounds without a date remain part of global calculations when eligible but are not assigned to a displayed year.

## Goals / Non-Goals

**Goals:**

- Produce year-scoped putting metrics using only complete rounds with a non-null round date in that year.
- Preserve global totals and global aggregate highlights, including contributions from complete rounds without a date.
- Export dated year summaries with their displayable round summaries nested under the corresponding year.
- Render every year accordion closed initially, with year-level round details also collapsed initially.
- Keep incomplete rounds with recorded attempts available in the correct year's details and continue omitting empty incomplete rounds from display.

**Non-Goals:**

- Changing putting-zone classification, completeness detection, eligibility, or percentage calculations.
- Creating an unknown-year bucket or inventing a year for undated rounds.
- Adding year filters, charts, persistence, database migrations, or external API endpoints.
- Changing personal basket-variation statistics.

## Decisions

### Build year summaries during export

Extend the putting export model with a list of year summaries. Each summary contains the calendar year, C1x/C2 metrics, C2+ per-round metric, most-C2+ round, best-C1x round, and the round summaries for that year. The existing player-level builder continues to aggregate all complete rounds for global metrics, including rounds whose date is null, while a dated round is also added to its year builder.

This keeps yearly percentages and highlight calculations authoritative and consistent with global calculations. Client-side regrouping was rejected because it would duplicate aggregation rules and could not reliably reconstruct weighted C2+ and highlight tie behavior from rendered data.

### Use calendar year from the round date

The exporter assigns a round to `roundDate.getYear()` only when the date exists. Year summaries are sorted newest first; rounds within a year retain the existing chronological ordering. Undated rounds are not emitted as members of any year and therefore have no year-level detail presentation, but remain in the internal global round collection so complete undated rounds continue contributing to global aggregates.

### Nest round details under each year

The new export shape places round summaries under their year summary rather than under the top-level putting object. The UI renders the year summary as a native disclosure element or equivalent accessible accordion, initially closed. Within the opened year content, it renders that year's metrics followed by a separate show/hide round-details control, initially collapsed. A year with no displayable rounds has no empty round-details control.

Older files without year summaries remain loadable as total-only putting data; legacy top-level round data is not rendered as a new global details section.

### Reuse existing displayability and metric formatting

The existing `displayablePuttingRound`, incomplete styling, metric formatting, and accessibility conventions remain in force. The same helpers are applied to each year's round list so incomplete rounds with attempts remain italicized and struck through, while empty incomplete rounds are omitted.

## Risks / Trade-offs

- [Personal JSON files change shape and become larger] → Regenerate all exported files and keep the UI tolerant of older total-only files.
- [Undated rounds become unavailable in visible round details] → Preserve them in global aggregation and make the omission explicit in the export and specification; do not create a misleading synthetic year.
- [Year-level highlights could diverge from global highlights] → Calculate each year from the same complete-round builders and comparison methods used globally, scoped before finalization.
- [Nested disclosures can create confusing accessibility state] → Give every year and round-details control a unique id, correct `aria-expanded`, and matching `aria-controls`; initialize all controls closed.

## Migration Plan

1. Extend exporter models/builders and tests for dated year summaries while preserving global aggregation.
2. Update the static renderer and styles, then regenerate `docs/data/personal-stats/*.json`.
3. Verify representative players with multiple years, one year, incomplete rounds, and undated rounds.
4. If rollback is needed, revert the exporter/UI changes and regenerate the previous personal-statistics files; no database migration is required.

## Context

The putting renderer currently creates an accordion for every exported dated year. The exporter intentionally retains incomplete rounds for auditability, so some years contain only incomplete rounds and display yearly metrics with no complete statistical basis. The existing JSON already exposes `round.complete`, making this a presentation-only filter.

## Goals / Non-Goals

**Goals:**

- Display a year accordion only when that year contains at least one round whose `complete` field is `true`.
- Preserve the existing global totals and all exported year/round data.
- Keep incomplete rounds with attempts visible inside an eligible year.
- Show no year accordions when the player has no eligible year.

**Non-Goals:**

- Changing exporter aggregation, JSON shape, or completeness calculation.
- Requiring non-zero C1x/C2 metrics for a year to be visible.
- Removing incomplete rounds from an eligible year's round details.
- Changing global totals or the existing empty-incomplete-round display filter.

## Decisions

### Filter at render time

Filter `putting.years` in `renderPuttingStatistics` before creating DOM elements. A year passes when `year.rounds` contains at least one round with `complete === true`. This preserves the complete export as the source of truth and avoids regenerating hundreds of player files for a UI-only decision.

Filtering in the exporter was rejected because incomplete rounds remain useful evidence and may be displayed alongside complete rounds in an eligible year.

### Treat explicit completeness strictly

Only an explicit boolean `true` qualifies a year. Legacy or malformed round entries without `complete: true` do not make a year visible. This matches the requirement that the year have at least one round with complete statistics.

### Keep global content independent

The global C1x/C2 totals and global aggregate highlights remain rendered exactly as before. If no years qualify, the page displays those global metrics without an empty year container or replacement message.

## Risks / Trade-offs

- [Legacy files may have rounds without completeness metadata] → Treat them as non-qualifying for year visibility while continuing to load global totals safely.
- [A year with one complete zero-attempt round may look sparse] → Count `complete: true` rather than metric totals, because completeness—not putting volume—is the defined eligibility criterion.
- [Exported data contains hidden incomplete-only years] → Keep this intentional and documented; the UI is filtering presentation, not deleting evidence.

## Migration Plan

No data migration or export regeneration is required. Deploy the renderer change and validate players with complete-only, mixed, incomplete-only, and no-year data. Rollback is a one-file JavaScript revert.

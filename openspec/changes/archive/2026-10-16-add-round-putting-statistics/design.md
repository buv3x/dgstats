## Context

The personal statistics export currently builds one `PuttingSummary` per player. The putting query returns player and throw fields but not the round metadata needed to explain where those attempts occurred. The static Personal stats page renders the two global metrics as plain text and has no detail disclosure.

The existing putting classification rules, including the separate total-counting correction, remain the source of truth. This change adds a second aggregation level and presents it without removing the overall summary.

## Goals / Non-Goals

**Goals:**

- Aggregate putting attempts per player and per round while retaining overall totals.
- Carry competition simple name and abbreviated round label from the round relationship into the export.
- Recalculate each aggregate percentage from its own successful and total counts.
- Display totals immediately and round details behind a collapsed, accessible show/hide control.
- Apply consistent semantic bold formatting to competition names, round labels, `C1x`, and `C2` labels.

**Non-Goals:**

- Changing putting classification, success determination, percentage rounding, or eligibility rules.
- Adding database tables, migrations, or a public API endpoint.
- Adding round filters, sorting controls, charts, or per-hole putting details.
- Changing the existing personal basket-variation statistics.

## Decisions

### Extend the putting export model with round entries

Add a list of round summaries to `PuttingSummary`, with each entry containing a stable round identity, competition simple name, abbreviated round label, and C1x/C2 metrics. The exporter will group throws by the round-result's round, while the existing player-level builder continues to maintain total counters.

This keeps the JSON self-contained for the static page and avoids client-side reconstruction from unrelated score samples. A round entry is emitted when that player has at least one classified putting attempt in the round; metrics with no attempts remain zero/undefined within that entry.

### Enrich the putting projection at the query boundary

Extend `PuttingThrowRow` and its JPQL projection by joining `rr.roundDivision.round` and `r.competition`. Project round ID, competition simple name, and abbreviated round label alongside the existing throw fields. The grouping key uses round ID rather than display labels so duplicate or missing labels cannot merge distinct rounds.

### Sum counts, then calculate percentages

Use the existing `PuttingMetric` value shape for both total and round metrics. Each builder calculates a percentage only after its own counters are complete. The overall percentage is therefore based on total successful/attempt counts, and a round percentage is based only on that round's counts.

### Render semantic disclosure content in the static page

The putting summary will render overall C1x and C2 lines first, followed by a native disclosure link/button whose details container is hidden initially. JavaScript will toggle the details and update `aria-expanded` and the control text. DOM nodes will be created with text content for dynamic values; labels and descriptors use `strong` elements so the requested bold formatting is semantic and safe.

The displayed line format will be:

```text
<bold C1x>: percentage (successful / total), <bold C2>: percentage (successful / total)
```

Round lines additionally prefix the same metric pair with bold competition and round text.

### Preserve backward tolerance for older exported files

The page will treat a missing or empty round list as having no round details and continue rendering the existing total metrics. This allows a page to load older personal JSON files while new exports are rolled out.

## Risks / Trade-offs

- [Exported personal JSON files become larger] → Store only one compact metric pair per round and keep round details in the existing per-player file rather than duplicating throw data.
- [Competition or round labels may be blank in imported data] → Use the existing display-fallback convention when constructing exported descriptors so each round remains identifiable.
- [Averages could be misread as additive] → Define and test percentages from summed counts, never by averaging percentages.
- [Older files lack round data] → Keep the UI total-only fallback and hide the detail control when no round entries exist.

## Migration Plan

No database migration is required. Deploy the exporter and static-page changes together, regenerate the personal statistics export, and commit the resulting JSON files. If rollback is needed, revert the application/static files and regenerate the prior export format; the UI remains tolerant of files without round details.

## Open Questions

- None for the requested behavior. Round entries are ordered chronologically by round date when available, then by round ID, with stable competition/name fallbacks for ties.

## Context

The Personal putting view is rendered by `docs/app.js` and styled by `docs/styles.css`. Aggregate C1x and C2 metrics currently share a flex row, while round details inherit the summary's larger font size. The export already calculates the best C2+ and C1x round values, but their DTOs expose only the occurrence count; the UI therefore cannot identify the rounds behind a highlight.

## Goals / Non-Goals

**Goals:**

- Make aggregate C1x and C2 values scan as separate statistic rows.
- Reduce round-detail typography to 14px without changing its content, ordering, emphasis, or incomplete-round styling.
- Provide accessible, hoverable/focusable tooltip triggers for the complete displayed value of each putting highlight.
- Supply the matching round descriptors for each highlight at both overall and yearly aggregation levels.
- Preserve compatibility with exports that omit the new round-list fields.

**Non-Goals:**

- Changing putting calculations, tie-breaking, rounding, or which rounds contribute to aggregates.
- Changing the existing round-detail expand/collapse interaction.
- Adding a new tooltip library or changing tooltip behavior elsewhere on the page.
- Changing database tables or public service endpoints.

## Decisions

### Store matching round descriptors in highlight DTOs

Extend `MostC2PlusRound` and `BestC1xRound` with ordered lists of matching round descriptors. Build those lists from the same complete rounds used to calculate each highlight, preserving the existing aggregation and tie rules. A descriptor should contain the competition simple name and abbreviated round label, with the round id/date available if needed to keep identities distinct.

This keeps the UI from re-deriving statistical matches and ensures the tooltip describes exactly the rounds used by the exporter. Recomputing matches in JavaScript was rejected because it would duplicate weighted C2+ and cross-multiplication tie logic.

### Reuse the existing UI tooltip mechanism

Render the complete highlight value as a `.tooltip-trigger` with `tabindex="0"`, an accessible `aria-label`, and a `data-tooltip` string containing the matching round list. The existing delegated pointer and focus handlers will provide hover and keyboard behavior.

Making the whole value the trigger—not only the occurrence suffix—keeps the relationship between the statistic and its explanation clear. A trigger is rendered whenever the highlight exists and has at least one matching round, including a single occurrence.

### Keep display formatting separate from tooltip content

Retain the current visible formatting, including omission of the `(1 times)` suffix and the existing pluralized suffix for multiple occurrences. The tooltip is additional context and does not replace or alter the displayed statistic.

### Use layout and typography CSS changes only

Change aggregate total layout to a block presentation and set round-detail typography to 14px. Keep round lines inline internally so the established `[competition] [round]: C1x, C2` format remains unchanged.

## Risks / Trade-offs

- [Older JSON exports lack matching-round lists] → Render the existing visible values without a tooltip when no list is available; current pages remain readable.
- [Long competition names can produce large tooltips] → Reuse the existing bounded, viewport-aware tooltip positioning and provide one round per line in the tooltip text.
- [Round identity may be ambiguous if only labels are shown] → Generate descriptors from the existing competition/round labels and retain round identity in the export data; use date/id as a fallback or disambiguator when labels repeat.
- [Tooltip text is plain text] → Keep it keyboard accessible through the existing focus handling and use an explicit accessible label describing the statistic and its rounds.

## Why

The Description view explains hole-level personal statistics but does not document the recently available putting statistics. Users need a clear explanation of data completeness, metric definitions, and yearly/overall aggregation rules when interpreting the Putting stats view.

## What Changes

- Rename the Description view section heading from `Personal Stats` to `Personal Stats. Hole Stats`.
- Add a `Personal stats. Putting stats` subsection after the hole-statistics paragraphs.
- Document that putting statistics use player-entered PDGA Live data and require complete statistics for every hole in a round.
- Document the definitions and scope of C1x, C2, C2+ per round, Most C2+ in a round, Best C1x round, Longest throw-in, and Lost 3+putting per round.
- Preserve the existing Future Improvements section for a later change.

## Capabilities

### New Capabilities

### Modified Capabilities

- `static-basket-statistics-page`: Add the putting-statistics description and update the existing Personal Stats section heading.

## Impact

- `docs/index.html` description content only.
- No changes to putting calculations, exported data, navigation behavior, APIs, or the existing Future Improvements section.

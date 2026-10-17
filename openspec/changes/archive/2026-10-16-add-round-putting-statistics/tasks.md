## 1. Round-aware putting export model

- [x] 1.1 Extend the putting throw projection with round ID, competition simple name, and abbreviated round label by joining round-result, round, and competition data.
- [x] 1.2 Add round descriptor and round-summary records to the personal putting export model while preserving the existing overall C1x/C2 metrics.
- [x] 1.3 Group each player's classified putting attempts by round identity and maintain separate C1x/C2 counters for every group.
- [x] 1.4 Order exported round summaries deterministically by round date when available and round identity as the fallback, using safe display fallbacks for missing labels.
- [x] 1.5 Calculate total and per-round percentages from their respective successful and total counters without changing existing classification or rounding rules.

## 2. Static putting details UI

- [x] 2.1 Update the putting summary markup/styles to support total metric lines, a round-details disclosure control, and a hidden details container.
- [x] 2.2 Render total and round metric lines with semantic bold elements for `C1x`, `C2`, competition names, and round labels, using text content for dynamic values.
- [x] 2.3 Add collapsed-by-default show/hide behavior for round details and keep `aria-expanded` and control text synchronized.
- [x] 2.4 Preserve total-only rendering when loading older or incomplete personal statistics files without round aggregates.

## 3. Export handoff and verification

- [x] 3.1 Regenerate representative personal statistics output so round aggregates are present alongside overall totals.
- [x] 3.2 Compile the affected application code and confirm the generated export model and static assets remain buildable without changing unrelated statistics.

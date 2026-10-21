## Context

Personal putting statistics are rendered by shared helpers in `docs/app.js`. C1x and C2 are emitted as bold labels by `puttingLine`, while the global and yearly highlight metrics use `puttingValueLine`. The page already has a shared `.tooltip-trigger` pattern with delegated pointer and focus handlers, so the change only needs to make putting labels capable of rendering either a complete tooltip-enabled label or a tooltip-enabled substring.

## Goals / Non-Goals

**Goals:**

- Reuse the existing tooltip behavior and styling for all requested putting metrics.
- Apply the C1x and C2 definitions consistently to overall, yearly, and round-detail metrics.
- Make only `C2+` and `3+putting` interactive within their longer labels.
- Preserve visible label text, metric values, calculations, and accessibility semantics.

**Non-Goals:**

- No changes to putting calculations, JSON exports, APIs, leaderboards, or tooltip positioning behavior.
- No tooltip additions to unrelated putting highlights such as Most C2+, Best C1x round, or Longest throw-in.

## Decisions

- **Use the existing delegated tooltip contract.** Put the requested text in `data-tooltip`, use the existing `tooltip-trigger` class, keyboard focusability, role, and accessible label behavior. This avoids introducing a second tooltip implementation.
- **Centralize metric-label rendering.** Extend or adapt the shared putting label helpers so C1x and C2 cannot diverge between overall, yearly, and round-detail rendering.
- **Support label fragments explicitly.** Render the `C2+` fragment as its own tooltip trigger before the literal ` per round`, and render the `3+putting` fragment as its own trigger within `Lost 3+putting per round`. The complete visible labels remain unchanged.
- **Keep tooltip text exact.** Use the requested strings verbatim, including punctuation, capitalization, plus signs, and distance ranges.

## Risks / Trade-offs

- [Risk] Partial-label rendering could accidentally change spacing or accessible names → Keep surrounding text as separate text nodes and add focused UI checks for the complete visible labels and trigger text.
- [Risk] Existing dirty changes in `docs/app.js` may overlap the implementation → Apply the change narrowly around the putting render helpers and preserve unrelated worktree modifications.
- [Risk] Tooltip triggers may be difficult to discover visually on short labels → Reuse the existing dotted underline and focus styling already required by the page tooltip contract.

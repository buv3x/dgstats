## Why

The Personal stats putting view exposes several domain-specific labels without explaining their distance ranges or counting rules. Adding consistent, accessible tooltips will make C1x, C2, C2+, and lost three-plus-putting metrics understandable without changing their values or data semantics.

## What Changes

- Add explanatory custom tooltips to C1x and C2 metric labels wherever they appear in Personal stats putting summaries and round details.
- Add a tooltip to only the `C2+` portion of the `C2+ per round` label.
- Add a tooltip to only the `3+putting` portion of the `Lost 3+putting per round` label.
- Ensure the new tooltip triggers retain the existing hover, keyboard-focus, visual-indicator, and accessible-label behavior.
- Keep putting calculations, exported fields, displayed values, and leaderboard behavior unchanged.

## Capabilities

### New Capabilities

### Modified Capabilities

- `static-basket-statistics-page`: Extend Personal stats putting metric labels with explanatory custom tooltips and exact requested text.

## Impact

- `docs/app.js` putting-statistics renderers and shared tooltip-label rendering.
- The existing tooltip styling and event handling in `docs/styles.css` and `docs/app.js` should be reused; no new dependency or API change is expected.
- The static page specification will gain scenarios for the four requested tooltip mappings and partial-label triggers.

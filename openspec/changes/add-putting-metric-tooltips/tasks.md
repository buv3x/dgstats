## 1. Shared putting-label rendering

- [x] 1.1 Extend the shared putting metric-label renderer in `docs/app.js` to create accessible tooltip triggers using the existing `tooltip-trigger` contract.
- [x] 1.2 Add the exact C1x and C2 tooltip definitions and apply them consistently to overall, yearly, and round-detail putting metrics.
- [x] 1.3 Add fragment-aware rendering so only `C2+` is interactive in `C2+ per round` and only `3+putting` is interactive in `Lost 3+putting per round`.

## 2. Verification

- [x] 2.1 Confirm the visible putting labels and values remain unchanged, including spacing and existing metric ordering.
- [x] 2.2 Confirm tooltip triggers expose the requested text through the existing hover and keyboard-focus behavior and retain visible focus styling.
- [x] 2.3 Run the project’s applicable static/code validation checks without modifying generated data or unrelated worktree changes.

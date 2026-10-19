## Why

Putting highlight tooltips currently repeat the metric name, displayed value, and a section heading before showing the useful round context. Removing that repetition makes the tooltip more compact and lets users scan the competition, round, and hole entries directly.

## What Changes

- Simplify Most C2+ and Best C1x tooltip content to context lines only.
- Simplify Longest throw-in tooltip content to context lines only, including the hole for each occurrence.
- Render each matching round or occurrence on its own line.
- Preserve the visible statistic value and accessible tooltip trigger behavior.

## Capabilities

### New Capabilities

### Modified Capabilities

- `personal-basket-statistics`: Simplify putting highlight tooltip content and require one context entry per line.

## Impact

- Tooltip formatting functions in `docs/app.js`.
- Personal putting presentation requirements and related UI verification.
- No export, database, API, or statistical calculation changes.

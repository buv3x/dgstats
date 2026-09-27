## Why

The Description view currently presents all explanatory content in one long, always-visible block. As the page grows, visitors should be able to scan the available topics and expand only the information they need while retaining immediate access to the general overview.

## What Changes

- Render each top-level Description heading as an independently collapsible accordion section.
- Keep the General Information section expanded by default.
- Start all other Description sections collapsed.
- Allow multiple sections to remain open at the same time; opening or closing one section must not change another section's state.
- Preserve the existing description source format, placeholder replacement, inline formatting, loading behavior, and section content.
- Use accessible disclosure controls for section headings and content.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Change the Description view from a flat content block to independently collapsible sections with General Information open initially.

## Impact

- Affects the static Description markup and rendering logic in `docs/index.html` and `docs/app.js`.
- Affects Description-specific styling in `docs/styles.css`.
- Requires browser-level verification of default-open state, independent expansion, content rendering, and keyboard-accessible controls.
- No API, export format, description source, or dependency changes are expected.

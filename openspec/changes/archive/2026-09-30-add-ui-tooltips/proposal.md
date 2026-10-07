## Why

Several statistics labels and abbreviated metrics are not self-explanatory without referring to the description text. Adding consistent, accessible custom tooltips will clarify the meaning of chart metrics and table columns while keeping the compact layout. The Personal stats table also needs terminology aligned with the product concept of a basket layout.

## What Changes

- Add custom tooltips to the Course stats SPR and VAR labels, with separate tooltip triggers and corrected metric descriptions.
- Add custom tooltips to Course stats table headers for Basket, Average, every score bucket, and Count.
- Add a custom tooltip to the Basket stats SPRW label.
- Add custom tooltips to the Personal stats Rating, Count, and Scores headers.
- Style all tooltip triggers with a dotted underline and support both pointer hover and keyboard focus.
- Rename the Basket stats filter label `Basket variation` to `Basket layout`.
- Rename the Personal stats table header `Variation` to `Layout`.
- Use corrected spelling and wording: `Measurement` and `recorded results`.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Add explanatory custom tooltips to statistics labels and table headers, and update Basket stats and Personal stats terminology.

## Impact

- Updates the static UI markup in `docs/index.html`.
- Updates tooltip behavior and dynamically rendered table/header content in `docs/app.js`.
- Updates tooltip-trigger and tooltip presentation styles in `docs/styles.css`.
- No API, export format, or data-processing changes.

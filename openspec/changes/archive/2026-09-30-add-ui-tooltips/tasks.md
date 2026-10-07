## 1. Add tooltip markup and terminology updates

- [x] 1.1 Add individually focusable SPR and VAR tooltip triggers to the Course stats chart heading, plus the SPRW trigger to the Basket stats heading, using the specified corrected tooltip copy.
- [x] 1.2 Add tooltip triggers and exact explanatory copy to the Course stats table headers, including separate throw-count wording for `1-2`, `3`, `4`, `5`, `6`, `7`, and `8+`.
- [x] 1.3 Add tooltip triggers to the Personal stats `Rating`, `Count`, and `Scores` headers, and rename the `Variation` header to `Layout`.
- [x] 1.4 Rename the Basket stats filter label from `Basket variation` to `Basket layout` without changing selector behavior.

## 2. Implement custom tooltip presentation and behavior

- [x] 2.1 Add reusable tooltip-trigger and tooltip-surface styles with dotted underlines, wrapping, viewport-safe positioning, hover display, and visible keyboard focus styling.
- [x] 2.2 Add the interaction logic needed for custom tooltips to work for static and dynamically rendered elements on pointer hover and keyboard focus, including appropriate hide behavior.
- [x] 2.3 Preserve the existing chart point/arrow tooltip behavior and ensure the new label tooltips do not interfere with chart hit testing or table layout.

## 3. Verify the implementation

- [x] 3.1 Review all tooltip strings and visible labels against the static-page delta specification, including spelling corrections and the `Layout` header.
- [x] 3.2 Run the project’s available non-browser syntax/compilation checks for the changed static-page assets and inspect the resulting diff for unintended data or export changes.

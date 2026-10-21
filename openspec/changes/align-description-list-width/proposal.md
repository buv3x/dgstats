## Why

The numbered statistics list in the Description view is currently wider than the surrounding explanatory paragraphs because paragraph text is capped at 920px while the ordered list has no matching constraint. This makes the list wrap at a visibly different width and weakens the document's visual consistency.

## What Changes

- Constrain the ordered list in the Description view so its text wraps consistently with the surrounding paragraphs.
- Preserve the existing list content, numbering, indentation, responsive behavior, and all other page behavior.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Require Description-view ordered-list text to use the same readable line width as adjacent explanatory paragraphs.

## Impact

- `docs/styles.css`: adjust the Description article's ordered-list width constraint.
- No APIs, data exports, JavaScript behavior, or dependencies are affected.

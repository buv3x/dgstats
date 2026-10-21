## Context

The Description view is embedded in `docs/index.html` and styled by `docs/styles.css`. Paragraphs in `.description-content` are limited to 920px, but the ordered list used for the putting-statistics descriptions has no corresponding width limit and therefore extends farther across the page.

## Goals / Non-Goals

**Goals:**

- Make ordered-list description text wrap at the same readable width as adjacent paragraphs.
- Preserve list numbering, marker indentation, content, responsive behavior, and the rest of the Description layout.

**Non-Goals:**

- Do not rewrite the putting-statistics copy.
- Do not change other lists, page views, data files, or JavaScript behavior.

## Decisions

- Add a Description-specific ordered-list width rule alongside the existing `.description-content p` rule. This keeps the fix scoped to the affected article and avoids changing global list styling.
- Use the existing 920px readable-width convention as the target. The list's native marker indentation remains intact; only its available outer width is constrained.
- Verify the result at both desktop and narrow viewport sizes, confirming that the list remains readable and does not introduce horizontal overflow.

Alternatives considered:

- Changing the entire `.description-content` width was rejected because it would alter all Description content and would not address the list-specific inconsistency.
- Reformatting the HTML text with manual line breaks was rejected because wrapping should remain responsive and should be controlled by CSS.

## Risks / Trade-offs

- [Risk] The list marker indentation makes its text start slightly farther right than paragraph text. → Mitigation: retain native list semantics and indentation while matching the paragraph content's readable line width; inspect the rendered result at representative viewport sizes.

- [Risk] A fixed max-width could affect very narrow screens. → Mitigation: use `max-width`, not a fixed width, so the list can still shrink with its parent.

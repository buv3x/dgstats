## Context

The static Description view in `docs/index.html` currently has a `Personal Stats` section describing hole-level performance ratings. The Personal stats UI now also exposes Putting stats, but the Description view does not explain its source data, completeness rule, or metric definitions.

This is a static content change. Existing putting calculations, exports, navigation, and the Future Improvements section are outside the scope of this change.

## Goals / Non-Goals

**Goals:**

- Make the existing section heading distinguish hole statistics from putting statistics.
- Add the approved, grammar-checked putting-statistics description after the hole-statistics paragraphs.
- Preserve the wording of metric names and explain their yearly/overall scope and completeness rules.

**Non-Goals:**

- Do not change putting calculations, data files, labels in the Personal stats UI, or tooltip text.
- Do not remove or revise Future Improvements; that will be handled separately.
- Do not add a new navigation item or page section outside the existing Description content.

## Decisions

- **Edit the existing Description article in `docs/index.html`.** The page is static and already contains the surrounding Personal Stats content, so adding the text there keeps the documentation colocated with related explanations.
- **Use the requested headings verbatim.** Rename `Personal Stats` to `Personal Stats. Hole Stats` and add `Personal stats. Putting stats` as the next heading.
- **Use an ordered list for the seven metrics.** The source text is explicitly numbered, and an HTML ordered list preserves that structure and accessibility.
- **Describe behavior without duplicating implementation details.** The text documents user-facing semantics such as complete-round eligibility, weighted per-round values, and secondary ranking criteria, while leaving code-level calculation rules in the putting-statistics specification.

## Risks / Trade-offs

- [Risk] Documentation could drift from putting-statistics behavior → Keep the metric names and eligibility statements aligned with the existing `putting-statistics` specification during implementation review.
- [Risk] The heading capitalization differs from nearby sentence-case UI labels → Preserve the exact headings requested for this description change.
- [Risk] Existing character encoding in the static HTML may render punctuation inconsistently → Use the file's established encoding conventions and verify the rendered text after editing.


## Context

The static Description view currently loads `description.txt`, parses top-level `#` headings into `h2` elements, and appends all resulting content to one article. The description source already uses headings to define its user-facing topics, including General Information, Course Stats, Hole Stats, Personal Stats, Technical Details, and Future Improvements.

The change is limited to the static client. It must preserve the existing relative asset loading, placeholder substitution, safe inline formatting, loading/error states, and repository-maintained text format.

## Goals / Non-Goals

**Goals:**

- Represent each parsed top-level description heading as an accessible collapsible section.
- Make General Information open on the initial successful render.
- Keep sections independent so multiple sections can be open simultaneously.
- Preserve the current content order and rendering behavior within each section.
- Maintain usable keyboard and native disclosure behavior without adding a dependency.

**Non-Goals:**

- Changing the description source syntax or wording.
- Adding nested accordions or changing top-level page navigation.
- Persisting accordion state across page reloads or URL changes.
- Making the accordion mutually exclusive.

## Decisions

### Use native disclosure elements

Render each section as a `details` element with a `summary` containing the section title and a content container containing its paragraphs. Native disclosure elements provide keyboard interaction, semantics, and open/close behavior without custom state management or a new dependency.

The General Information section receives the `open` attribute when sections are initially built. No shared accordion controller will remove `open` from sibling sections, which preserves independent expansion.

### Build sections during the existing description parse

Update the current line parser to accumulate one section at a time. When a new top-level heading is encountered, flush the previous section and start a new `details` element. Paragraph flushing remains unchanged in principle and continues to use safe text-node/element construction for inline markers.

This keeps section boundaries driven by the existing source headings and avoids duplicating or hard-coding the current list of topics in JavaScript.

### Keep the description article as the visual container

Retain the existing `description-content` article and style each generated `details` section inside it. Add spacing, borders, summary affordances, and content padding through CSS while preserving the page's existing typography and color palette.

### Keep failure and loading states unchanged

The message element remains outside the generated article content. Fetch failures continue to hide the article and display the existing unavailable message; successful rendering continues to show the article after all sections are built.

## Risks / Trade-offs

- [Risk] Browser-specific default disclosure marker styling may differ → Use modest CSS normalization while retaining the native disclosure affordance.
- [Risk] A malformed description without headings could produce no accordion sections → Preserve the existing paragraph rendering fallback and verify the content remains visible or reports the same deterministic state.
- [Risk] Changing the DOM structure could affect existing description CSS → Scope selectors to the generated `details`, `summary`, and section content elements and verify the static page visually.

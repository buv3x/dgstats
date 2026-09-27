## Context

The static page in `docs/` currently loads `data/statistics.json`, renders three
statistics views, and makes Course stats the active view in the HTML and
JavaScript. The description source is a repository file with Markdown-style
headings, square-bracket placeholders, and two lightweight inline formatting
markers. The export manifest already contains export diagnostics and paths, but
competition count and latest competition metadata are not available as direct
manifest values.

The change crosses the Java export service and the static HTML/JavaScript/CSS
page. The description must remain usable as a repository-maintained text asset,
and the generated static page must continue to work from relative GitHub Pages
paths.

## Goals / Non-Goals

**Goals:**

- Add a first, default Description view while preserving all existing views.
- Load the description from a static relative asset and render its headings,
  paragraphs, placeholder values, italic markers, and bold markers safely.
- Export stable metadata for the three current placeholders.
- Keep placeholder resolution data-driven so a regenerated snapshot updates the
  displayed counts and latest competition automatically.
- Use the reviewed, corrected `description/description.txt` as the source copy.

**Non-Goals:**

- Do not introduce a Markdown parser, templating engine, or new dependency.
- Do not make the description editable through the administration UI.
- Do not change the calculations, filters, charts, or personal-statistics
  semantics.
- Do not expose player-identifying data beyond the already exported player
  lookup contract.

## Decisions

### Keep the description as a static text asset

The page will fetch `description/description.txt` using a path that is valid
when served from the repository's `docs/` output (for example, copy or export
the file to a suitable `docs/` asset location as part of the implementation).
The client will render only the supported source constructs rather than
interpreting arbitrary HTML.

Alternative considered: hard-code the prose in `index.html`. Rejected because
it duplicates the maintained source and makes content edits easy to miss.

### Use explicit manifest metadata for placeholders

Add description-oriented metadata to `statistics.json`: the count of included
competitions, the count of included players, and a display-ready latest
competition value. Counts must describe the same exported, rated, mapped data
set that powers the page. The latest competition should be selected by the
latest available competition start date among included rows, with a stable
fallback for missing dates.

Alternative considered: scan every course file in the browser and derive the
values. Rejected because it increases initial requests and couples the
Description view to course-file layout.

### Resolve placeholders before formatting

The client will replace known `[competition_count]`, `[players_count]`, and
`[latest_competition]` tokens, then convert the supported `<italic>` and
`<bold>` markers to DOM elements using text-node/element construction or an
equivalent safe allowlist. Unknown placeholders and unsupported tags will remain
visible as plain text or be handled deterministically, rather than becoming
executable markup.

### Preserve existing view state semantics

The navigation will use the existing `setActiveView` pattern with a new
Description view. The initial state will select Description, while clicking the
existing tabs will keep their current behavior and data-loading lifecycle.

## Risks / Trade-offs

- [Description path differs between local files and GitHub Pages] → Use a
  repository-relative asset under `docs/` and test the page from its published
  relative URL.
- [Missing or stale metadata prevents useful prose] → Provide deterministic
  fallback text for unavailable values and retain the existing missing-manifest
  error state.
- [Source markers could become unsafe HTML] → Implement a small allowlist
  formatter and never inject untrusted marker content as raw HTML.
- [Latest competition date is absent for some records] → Select the maximum
  non-null start date and use a stable name/ID fallback when all dates are null.

## Migration Plan

1. Update the export manifest contract and regenerate the static snapshot.
2. Add/copy the reviewed description asset to the static output.
3. Add the Description tab and client rendering behavior.
4. Verify direct loading, missing-data behavior, and all existing tabs.

Rollback is a source revert; old manifests remain usable for the existing
statistics views, while the Description view displays its defined unavailable
metadata state until a new export is generated.

## Open Questions

- Confirm whether `competition_count` means competitions represented by rated,
  mapped exported samples (the design assumes this) or all imported
  competitions.
- Confirm the desired display format for `[latest_competition]` (name only,
  date plus name, or another localized format).

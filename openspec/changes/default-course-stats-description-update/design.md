## Context

The page now embeds Description content directly in `docs/index.html`, places Description last in the navigation, and initializes Description as the default view. The maintained source text in `description/description.txt` has since been replaced with a more detailed explanation covering hole layouts, ratings, SPR/VAR, SPRW, personal statistics, and future improvements.

This change updates the embedded copy and changes only the initial view. Runtime placeholder values still come from `statistics.json`; the description source is not fetched by the browser.

## Goals / Non-Goals

**Goals:**

- Make Course stats the initial active view both in static HTML and after JavaScript initialization.
- Keep Description last in the navigation and fully selectable.
- Refresh the embedded Description structure and wording from `description/description.txt`.
- Preserve safe replacement of the three dynamic placeholders.

**Non-Goals:**

- Reintroducing runtime Description file loading or export of a Description asset.
- Changing statistics calculations, filters, charts, or manifest schema.
- Changing the navigation order of Course stats, Basket stats, and Personal stats.

## Decisions

### Set the default state in both HTML and JavaScript

The initial HTML will show `course-stats-view`, hide `description-view`, and mark Course stats active. `initializePage()` will call `setActiveView("course")` after manifest loading so the runtime state matches the no-script/initial-render state.

Updating both layers avoids a flash of Description and keeps the page behavior deterministic during asynchronous manifest loading. Changing only `initializePage()` would leave the old view visible until JavaScript runs.

### Translate the source text into semantic HTML

Replace the current embedded article with the current source sections as `h2` headings and paragraphs. Render the numbered future-improvement items as an ordered list. Keep the three placeholders as dedicated span elements in General Information, and continue updating them through `textContent`.

The source remains an editorial reference; the static HTML remains the published runtime content because the page must not fetch a separate file.

### Preserve existing view switching and fallback behavior

Keep the existing `setActiveView` implementation and manifest error handling. A missing manifest still shows the existing statistics error states, while the Description content remains visible when selected and its placeholders remain `unavailable`.

## Risks / Trade-offs

- [Risk] Embedded content can diverge from `description/description.txt` → Copy the current source carefully and preserve section order, placeholders, and list content.
- [Risk] Course stats controls/data are unavailable before the manifest loads → Preserve the existing loading/empty behavior; the default view selection should not require a second data source.
- [Risk] Static initial visibility and JavaScript state can diverge later → Verify both the HTML `hidden`/active attributes and `initializePage()` call.

## Migration Plan

1. Replace the embedded Description article content.
2. Change the initial HTML visibility and active navigation state.
3. Change JavaScript initialization to select Course stats.
4. Run syntax/compilation and static consistency checks.

Rollback is limited to restoring the previous embedded content and Description default-state values.

## Open Questions

- None.

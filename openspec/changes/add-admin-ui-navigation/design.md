## Context

The application exposes several local Spring MVC administration pages backed by Thymeleaf templates:

- `/basket-courses` lists and creates basket courses.
- `/basket-courses/{courseId}` manages baskets and variations for one course.
- `/basket-variation-mappings` maps imported round/division holes to basket variations.
- `/basket-statistics-export` triggers static statistics export.

The current navigation is ad hoc. The course list links to mapping and export pages, while mapping/export/detail pages only provide narrow back links. The root URL has no local administration landing behavior.

## Goals / Non-Goals

**Goals:**

- Make `http://localhost:8080/` open the basket courses workflow by redirecting to `/basket-courses`.
- Present the same top-level administration menu on all basket administration templates.
- Keep navigation simple and server-rendered, matching the existing Thymeleaf/Spring MVC style.
- Preserve all existing forms, redirects, flash messages, and local-only operation.

**Non-Goals:**

- No new client-side framework, layout system, authentication, or session state.
- No database or export data contract changes.
- No changes to the static GitHub Pages statistics viewer under `docs/`.
- No requirement to rename existing routes or page titles.

## Decisions

1. Add a small MVC redirect handler for `/`.

   The root path should return a Spring redirect view to `/basket-courses`. This keeps the existing basket courses controller as the actual landing page and avoids duplicating page content.

   Alternative considered: serve the basket courses template directly from `/`. Redirect is preferable because it leaves a single canonical URL for the list page and keeps existing form actions unchanged.

2. Render the menu in each admin template using existing Thymeleaf links.

   The templates currently keep their styling inline and do not use a shared layout fragment. For this small change, copying a minimal `nav` block and matching CSS is lower risk than introducing a new layout abstraction.

   Alternative considered: create a Thymeleaf fragment for shared navigation. That may be useful later, but it would add template structure for four pages when the immediate change is small.

3. Include the top menu on basket course detail pages.

   Users can land on detail pages after creating or selecting a course, so the detail view should also provide direct access to mapping and export pages. The existing "Back to courses" affordance can remain or be folded into the top menu as long as global navigation is present.

## Risks / Trade-offs

- Duplicated menu markup across templates -> Keep the markup small and consistent; consider a Thymeleaf fragment only if more admin pages are added.
- CSS drift between pages -> Use common class names and visually similar inline CSS across the four templates.
- Root route conflict with future landing pages -> The redirect is intentionally scoped to the current local administration app and can be changed later if a public homepage is introduced.
- Navigation labels may differ from page titles -> Labels are user-facing shortcuts and do not need to exactly match existing template titles or route names.

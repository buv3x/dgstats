## Why

The local administration UI has grown into several related pages, but navigation between them is inconsistent and the root application URL does not open a useful admin page. This change makes the localhost entry point and cross-page movement predictable for routine basket administration work.

## What Changes

- Redirect `GET /` to the basket courses administration page.
- Add a shared top navigation menu to the basket courses list page, basket course detail page, basket variation mapping page, and basket statistics export page.
- Include menu links for `Basket courses`, `Basket Variation Mapping`, and `Export`, using the existing admin routes.
- Preserve existing page behavior, form submissions, redirects, flash messages, and local-only assumptions.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `basket-course-admin-ui`: Add root admin entry behavior and shared navigation requirements for basket course administration pages.
- `basket-variation-round-division-mapping`: Require the basket variation mapping administration page to expose the shared admin navigation menu.
- `basket-statistics-export`: Require the basket statistics export administration page to expose the shared admin navigation menu.

## Impact

- Affects Spring MVC route handling for the application root.
- Affects Thymeleaf templates for basket course list/detail, basket variation mappings, and basket statistics export pages.
- No database schema, persistence model, external API, export data contract, authentication, or dependency changes are expected.

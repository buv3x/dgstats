## 1. Root Route

- [x] 1.1 Add a Spring MVC handler for `GET /` that redirects to `/basket-courses`.
- [x] 1.2 Confirm the new root handler does not alter existing `/api` or basket administration route mappings.

## 2. Shared Administration Navigation

- [x] 2.1 Add consistent top navigation styling and markup to `basket-courses.html`.
- [x] 2.2 Add consistent top navigation styling and markup to `basket-course-detail.html`.
- [x] 2.3 Add consistent top navigation styling and markup to `basket-variation-mappings.html`.
- [x] 2.4 Add consistent top navigation styling and markup to `basket-statistics-export.html`.
- [x] 2.5 Ensure each top menu links to `/basket-courses`, `/basket-variation-mappings`, and `/basket-statistics-export`.

## 3. Preservation Checks

- [x] 3.1 Review basket course list and detail forms to ensure existing actions, redirects, and flash feedback remain unchanged.
- [x] 3.2 Review basket variation mapping controls to ensure competition selection, basket course selection, copy controls, and save form behavior remain unchanged.
- [x] 3.3 Review the export page to ensure export submission and result diagnostics remain unchanged.

## 4. Validation

- [x] 4.1 Compile the project to confirm Spring MVC controller changes and Thymeleaf templates remain valid.
- [x] 4.2 Inspect the changed templates and route mappings against the OpenSpec scenarios.

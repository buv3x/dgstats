## Context

The application is a Spring Boot 3.4 MVC/JPA service backed by PostgreSQL and Liquibase. It currently exposes an API endpoint for triggering PDGA imports, but it does not have server-rendered pages, template dependencies, or Java mappings for the manual basket tables.

The database already contains the manual model needed for Part 1:

- `datas.basket_course` stores manually maintained course names.
- `datas.basket` stores baskets linked to `basket_course`.
- `datas.basket_variation` stores named basket variations and `distance`.

These tables are intentionally separate from imported PDGA `course`, `layout`, and `layout_hole` data. This change should expose the manual model for local maintenance without coupling it to PDGA import behavior yet.

## Goals / Non-Goals

**Goals:**

- Provide a simple browser UI for maintaining basket courses, baskets, and basket variations in the local database.
- Use conventional Spring MVC controllers and server-rendered HTML.
- Keep the UI usable from localhost without introducing production administration infrastructure.
- Add JPA mappings, repositories, and transactional service methods for the manual basket tables.
- Validate required form input in the application layer before saving.
- Preserve existing PDGA import behavior.

**Non-Goals:**

- Do not link basket variations to `hole_score` rows in this change.
- Do not infer or synchronize manual baskets from PDGA layouts.
- Do not add delete operations unless explicitly requested later.
- Do not add authentication, authorization, user sessions, audit history, or production deployment work.
- Do not build a frontend application framework or REST API for the admin UI.

## Decisions

1. Use a separate manual basket domain model.

   Add `BasketCourse`, `Basket`, and `BasketVariation` entities rather than reusing imported `Course`, `Layout`, or `LayoutHole`. The schema already separates manually entered basket-course data from PDGA course/layout data, and keeping the Java model separate avoids implying a relationship that Part 2 has not defined yet.

   Alternative considered: attach baskets directly to imported `Course`. That would blur manual course maintenance with PDGA-imported course records and could create confusing update behavior when imports rename or add PDGA courses.

2. Use Spring MVC with Thymeleaf templates.

   Add `spring-boot-starter-thymeleaf` and render ordinary form pages. This matches the request for simple HTML and Spring controllers while avoiding raw string HTML in controllers.

   Alternative considered: expose JSON endpoints and build static JavaScript pages. That adds an API surface and client-side state that are unnecessary for this local maintenance workflow.

3. Use redirect-after-post for all mutations.

   Form submissions should create or update records and then redirect back to the course list or selected course detail page. This avoids duplicate writes on browser refresh and keeps controller behavior predictable.

   Alternative considered: AJAX updates. That would make the UI more dynamic but introduces client-side complexity without changing the core workflow.

4. Scope Part 1 to create and update operations.

   The requested workflow mentions adding new records and editing existing names/info. Delete behavior is intentionally left out because deleting baskets or variations may later affect score references through `hole_score.basket_variation_id`.

   Alternative considered: include delete immediately. That would require decisions about cascade behavior, blocked deletion, or orphan handling, which belongs closer to Part 2.

5. Bind the application to localhost.

   Configure `server.address=127.0.0.1` so the local admin UI is not exposed on external interfaces by default. This is enough for the stated localhost-only use case and avoids adding unnecessary security mechanics.

   Alternative considered: add Spring Security. That would be heavier than the current requirement and would introduce login/session behavior that the app does not otherwise need.

6. Keep schema changes minimal.

   The existing schema already has the required tables and `distance` column. The implementation should not need a new Liquibase changeset unless validation rules are moved into database constraints. Application-level validation is sufficient for Part 1.

   The existing `src/main/resources/sql/cleanup.sql` script references the removed `datas.basket.course_id` column. If that script is still used, update it to match `basket_course_id` or remove that stale basket condition.

## Risks / Trade-offs

- Localhost binding can surprise users who previously expected the app to be reachable from another machine -> Document this as intentional for the admin UI and allow future configuration if remote access is needed.
- Adding Thymeleaf introduces a new dependency -> The dependency is small and conventional for Spring server-rendered pages.
- Without delete operations, mistaken entries cannot be removed through the UI -> This is acceptable for Part 1 and avoids premature decisions about score references.
- Database nullable columns allow incomplete manual rows from outside the UI -> Validate UI submissions in the service/controller layer; consider database constraints later if manual SQL edits remain a problem.
- Rendering all baskets and variations on one course detail page may become large for very large courses -> The expected local data size is small enough for a simple page; pagination can be added later if needed.

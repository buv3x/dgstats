## Why

Basket course, basket, and basket variation data is manually maintained because it does not come directly from the PDGA API. Today that means editing the local database or SQL scripts by hand, which is slow and error-prone for routine local maintenance.

This change adds a small localhost-only web UI so the manual basket-course model can be maintained through simple Spring MVC pages before later work connects those variations to scoring or analysis flows.

## What Changes

- Add a local basket course administration page where the user can view existing basket courses and create a new one.
- Add a basket course detail page where the user can select a basket course and manage its baskets.
- Allow adding baskets to a selected basket course.
- Allow editing existing basket names.
- Allow adding and editing basket variations for each basket, including variation name and distance.
- Keep the UI server-rendered and simple, using Spring MVC controllers and HTML templates.
- Bind the local application to localhost so the admin UI is not exposed on external interfaces by default.
- Exclude Part 2 behavior such as associating basket variations with imported `hole_score` rows, PDGA layout matching, analysis UI, authentication, and production deployment.

## Capabilities

### New Capabilities

- `basket-course-admin-ui`: Local web UI for maintaining manually entered basket courses, baskets, and basket variations.

### Modified Capabilities

- None.

## Impact

- Adds JPA entities and repositories for `datas.basket_course`, `datas.basket`, and `datas.basket_variation`.
- Adds Spring MVC controller routes and server-rendered HTML templates for local basket-course administration.
- Adds a small service layer for transactional create/update operations.
- Adds template rendering dependency if the implementation uses Thymeleaf.
- Updates local configuration so the Spring Boot server binds to `127.0.0.1`.
- May update the existing manual SQL cleanup script if it still references the removed `datas.basket.course_id` column.

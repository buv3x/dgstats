## 1. Persistence Model

- [x] 1.1 Add JPA entities for `BasketCourse`, `Basket`, and `BasketVariation` mapped to the existing `datas` tables and relationships.
- [x] 1.2 Add Spring Data repositories for basket courses, baskets, and basket variations with ordering/query methods needed by the UI.
- [x] 1.3 Update `HoleScore` mapping for the existing nullable `basket_variation_id` column only if needed to keep Hibernate validation aligned with the schema.

## 2. Administration Service

- [x] 2.1 Add a transactional basket course administration service for loading courses, baskets, and variations.
- [x] 2.2 Implement create/update methods for basket courses and baskets with non-blank name validation.
- [x] 2.3 Implement create/update methods for basket variations with non-blank name and valid distance validation.
- [x] 2.4 Ensure basket and variation updates verify that submitted child ids belong to the selected course context before saving.

## 3. Web UI

- [x] 3.1 Add the Thymeleaf dependency and template/resource structure for server-rendered pages.
- [x] 3.2 Add a Spring MVC controller for `GET /basket-courses` and basket course creation.
- [x] 3.3 Add a Spring MVC controller route for `GET /basket-courses/{courseId}` showing the selected course, its baskets, and each basket's variations.
- [x] 3.4 Add POST routes for adding baskets and editing existing basket names using redirect-after-post.
- [x] 3.5 Add POST routes for adding and editing basket variations using redirect-after-post.
- [x] 3.6 Add simple HTML templates with validation feedback for course, basket, and variation forms.

## 4. Local Configuration and Cleanup

- [x] 4.1 Configure the default embedded server binding to `127.0.0.1`.
- [x] 4.2 Keep the existing `/api/import/pdga` endpoint behavior unchanged.
- [x] 4.3 Fix or explicitly remove the stale `datas.basket.course_id` reference in `src/main/resources/sql/cleanup.sql`.

## 5. Verification

- [x] 5.1 Compile the application successfully.
- [x] 5.2 Manually inspect the implemented routes and templates for create/update coverage against the spec.
- [x] 5.3 Confirm the application configuration binds the default server address to localhost.

# basket-course-admin-ui Specification

## Purpose
TBD - created by archiving change add-basket-course-admin-ui. Update Purpose after archive.
## Requirements
### Requirement: Basket course selection and creation
The system SHALL provide a local web page for viewing existing basket courses and creating a new basket course.

#### Scenario: Existing basket courses are listed
- **WHEN** the local user opens the basket course administration page
- **THEN** the system displays the existing `basket_course` records with links or controls to select each course

#### Scenario: New basket course is created
- **WHEN** the local user submits a non-blank basket course name
- **THEN** the system persists a new `basket_course` record and returns the user to a page where the course can be selected or managed

#### Scenario: Blank basket course name is rejected
- **WHEN** the local user submits a blank basket course name
- **THEN** the system does not create a basket course and displays the course list page with validation feedback

### Requirement: Basket management for selected course
The system SHALL provide a basket course detail page where the local user can view baskets for a selected basket course, add baskets, and edit existing basket names.

#### Scenario: Selected course baskets are displayed
- **WHEN** the local user opens the detail page for a basket course
- **THEN** the system displays the selected course name and the baskets linked to that `basket_course`

#### Scenario: Basket is added to selected course
- **WHEN** the local user submits a non-blank basket name for the selected basket course
- **THEN** the system persists a new `basket` record linked to that `basket_course` and returns to the selected course detail page

#### Scenario: Basket name is updated
- **WHEN** the local user submits a non-blank replacement name for an existing basket on the selected basket course
- **THEN** the system updates that `basket` record and returns to the selected course detail page

#### Scenario: Blank basket name is rejected
- **WHEN** the local user submits a blank basket name
- **THEN** the system does not create or update the basket and displays validation feedback on the selected course detail page

### Requirement: Basket variation management
The system SHALL allow the local user to add and edit basket variations for each basket on the selected basket course, including variation name and distance.

#### Scenario: Basket variations are displayed
- **WHEN** the local user opens the detail page for a basket course
- **THEN** the system displays the existing `basket_variation` records for each listed basket

#### Scenario: Basket variation is added
- **WHEN** the local user submits a non-blank variation name and valid distance for a basket
- **THEN** the system persists a new `basket_variation` record linked to that basket and returns to the selected course detail page

#### Scenario: Basket variation is updated
- **WHEN** the local user submits a non-blank variation name and valid distance for an existing basket variation
- **THEN** the system updates that `basket_variation` record and returns to the selected course detail page

#### Scenario: Invalid basket variation input is rejected
- **WHEN** the local user submits a blank variation name or invalid distance
- **THEN** the system does not create or update the basket variation and displays validation feedback on the selected course detail page

### Requirement: Local-only web administration
The system SHALL expose the basket course administration UI for localhost use only by default.

#### Scenario: Application binds to localhost
- **WHEN** the application starts with its default configuration
- **THEN** the embedded web server binds to `127.0.0.1`

#### Scenario: No production administration features are required
- **WHEN** the basket course administration UI is reviewed
- **THEN** the implementation does not require authentication, user sessions, remote deployment configuration, or a client-side application framework

### Requirement: Existing import behavior is preserved
The system SHALL preserve existing PDGA import behavior while adding the manual basket course administration UI.

#### Scenario: PDGA import endpoint remains available
- **WHEN** the local user invokes the existing PDGA import endpoint
- **THEN** the system continues to trigger pending competition import processing as before

#### Scenario: Manual basket data is not synchronized from PDGA
- **WHEN** PDGA competition data is imported
- **THEN** the system does not create, update, or delete `basket_course`, `basket`, or `basket_variation` records as part of this change


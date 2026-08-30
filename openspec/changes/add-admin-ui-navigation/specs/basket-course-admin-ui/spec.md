## ADDED Requirements

### Requirement: Default administration entry
The system SHALL route the local application root to the basket course administration page.

#### Scenario: Root URL redirects to basket courses
- **WHEN** the local user opens `http://localhost:8080/`
- **THEN** the system redirects the request to `/basket-courses`

### Requirement: Basket course administration navigation
The system SHALL display a top administration navigation menu on basket course administration pages.

#### Scenario: Basket course list has top navigation
- **WHEN** the local user opens the basket course list page
- **THEN** the page displays navigation links to `/basket-courses`, `/basket-variation-mappings`, and `/basket-statistics-export`

#### Scenario: Basket course detail has top navigation
- **WHEN** the local user opens a basket course detail page
- **THEN** the page displays navigation links to `/basket-courses`, `/basket-variation-mappings`, and `/basket-statistics-export`

#### Scenario: Basket course page behavior is preserved
- **WHEN** the local user uses the basket course list or detail page after navigation is added
- **THEN** existing course, basket, and variation form submissions and validation feedback continue to work

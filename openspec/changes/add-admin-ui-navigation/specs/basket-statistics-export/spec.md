## ADDED Requirements

### Requirement: Basket statistics export administration navigation
The system SHALL display a top administration navigation menu on the basket statistics export administration page.

#### Scenario: Export page has top navigation
- **WHEN** the local user opens the basket statistics export administration page
- **THEN** the page displays navigation links to `/basket-courses`, `/basket-variation-mappings`, and `/basket-statistics-export`

#### Scenario: Export page behavior is preserved
- **WHEN** the local user uses the basket statistics export page after navigation is added
- **THEN** the existing export action, success diagnostics, and validation feedback continue to work

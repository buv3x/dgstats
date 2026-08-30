## ADDED Requirements

### Requirement: Basket variation mapping administration navigation
The system SHALL display a top administration navigation menu on the basket variation mapping administration page.

#### Scenario: Mapping page has top navigation
- **WHEN** the local user opens the basket variation mapping administration page
- **THEN** the page displays navigation links to `/basket-courses`, `/basket-variation-mappings`, and `/basket-statistics-export`

#### Scenario: Mapping page behavior is preserved
- **WHEN** the local user uses the basket variation mapping administration page after navigation is added
- **THEN** existing competition selection, basket course selection, mapping copy controls, mapping save behavior, and validation feedback continue to work

## ADDED Requirements

### Requirement: Personal stats client-side filters
The static page SHALL provide Personal stats filters for basket course and minimum personal result count, applied only in the browser to the selected player's loaded personal statistics rows.

#### Scenario: Course selector uses selected player rows
- **WHEN** a selected player's personal statistics file loads with variation rows
- **THEN** the Personal stats course selector is populated with an all-courses option and only the basket courses present in those loaded rows

#### Scenario: Course filter is optional
- **WHEN** the Personal stats course selector is set to the all-courses option
- **THEN** the Personal stats list includes rows from every basket course in the selected player's loaded personal statistics rows that also match the minimum count filter

#### Scenario: Selected course filters rows
- **WHEN** the user selects a basket course in the Personal stats course selector
- **THEN** the Personal stats list includes only selected-player variation rows whose `basketCourseId` matches the selected course and whose `count` matches the minimum count filter

#### Scenario: Minimum count defaults to two
- **WHEN** the Personal stats controls are shown
- **THEN** the minimum count filter defaults to `2`

#### Scenario: Minimum count cannot be lower than two
- **WHEN** the user enters a minimum count value lower than `2`, empty, or invalid
- **THEN** the page normalizes the minimum count filter to `2` before applying it

#### Scenario: Minimum count filters rows
- **WHEN** the Personal stats minimum count filter is set to a value of `2` or greater
- **THEN** the Personal stats list includes only selected-player variation rows whose `count` is greater than or equal to that value and whose course matches the course filter

#### Scenario: Filtering preserves exported row order
- **WHEN** Personal stats filters are applied
- **THEN** matching rows are displayed in the same relative order as the selected player's exported personal statistics file

#### Scenario: Empty filtered result is reported
- **WHEN** the selected player's personal statistics file contains variation rows but none match the active Personal stats filters
- **THEN** the Personal stats view displays a filtered-empty message instead of an empty table

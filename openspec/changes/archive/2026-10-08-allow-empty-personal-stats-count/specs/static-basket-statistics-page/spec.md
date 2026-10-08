## MODIFIED Requirements

### Requirement: Personal stats client-side filters
The static page SHALL provide Personal stats filters for basket course and minimum personal result count, applied only in the browser to the selected player's loaded personal statistics rows. The minimum count filter SHALL allow an empty value while the user is editing, treat that empty value as the default effective value of `2`, and normalize the visible value to `2` when the edit is committed.

#### Scenario: Course selector uses selected player rows
- **WHEN** a selected player's personal statistics file loads with variation rows
- **THEN** the Personal stats course selector is populated with an all-courses option and only the basket courses present in those loaded rows

#### Scenario: Course filter is optional
- **WHEN** the Personal stats course selector is set to the all-courses option
- **THEN** the Personal stats list includes rows from every basket course in the selected player's loaded personal statistics rows that also match the minimum count filter, ordered by rating descending

#### Scenario: Selected course filters rows
- **WHEN** the user selects a basket course in the Personal stats course selector
- **THEN** the Personal stats list includes only selected-player variation rows whose `basketCourseId` matches the selected course and whose `count` matches the minimum count filter, ordered by rating descending

#### Scenario: Minimum count defaults to two
- **WHEN** the Personal stats controls are shown
- **THEN** the minimum count filter defaults to `2`

#### Scenario: Minimum count can be cleared while editing
- **WHEN** the user deletes the current minimum count value
- **THEN** the input remains empty until the edit is committed and the Personal Stats list is filtered as if the minimum count were `2`

#### Scenario: Empty minimum count normalizes on commit
- **WHEN** the user leaves the minimum count input empty and commits the change
- **THEN** the page restores the visible minimum count value to `2` before applying the filter

#### Scenario: Minimum count cannot be lower than two
- **WHEN** the user enters a minimum count value lower than `2` or an invalid value and commits the change
- **THEN** the page normalizes the minimum count filter to `2` before applying it

#### Scenario: Minimum count filters rows
- **WHEN** the Personal Stats minimum count filter is set to a value of `2` or greater
- **THEN** the Personal Stats list includes only selected-player variation rows whose `count` is greater than or equal to that value and whose course matches the course filter, ordered by rating descending

#### Scenario: Empty filtered result is reported
- **WHEN** the selected player's personal statistics file contains variation rows but none match the active Personal Stats filters
- **THEN** the Personal Stats view displays a filtered-empty message instead of an empty table

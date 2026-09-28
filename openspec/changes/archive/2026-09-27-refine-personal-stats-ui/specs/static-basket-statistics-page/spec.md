## MODIFIED Requirements

### Requirement: Static page empty and metadata states
The static page SHALL communicate manifest state, selected course file state, and empty filter results without displaying snapshot export-time metadata.

#### Scenario: Snapshot freshness is not displayed
- **WHEN** the statistics manifest includes export time metadata
- **THEN** the page does not display an `Exported at ...` label or the manifest export time in the page header

#### Scenario: Missing manifest is reported
- **WHEN** the page cannot load `data/statistics.json`
- **THEN** it displays a clear missing-data message

#### Scenario: Missing selected course data is reported
- **WHEN** the page cannot load the selected course statistics file
- **THEN** it displays a clear selected-course missing-data message

#### Scenario: Empty course list is reported
- **WHEN** the manifest contains no basket courses with statistics
- **THEN** the page displays a no-courses message instead of the statistics table

#### Scenario: Empty filter result is reported
- **WHEN** the selected basket course and rating filter produce no visible variation rows
- **THEN** the page displays a no-results message instead of an empty statistics table

### Requirement: Personal stats variation list
The static page SHALL display one list of personal basket variation ratings for the selected player, ordered by calculated rating from highest to lowest.

#### Scenario: Personal list displays rows
- **WHEN** a selected player's personal statistics file contains variation rows
- **THEN** the Personal stats view displays one table or list containing those rows

#### Scenario: Personal row fields are displayed
- **WHEN** a personal variation row is displayed
- **THEN** the row shows basket course, basket label, variation label, rounded rating, personal result count, and comma-separated scores

#### Scenario: Personal rows use calculated rating order
- **WHEN** personal variation rows are rendered
- **THEN** rows are ordered by exported decimal calculated rating from highest to lowest
- **AND** course, basket, and variation ordering is used only as deterministic tie-breaking

#### Scenario: Personal row order survives filtering
- **WHEN** Personal stats course or minimum-count filters are applied
- **THEN** the matching rows remain ordered by exported decimal calculated rating from highest to lowest

#### Scenario: Empty selected player results are reported
- **WHEN** the selected player's personal statistics file contains no variation rows
- **THEN** the Personal stats view displays a no-personal-results message instead of an empty list

### Requirement: Personal stats client-side filters
The static page SHALL provide Personal stats filters for basket course and minimum personal result count, applied only in the browser to the selected player's loaded personal statistics rows.

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

#### Scenario: Minimum count cannot be lower than two
- **WHEN** the user enters a minimum count value lower than `2`, empty, or invalid
- **THEN** the page normalizes the minimum count filter to `2` before applying it

#### Scenario: Minimum count filters rows
- **WHEN** the Personal stats minimum count filter is set to a value of `2` or greater
- **THEN** the Personal stats list includes only selected-player variation rows whose `count` is greater than or equal to that value and whose course matches the course filter, ordered by rating descending

#### Scenario: Empty filtered result is reported
- **WHEN** the selected player's personal statistics file contains variation rows but none match the active Personal stats filters
- **THEN** the Personal stats view displays a filtered-empty message instead of an empty table

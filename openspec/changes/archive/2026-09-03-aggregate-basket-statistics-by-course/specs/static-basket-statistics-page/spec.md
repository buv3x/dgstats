## MODIFIED Requirements

### Requirement: Static basket statistics assets
The system SHALL provide a GitHub Pages-ready static basket statistics page with separated assets.

#### Scenario: Static page file exists
- **WHEN** the static visualization files are reviewed
- **THEN** `docs/index.html` provides the basket statistics page

#### Scenario: Static assets are separated
- **WHEN** the static visualization files are reviewed
- **THEN** the page behavior and styling are placed in separate JavaScript and CSS files under `docs/`

#### Scenario: Manifest is loaded from relative data path
- **WHEN** the static basket statistics page loads
- **THEN** it fetches the statistics manifest from `data/statistics.json`

#### Scenario: Selected course data is loaded from manifest path
- **WHEN** the user selects a basket course from the manifest
- **THEN** the page fetches that course's statistics file using the manifest-provided relative path

### Requirement: Statistics controls
The static page SHALL provide spreadsheet-like controls for selecting basket course and applying rating bounds.

#### Scenario: Course selector is populated
- **WHEN** the statistics manifest loads successfully
- **THEN** the page populates a basket course selector using exported basket course names

#### Scenario: Competition selector is not displayed
- **WHEN** the statistics page is displayed
- **THEN** the page does not display a competition filter

#### Scenario: Rating inputs are available
- **WHEN** the statistics page is displayed
- **THEN** the page provides numeric Rating from and Rating to inputs

#### Scenario: Filter button applies controls
- **WHEN** the user clicks the Filter button
- **THEN** the page recalculates displayed statistics using the selected basket course and rating inputs

### Requirement: Rating filter semantics
The static page SHALL filter samples by `round_result.rating` values exported in the selected basket course statistics file.

#### Scenario: Both rating bounds are inclusive
- **WHEN** the user filters with Rating from and Rating to values
- **THEN** the page includes samples with ratings greater than or equal to Rating from and less than or equal to Rating to

#### Scenario: Empty lower bound is open-ended
- **WHEN** Rating from is empty and Rating to has a value
- **THEN** the page includes samples with ratings less than or equal to Rating to

#### Scenario: Empty upper bound is open-ended
- **WHEN** Rating from has a value and Rating to is empty
- **THEN** the page includes samples with ratings greater than or equal to Rating from

#### Scenario: Empty bounds include all rated samples
- **WHEN** both rating inputs are empty
- **THEN** the page includes all exported samples for the selected basket course

### Requirement: Basket variation statistics table
The static page SHALL display aggregated score statistics grouped by basket and basket variation for the selected basket course.

#### Scenario: All competitions are aggregated for selected course
- **WHEN** the page calculates statistics for a selected basket course
- **THEN** it aggregates matching score samples across all competitions represented in the selected course statistics file

#### Scenario: Basket rows are grouping rows
- **WHEN** statistics are displayed
- **THEN** each basket appears as a grouping row with no score statistics

#### Scenario: Variation rows contain statistics
- **WHEN** statistics are displayed
- **THEN** each basket variation row displays Count, Average, and score bucket percentages

#### Scenario: Basket and variation rows are sorted by id
- **WHEN** statistics are displayed
- **THEN** basket groups are sorted by basket id and variation rows are sorted by variation id

#### Scenario: Empty rows are hidden
- **WHEN** a basket variation has no samples after filtering
- **THEN** that variation row is not displayed

#### Scenario: Empty basket groups are hidden
- **WHEN** all variations for a basket have no samples after filtering
- **THEN** that basket group is not displayed

### Requirement: Static page empty and metadata states
The static page SHALL communicate manifest state, selected course file state, and empty filter results.

#### Scenario: Snapshot freshness is displayed
- **WHEN** the statistics manifest includes export time metadata
- **THEN** the page displays the snapshot export time

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

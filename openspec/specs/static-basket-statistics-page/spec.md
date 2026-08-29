# static-basket-statistics-page Specification

## Purpose
TBD - created by archiving change add-static-basket-statistics-export. Update Purpose after archive.
## Requirements
### Requirement: Static basket statistics assets
The system SHALL provide a GitHub Pages-ready static basket statistics page with separated assets.

#### Scenario: Static page file exists
- **WHEN** the static visualization files are reviewed
- **THEN** `docs/index.html` provides the basket statistics page

#### Scenario: Static assets are separated
- **WHEN** the static visualization files are reviewed
- **THEN** the page behavior and styling are placed in separate JavaScript and CSS files under `docs/`

#### Scenario: Snapshot is loaded from relative data path
- **WHEN** the static basket statistics page loads
- **THEN** it fetches the statistics snapshot from `data/statistics.json`

### Requirement: Statistics controls
The static page SHALL provide spreadsheet-like controls for selecting competition and applying rating bounds.

#### Scenario: Competition selector is populated
- **WHEN** the statistics snapshot loads successfully
- **THEN** the page populates a competition selector using exported competition names

#### Scenario: Rating inputs are available
- **WHEN** the statistics page is displayed
- **THEN** the page provides numeric Rating from and Rating to inputs

#### Scenario: Filter button applies controls
- **WHEN** the user clicks the Filter button
- **THEN** the page recalculates displayed statistics using the selected competition and rating inputs

### Requirement: Rating filter semantics
The static page SHALL filter samples by `round_result.rating` values exported in the snapshot.

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
- **THEN** the page includes all exported samples for the selected competition

### Requirement: Basket variation statistics table
The static page SHALL display all-round aggregated score statistics grouped by basket and basket variation.

#### Scenario: All rounds are aggregated
- **WHEN** the page calculates statistics for a selected competition
- **THEN** it aggregates matching score samples across all rounds in that competition

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

### Requirement: Spreadsheet-style statistic formatting
The static page SHALL format statistics to match the spreadsheet-style table.

#### Scenario: Count is row-level sample count
- **WHEN** a variation row is displayed
- **THEN** Count equals the number of included hole-score samples for that variation

#### Scenario: Average is rounded to three decimals
- **WHEN** a variation row is displayed
- **THEN** Average is shown rounded to 3 decimal places

#### Scenario: Score buckets are displayed as percentages
- **WHEN** a variation row is displayed
- **THEN** columns `1-2`, `3`, `4`, `5`, `6`, `7`, and `8+` show percentages rounded to 1 decimal place

#### Scenario: Low score bucket includes scores up to two
- **WHEN** a score is less than or equal to 2
- **THEN** it contributes to the `1-2` bucket

#### Scenario: High score bucket includes scores eight and above
- **WHEN** a score is greater than or equal to 8
- **THEN** it contributes to the `8+` bucket

#### Scenario: Zero percentages are displayed
- **WHEN** a bucket has no matching samples for a displayed variation row
- **THEN** the bucket displays `0.0`

### Requirement: Static page empty and metadata states
The static page SHALL communicate snapshot state and empty filter results.

#### Scenario: Snapshot freshness is displayed
- **WHEN** the statistics snapshot includes export time metadata
- **THEN** the page displays the snapshot export time

#### Scenario: Missing snapshot is reported
- **WHEN** the page cannot load `data/statistics.json`
- **THEN** it displays a clear missing-data message

#### Scenario: Empty filter result is reported
- **WHEN** the selected competition and rating filter produce no visible variation rows
- **THEN** the page displays a no-results message instead of an empty statistics table


# static-basket-statistics-page Specification Delta

## MODIFIED Requirements

### Requirement: Basket variation statistics table

The static page SHALL display aggregated score statistics grouped by basket and
basket variation for the selected basket course.

#### Scenario: All competitions are aggregated for selected course

- **WHEN** the page calculates statistics for a selected basket course
- **THEN** it aggregates matching score samples across all competitions
  represented in the selected course statistics file

#### Scenario: Basket rows are grouping rows

- **WHEN** statistics are displayed
- **THEN** each basket appears as a grouping row with no score statistics

#### Scenario: Variation rows contain statistics

- **WHEN** statistics are displayed
- **THEN** each basket variation row displays Count, Average, and score bucket
  percentages

#### Scenario: Basket and variation rows use explicit order

- **WHEN** statistics are displayed
- **THEN** basket groups with non-null `sortOrder` are ordered by ascending
  `sortOrder`, followed by baskets with null `sortOrder` ordered by basket ID
- **AND** variation rows use the same ordering rule with variation `sortOrder`
  and variation ID

#### Scenario: Empty rows are hidden

- **WHEN** a basket variation has no samples after filtering
- **THEN** that variation row is not displayed

#### Scenario: Empty basket groups are hidden

- **WHEN** all variations for a basket have no samples after filtering
- **THEN** that basket group is not displayed

### Requirement: Personal stats variation list

The static page SHALL display one ordered list of personal basket variation
ratings for the selected player.

#### Scenario: Personal list displays rows

- **WHEN** a selected player's personal statistics file contains variation rows
- **THEN** the Personal stats view displays one table or list containing those
  rows

#### Scenario: Personal row fields are displayed

- **WHEN** a personal variation row is displayed
- **THEN** the row shows basket course, basket label, variation label, rounded
  rating, personal result count, and comma-separated scores

#### Scenario: Personal rows use exported entity order

- **WHEN** personal variation rows are rendered
- **THEN** rows use explicit basket and variation `sortOrder` values when present,
  with null-last and ID fallback ordering within the public display
- **AND** existing exported rating order remains the fallback for otherwise
  equivalent rows

#### Scenario: Empty selected player results are reported

- **WHEN** the selected player's personal statistics file contains no variation
  rows
- **THEN** the Personal stats view displays a no-personal-results message
  instead of an empty list

## ADDED Requirements

### Requirement: Public course selector ordering

All course select boxes on the public `docs/` statistics page SHALL order course
options by total exported result count descending without displaying that count.

#### Scenario: Course stats selector uses result count

- **WHEN** the statistics manifest loads successfully
- **THEN** the Course stats selector orders courses by descending `sampleCount`
- **AND** option labels contain only the course name

#### Scenario: Basket stats selector uses result count

- **WHEN** the statistics manifest loads successfully
- **THEN** the Basket stats selector orders eligible courses by descending
  `sampleCount`
- **AND** option labels contain only the course name

#### Scenario: Personal stats selector uses result count

- **WHEN** a selected player's personal statistics contain rows from multiple
  courses
- **THEN** the Personal stats course selector orders those courses by the
  corresponding manifest `sampleCount` descending
- **AND** the all-courses option remains available

#### Scenario: Course count ties are deterministic

- **WHEN** two courses have the same total exported result count
- **THEN** they are ordered by course name and then course ID

### Requirement: Public Basket stats variation selector ordering

The Basket stats variation selector SHALL use explicit basket and variation
ordering from the exported data.

#### Scenario: Variation selector uses entity order

- **WHEN** a selected course's Basket stats file loads successfully
- **THEN** variations are ordered by basket `sortOrder`, then basket ID,
  variation `sortOrder`, then variation ID, with null sort orders after
  non-null values


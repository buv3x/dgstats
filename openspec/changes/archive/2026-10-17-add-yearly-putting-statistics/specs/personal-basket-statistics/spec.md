## MODIFIED Requirements

### Requirement: Personal putting totals and round aggregates
The personal statistics export SHALL provide combined putting metrics from complete player rounds and separate year-scoped putting metrics for every year represented by the player's dated rounds, including year-scoped aggregates for represented rounds.

#### Scenario: Overall putting totals remain available
- **WHEN** a player has exported putting attempts from complete rounds
- **THEN** the player's putting data includes overall C1x and C2 metrics
- **AND** each metric includes successful count, total count, and percentage

#### Scenario: Complete round aggregate is exported under its year
- **WHEN** a player has a complete represented round with a non-null round date
- **THEN** the player's putting data includes the round under the calendar year of that date
- **AND** the aggregate includes `complete: true`
- **AND** the aggregate includes competition and abbreviated round labels
- **AND** the aggregate includes C1x and C2 metrics with successful count, total count, and percentage

#### Scenario: Undated round is excluded from year sections
- **WHEN** a represented player round has no round date
- **THEN** the round is not included in any year summary or year-level round details
- **AND** the round remains eligible to contribute to global aggregates when it is complete

#### Scenario: Year metrics use only complete dated rounds from that year
- **WHEN** a player has complete and incomplete represented rounds in a calendar year
- **THEN** that year's C1x and C2 totals include only the complete rounds from that year
- **AND** that year's C2+ per-round, most-C2+ round, and best-C1x round metrics use only those complete rounds

#### Scenario: Year metrics are separated by calendar year
- **WHEN** a player has complete rounds in multiple calendar years
- **THEN** the export contains one year summary per represented calendar year
- **AND** no round from one year contributes to another year's metrics

#### Scenario: Incomplete round aggregate remains available under its year
- **WHEN** a dated player round has a missing expected hole or an invalid hole sequence
- **THEN** the player's putting data includes its round under the calendar year of its date
- **AND** its round-level metrics retain classified attempts from valid holes
- **AND** those attempts are excluded from both global and year-level totals

#### Scenario: Empty incomplete round remains non-contributing
- **WHEN** a dated represented player round has no imported throw rows
- **THEN** its year summary may retain the round aggregate with `complete: false` and zero metrics
- **AND** it contributes to neither global nor year-level totals

#### Scenario: Overall percentage uses complete-round counts
- **WHEN** a player has putting attempts across complete and incomplete rounds
- **THEN** the overall percentage is calculated from complete-round successful and total counts
- **AND** no incomplete-round attempt is included in the numerator or denominator

#### Scenario: Year and round percentages use their own counts
- **WHEN** a year or round aggregate has a non-zero total count for a metric
- **THEN** that metric's percentage is calculated from successful count divided by successful and unsuccessful attempts for that scope

#### Scenario: Zero-count metric has no percentage
- **WHEN** a global, year, or round C1x or C2 metric has zero total attempts
- **THEN** its percentage is null or rendered as the existing no-value marker
- **AND** its successful and total counts remain zero

### Requirement: Personal putting round detail presentation
The Personal stats putting view SHALL display overall putting metrics immediately, display dated year summaries as independently collapsible accordions closed by default, and render each year's represented round details behind a separate control inside that year; it SHALL not display a global round-details section or an unknown-year bucket.

#### Scenario: Totals are visible by default
- **WHEN** the user opens the Personal stats putting view for a player with putting data
- **THEN** the overall C1x and C2 metrics are visible
- **AND** all year accordions are closed initially
- **AND** all year-level round details are closed initially

#### Scenario: Year accordion opens
- **WHEN** the user activates a year accordion control
- **THEN** that year's metrics become visible
- **AND** its control indicates an expanded state

#### Scenario: Year accordion closes
- **WHEN** the user activates an expanded year accordion control
- **THEN** that year's metrics become hidden
- **AND** its control indicates a collapsed state

#### Scenario: Year content includes year-scoped metrics
- **WHEN** a year accordion is expanded
- **THEN** it displays that year's C1x and C2 metrics
- **AND** it displays that year's available C2+ per-round, most-C2+ round, and best-C1x round metrics

#### Scenario: Year round-detail control is nested
- **WHEN** an expanded year contains one or more displayable round aggregates
- **THEN** the year content displays a show/hide round-details control
- **AND** the control is located inside that year's accordion
- **AND** the control is collapsed initially

#### Scenario: Year round details expand and collapse
- **WHEN** the user activates a year's round-details control
- **THEN** only that year's displayable round aggregates become visible
- **AND** activating it again hides only that year's round aggregates
- **AND** its `aria-expanded` value reflects the current state

#### Scenario: Undated rounds are not displayed in year details
- **WHEN** a player has represented rounds without a round date
- **THEN** those rounds do not appear in any year accordion or year-level round-details list
- **AND** no unknown-year accordion or placeholder is displayed

#### Scenario: Empty incomplete round is hidden
- **WHEN** an exported dated round has `complete: false` and both its C1x and C2 total counts are zero
- **THEN** the round is omitted from its year's rendered round-detail list
- **AND** it does not cause an empty round-details control or container to be displayed

#### Scenario: Incomplete round with recorded attempts is styled as excluded
- **WHEN** an expanded year displays a round with `complete: false` and either its C1x or C2 total counts is greater than zero
- **THEN** its entire round-detail line is rendered in italic text
- **AND** its entire round-detail line has strikethrough styling

#### Scenario: Round line format remains unchanged
- **WHEN** a year's round aggregate is displayed
- **THEN** it uses the format `[competition.simple_name] [round.abbreviated_label]: [C1x stats], [C2 stats]`
- **AND** the competition name and round label are bold
- **AND** the `C1x` and `C2` labels are bold

#### Scenario: No dated years are available
- **WHEN** a selected player's putting data contains no represented round with a non-null year
- **THEN** the page still displays overall putting metrics
- **AND** it does not display any year accordion
- **AND** it does not display a global round-details control or container

#### Scenario: Accordion controls are keyboard accessible
- **WHEN** a keyboard user focuses and activates a year accordion or its round-details control
- **THEN** the corresponding content toggles in the same way as pointer activation
- **AND** the activated control exposes its current expanded state through `aria-expanded`

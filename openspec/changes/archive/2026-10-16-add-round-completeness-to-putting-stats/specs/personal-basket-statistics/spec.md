## MODIFIED Requirements

### Requirement: Personal putting totals and round aggregates
The personal statistics export SHALL provide combined putting metrics from complete player rounds and separate putting metrics for every represented player round, including incomplete rounds.

#### Scenario: Overall putting totals remain available
- **WHEN** a player has exported putting attempts from complete rounds
- **THEN** the player’s putting data includes overall C1x and C2 metrics
- **AND** each metric includes successful count, total count, and percentage

#### Scenario: Complete round aggregate is exported
- **WHEN** a player has a complete represented round
- **THEN** the player’s putting data includes one round aggregate for that player round
- **AND** the aggregate includes `complete: true`
- **AND** the aggregate includes competition and abbreviated round labels
- **AND** the aggregate includes C1x and C2 metrics with successful count, total count, and percentage

#### Scenario: Incomplete round aggregate is exported
- **WHEN** a player round has a missing expected hole or an invalid hole sequence
- **THEN** the player’s putting data includes one round aggregate for that player round
- **AND** the aggregate includes `complete: false`
- **AND** its round-level metrics retain classified attempts from valid holes
- **AND** those attempts are excluded from overall totals

#### Scenario: Empty incomplete round is exported
- **WHEN** a represented player round has no imported throw rows
- **THEN** the export includes its round aggregate with `complete: false`
- **AND** both round metrics have zero counts

#### Scenario: Rounds are not merged by display label
- **WHEN** two rounds have different round identities but equal competition or round display labels
- **THEN** the export contains separate aggregates for those rounds

#### Scenario: Overall percentage uses complete-round counts
- **WHEN** a player has putting attempts across complete and incomplete rounds
- **THEN** the overall percentage is calculated from complete-round successful and total counts
- **AND** no incomplete-round attempt is included in the numerator or denominator

#### Scenario: Round percentage uses round counts
- **WHEN** a round aggregate has a non-zero total count for a metric
- **THEN** that metric’s percentage is calculated from successful count divided by successful and unsuccessful attempts for that round

#### Scenario: Zero-count metric has no percentage
- **WHEN** a total or round C1x or C2 metric has zero total attempts
- **THEN** its percentage is null or rendered as the existing no-value marker
- **AND** its successful and total counts remain zero

### Requirement: Personal putting round detail presentation
The Personal stats putting view SHALL display overall putting totals immediately and all represented round details collapsed by default, visibly marking incomplete rounds as excluded from totals.

#### Scenario: Totals are visible by default
- **WHEN** the user opens the Personal stats putting view for a player with putting data
- **THEN** the overall C1x and C2 metrics are visible
- **AND** round details are hidden initially

#### Scenario: Incomplete round is styled as excluded
- **WHEN** an expanded round aggregate has `complete: false`
- **THEN** its entire round-detail line is rendered in italic text
- **AND** its entire round-detail line has strikethrough styling

#### Scenario: Legacy round without completeness metadata
- **WHEN** an older exported round aggregate has no `complete` field
- **THEN** the UI treats it as complete for rendering compatibility

#### Scenario: Round detail control is available
- **WHEN** the selected player’s export contains one or more round aggregates
- **THEN** the view displays a show/hide round-details control
- **AND** the control indicates a collapsed state initially

#### Scenario: Round details expand
- **WHEN** the user activates the show/hide round-details control while details are collapsed
- **THEN** the round aggregates become visible
- **AND** the control indicates an expanded state

#### Scenario: Round details collapse
- **WHEN** the user activates the show/hide round-details control while details are expanded
- **THEN** the round aggregates become hidden
- **AND** the control indicates a collapsed state

#### Scenario: Round line format and emphasis
- **WHEN** an expanded round aggregate is displayed
- **THEN** it uses the format `[competition.simple_name] [round.abbreviated_label]: [C1x stats], [C2 stats]`
- **AND** the competition name and round label are bold
- **AND** the `C1x` and `C2` labels are bold

#### Scenario: Total metric labels are emphasized
- **WHEN** overall putting totals are displayed
- **THEN** the `C1x` and `C2` labels are bold

#### Scenario: No round details are available
- **WHEN** a selected player’s putting data has no round aggregates
- **THEN** the page still displays overall totals
- **AND** it does not display an empty round-details control or container

#### Scenario: Round detail control is keyboard accessible
- **WHEN** a keyboard user focuses and activates the show/hide control
- **THEN** the round details toggle in the same way as pointer activation
- **AND** the control exposes its current expanded state through `aria-expanded`

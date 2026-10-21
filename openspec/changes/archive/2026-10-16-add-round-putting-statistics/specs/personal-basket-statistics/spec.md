## ADDED Requirements

### Requirement: Personal putting totals and round aggregates
The personal statistics export SHALL provide both combined putting metrics and separate putting metrics for each competition round represented for the player.

#### Scenario: Overall putting totals remain available
- **WHEN** a player has exported putting attempts
- **THEN** the player's putting data includes overall C1x and C2 metrics
- **AND** each metric includes successful count, total count, and percentage

#### Scenario: Round putting aggregate is exported
- **WHEN** a player has at least one classified putting attempt in a round
- **THEN** the player's putting data includes one round aggregate for that round
- **AND** the aggregate includes the competition simple name and abbreviated round label
- **AND** the aggregate includes C1x and C2 metrics with successful count, total count, and percentage

#### Scenario: Rounds are not merged by display label
- **WHEN** two rounds have different round identities but equal competition or round display labels
- **THEN** the export contains separate aggregates for those rounds

#### Scenario: Overall percentage uses combined counts
- **WHEN** a player has putting attempts across multiple rounds
- **THEN** the overall percentage is calculated from the overall successful and total counts
- **AND** it is not calculated as an average of round percentages

#### Scenario: Round percentage uses round counts
- **WHEN** a round aggregate has a non-zero total count for a metric
- **THEN** that metric's percentage is calculated from successful count divided by total count for that round

#### Scenario: Zero-count metric has no percentage
- **WHEN** a total or round C1x or C2 metric has zero total attempts
- **THEN** its percentage is null or rendered as the existing no-value marker
- **AND** its successful and total counts remain zero

### Requirement: Personal putting round detail presentation
The Personal stats putting view SHALL display overall putting totals immediately and round details collapsed by default.

#### Scenario: Totals are visible by default
- **WHEN** the user opens the Personal stats putting view for a player with putting data
- **THEN** the overall C1x and C2 metrics are visible
- **AND** round details are hidden initially

#### Scenario: Round detail control is available
- **WHEN** the selected player's export contains one or more round aggregates
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
- **WHEN** a selected player's putting data has no round aggregates
- **THEN** the page still displays overall totals
- **AND** it does not display an empty round-details control or container

#### Scenario: Round detail control is keyboard accessible
- **WHEN** a keyboard user focuses and activates the show/hide control
- **THEN** the round details toggle in the same way as pointer activation
- **AND** the control exposes its current expanded state through `aria-expanded`

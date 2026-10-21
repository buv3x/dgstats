## MODIFIED Requirements

### Requirement: Personal putting totals and round aggregates
The personal statistics export SHALL provide combined C1x and C2 putting metrics from complete player rounds, separate putting metrics for every represented player round including incomplete rounds, and global C2+ and best-C1x summaries derived only from complete rounds.

#### Scenario: Overall putting totals remain available
- **WHEN** a player has exported putting attempts from complete rounds
- **THEN** the player's putting data includes overall C1x and C2 metrics
- **AND** each metric includes successful count, total count, and percentage

#### Scenario: Complete round aggregate is exported
- **WHEN** a player has a complete represented round
- **THEN** the player's putting data includes one round aggregate for that player round
- **AND** the aggregate includes `complete: true`
- **AND** the aggregate includes competition and abbreviated round labels
- **AND** the aggregate includes C1x and C2 metrics with successful count, total count, and percentage

#### Scenario: Incomplete round aggregate is exported
- **WHEN** a player round has a missing expected hole or an invalid hole sequence
- **THEN** the player's putting data includes one round aggregate for that player round
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
- **THEN** that metric's percentage is calculated from successful count divided by successful and unsuccessful attempts for that round

#### Scenario: Zero-count metric has no percentage
- **WHEN** a total or round C1x or C2 metric has zero total attempts
- **THEN** its percentage is null or rendered as the existing no-value marker
- **AND** its successful and total counts remain zero

#### Scenario: Global C2+ average is exported
- **WHEN** a player has one or more complete rounds
- **THEN** the putting data includes C2+ per round as the average of each complete round's successful C2+ count weighted to 18 holes
- **AND** the displayed value is rounded to two decimal places

#### Scenario: Global C2+ maximum is exported with real and display values
- **WHEN** a player has one or more complete rounds
- **THEN** the putting data includes the maximum normalized C2+ value
- **AND** it includes the exact real value, rounded display value, and tie count

#### Scenario: Best C1x round is exported globally
- **WHEN** a player has one or more complete rounds with C1x attempts
- **THEN** the putting data includes the highest exact C1x success percentage
- **AND** ties are resolved by the highest total C1x attempts
- **AND** the number of rounds sharing the winning percentage and total is included

#### Scenario: Incomplete rounds do not affect global summaries
- **WHEN** a player has complete and incomplete rounds
- **THEN** no attempt or metric from an incomplete round contributes to C2+ per round, most C2+ in a round, or best C1x round

#### Scenario: Existing round aggregate remains unchanged
- **WHEN** a player round aggregate is exported
- **THEN** its existing completeness, competition, round, C1x, and C2 fields remain present with their current meanings
- **AND** no new global metric is added to the round entry

### Requirement: Personal putting round detail presentation
The Personal stats putting view SHALL display overall C1x and C2 totals and the new global putting summaries immediately, while keeping represented round details collapsed by default and omitting incomplete rounds that have no recorded putting attempts.

#### Scenario: Global summaries are visible by default
- **WHEN** the user opens the Personal stats putting view for a player with new global putting metrics
- **THEN** the view displays C2+ per round, most C2+ in a round, and best C1x round alongside the existing C1x and C2 totals
- **AND** round details remain hidden initially

#### Scenario: Totals are visible by default
- **WHEN** the user opens the Personal stats putting view for a player with putting data
- **THEN** the overall C1x and C2 metrics are visible
- **AND** round details are hidden initially

#### Scenario: Empty incomplete round is hidden
- **WHEN** an exported round has `complete: false` and both its C1x and C2 total counts are zero
- **THEN** the round is omitted from the rendered round-detail list
- **AND** the round does not cause an empty round-details control or container to be displayed

#### Scenario: Incomplete round with recorded attempts is styled as excluded
- **WHEN** an expanded round aggregate has `complete: false` and either its C1x or C2 total count is greater than zero
- **THEN** its entire round-detail line is rendered in italic text
- **AND** its entire round-detail line has strikethrough styling

#### Scenario: Complete round remains visible
- **WHEN** an exported round has `complete: true`
- **THEN** the round is rendered even when both metrics have zero totals

#### Scenario: Legacy round without completeness metadata
- **WHEN** an older exported round aggregate has no `complete` field
- **THEN** the UI treats it as complete for rendering compatibility
- **AND** the round remains visible

#### Scenario: Round detail control is available
- **WHEN** the selected player's export contains one or more displayable round aggregates
- **THEN** the view displays a show/hide round-details control
- **AND** the control indicates a collapsed state initially

#### Scenario: All rounds are filtered
- **WHEN** the selected player's export contains only incomplete rounds with zero C1x and C2 totals
- **THEN** the page still displays overall totals
- **AND** it does not display an empty round-details control or container

#### Scenario: Missing new fields remain backward compatible
- **WHEN** an older exported player file has no new global putting fields
- **THEN** the view still displays the existing C1x and C2 totals
- **AND** it does not display fabricated new metric values

#### Scenario: Round detail presentation remains unchanged
- **WHEN** round details are expanded
- **THEN** round lines continue to display only their existing C1x and C2 metrics

#### Scenario: Round details expand
- **WHEN** the user activates the show/hide round-details control while details are collapsed
- **THEN** the displayable round aggregates become visible
- **AND** the control indicates an expanded state

#### Scenario: Round details collapse
- **WHEN** the user activates the show/hide round-details control while details are expanded
- **THEN** the displayable round aggregates become hidden
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
- **WHEN** a selected player's putting data has no displayable round aggregates
- **THEN** the page still displays overall totals
- **AND** it does not display an empty round-details control or container

#### Scenario: Round detail control is keyboard accessible
- **WHEN** a keyboard user focuses and activates the show/hide control
- **THEN** the round details toggle in the same way as pointer activation
- **AND** the control exposes its current expanded state through `aria-expanded`

#### Scenario: Existing round detail behavior is preserved
- **WHEN** the selected player's export contains represented round aggregates
- **THEN** the view retains the current collapsed show/hide behavior, incomplete-round filtering, semantic emphasis, and keyboard-accessible toggle

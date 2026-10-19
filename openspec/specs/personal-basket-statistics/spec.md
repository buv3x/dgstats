# personal-basket-statistics Specification

## Purpose

Define how eligible personal basket statistics are selected, calculated, ordered, and formatted for export and display.
## Requirements
### Requirement: Eligible player lookup
The system SHALL provide a player lookup data set for selecting players who have personal basket statistics or imported putting statistics.

#### Scenario: Eligible player is listed
- **WHEN** a player has at least one personally eligible basket variation result
- **THEN** the player lookup data includes that player

#### Scenario: Player with eligible hole statistics is listed
- **WHEN** a player has at least one personally eligible basket variation result
- **THEN** the player lookup data includes that player

#### Scenario: Player with imported throws is listed
- **WHEN** a player has imported throw data represented in the personal statistics export
- **THEN** the player lookup data includes that player even if the player has no eligible basket variation result

#### Scenario: Ineligible player is omitted
- **WHEN** a player has neither a personally eligible basket variation result nor imported throw data
- **THEN** the player lookup data does not include that player

#### Scenario: Player display label includes PDGA number
- **WHEN** a listed player has a PDGA number
- **THEN** the lookup display label includes the player name and PDGA number

#### Scenario: Player display label without PDGA number
- **WHEN** a listed player has no PDGA number
- **THEN** the lookup display label includes the player name without a PDGA number suffix

### Requirement: Global variation regression eligibility
The system SHALL calculate personal statistics only from basket variations with a usable global regression line.

#### Scenario: Variation with enough data is eligible
- **WHEN** a basket variation has at least 50 mapped hole-score results with non-null round result ratings, non-zero rating variance, and a negative unweighted score-over-rating regression slope
- **THEN** the system treats the basket variation as globally eligible for personal statistics

#### Scenario: Sparse global variation is omitted
- **WHEN** a basket variation has fewer than 50 mapped hole-score results with non-null round result ratings
- **THEN** the system omits the basket variation from personal statistics

#### Scenario: Global variation without rating variance is omitted
- **WHEN** all mapped rated results for a basket variation have the same rating
- **THEN** the system omits the basket variation from personal statistics

#### Scenario: Global variation with unusable slope is omitted
- **WHEN** a basket variation's unweighted score-over-rating regression slope is zero or positive
- **THEN** the system omits the basket variation from personal statistics

### Requirement: Personal variation rating calculation
The system SHALL calculate a player's basket variation rating from the inverse of the global variation regression line.

#### Scenario: Personal variation row is eligible
- **WHEN** a selected player has at least two mapped rated scores on a globally eligible basket variation
- **THEN** the system includes a personal statistics row for that player and variation

#### Scenario: Personal variation row with one result is omitted
- **WHEN** a selected player has fewer than two mapped rated scores on a globally eligible basket variation
- **THEN** the system omits that player and variation row

#### Scenario: Per-score rating uses global regression inverse
- **WHEN** a personal score belongs to a globally eligible basket variation with regression `score = intercept + slope * rating`
- **THEN** the score's calculated rating is `(score - intercept) / slope`

#### Scenario: Personal average uses equal weighting
- **WHEN** a personal statistics row has multiple calculated score ratings
- **THEN** the row rating is the arithmetic average of those calculated ratings with each recorded score weighted equally

### Requirement: Personal variation list ordering and formatting
The system SHALL expose personal basket variation rows as one best-to-worst list.

#### Scenario: Rows are ordered by calculated rating
- **WHEN** personal basket variation rows are displayed for a selected player
- **THEN** rows are ordered by decimal calculated rating from highest to lowest

#### Scenario: Row rating is displayed as integer
- **WHEN** a personal basket variation row is displayed
- **THEN** the calculated rating is rounded to the nearest integer for display

#### Scenario: Decimal rating is preserved for ordering
- **WHEN** multiple personal basket variation rows are sorted
- **THEN** sorting uses the unrounded decimal calculated rating

#### Scenario: Personal count is displayed
- **WHEN** a personal basket variation row is displayed
- **THEN** it displays the number of recorded scores for that selected player and variation

#### Scenario: Scores are displayed chronologically
- **WHEN** a personal basket variation row is displayed
- **THEN** it displays that player's scores for the variation as a comma-separated list sorted by round date ascending and round id ascending when dates are equal

### Requirement: Personal putting totals and round aggregates
The personal statistics export SHALL provide combined putting metrics from complete player rounds and separate putting metrics for every represented player round, including incomplete rounds.

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

### Requirement: Personal putting round detail presentation
The Personal stats putting view SHALL display overall putting totals immediately and represented round details collapsed by default, but SHALL omit incomplete rounds that have no recorded putting attempts. Overall and yearly aggregate C1x and C2 metrics SHALL be displayed on separate lines. Round-detail text SHALL use 14px typography while preserving its existing content, ordering, emphasis, and incomplete-round styling.

#### Scenario: Totals are visible by default
- **WHEN** the user opens the Personal stats putting view for a player with putting data
- **THEN** the overall C1x and C2 metrics are visible on separate lines
- **AND** round details are hidden initially

#### Scenario: Yearly totals use separate lines
- **WHEN** the user expands a yearly putting summary
- **THEN** that year's C1x and C2 metrics are displayed on separate lines

#### Scenario: Round details use reduced typography
- **WHEN** an expanded round aggregate is displayed
- **THEN** its round-detail text uses 14px font size
- **AND** its existing format and emphasis remain unchanged

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
- **WHEN** a keyboard user focuses and activates the round-details control
- **THEN** the round details toggle in the same way as pointer activation
- **AND** the control exposes its current expanded state through `aria-expanded`

### Requirement: Putting highlight round explanations
The personal putting export SHALL provide the matching round descriptors for each available `mostC2PlusInRound` and `bestC1xRound` highlight. The Personal stats putting view SHALL render the complete displayed value of each available highlight as an accessible tooltip trigger whenever at least one matching round descriptor is present.

#### Scenario: Most C2+ highlight includes matching rounds
- **WHEN** a player or yearly aggregate has a most-C2+ highlight
- **THEN** its export data includes the matching rounds that produced the displayed maximum
- **AND** the matching-round list follows the existing aggregate round ordering

#### Scenario: Best C1x highlight includes matching rounds
- **WHEN** a player or yearly aggregate has a best-C1x highlight
- **THEN** its export data includes the matching rounds that produced the displayed best percentage and tie-break result
- **AND** the matching-round list follows the existing aggregate round ordering

#### Scenario: Most C2+ value is a tooltip trigger
- **WHEN** a most-C2+ highlight has at least one matching round descriptor
- **THEN** the complete displayed value, including the maximum and occurrence suffix when present, is rendered as a focusable tooltip trigger
- **AND** the tooltip lists all matching rounds

#### Scenario: Best C1x value is a tooltip trigger
- **WHEN** a best-C1x highlight has at least one matching round descriptor
- **THEN** the complete displayed value, including the percentage, counts, and occurrence suffix when present, is rendered as a focusable tooltip trigger
- **AND** the tooltip lists all matching rounds

#### Scenario: Single occurrence still has a tooltip
- **WHEN** either highlight has exactly one matching round descriptor
- **THEN** its complete displayed value is still rendered as a tooltip trigger
- **AND** the tooltip lists that one round

#### Scenario: Legacy highlight data remains readable
- **WHEN** an older export contains a highlight without matching-round descriptors
- **THEN** the existing visible highlight value is rendered without a tooltip
- **AND** no empty tooltip trigger is created

### Requirement: Longest throw-in export

The personal statistics export SHALL provide an overall and yearly longest-throw-in highlight when at least one complete-round successful throw-in has a recorded distance. The highlight SHALL include the displayed whole-meter distance, the exact-feet tie count, and one occurrence descriptor per matching successful throw with competition, abbreviated round, round identity, and hole ordinal.

#### Scenario: Overall longest throw-in is exported
- **WHEN** a player has at least one complete-round successful throw-in with a recorded distance
- **THEN** the overall putting summary contains the longest-throw-in highlight
- **AND** the highlight contains the rounded meter display value and matching occurrence descriptors

#### Scenario: Yearly longest throw-in is exported
- **WHEN** a year contains at least one complete-round successful throw-in with a recorded distance
- **THEN** that yearly putting summary contains its longest-throw-in highlight

#### Scenario: No qualifying distance produces no highlight
- **WHEN** no complete-round successful throw-in has a recorded distance
- **THEN** the corresponding overall or yearly longest-throw-in highlight is absent

#### Scenario: Occurrences identify holes
- **WHEN** the exact maximum feet value occurs more than once
- **THEN** the exported occurrence list contains every matching competition, round, and hole
- **AND** the occurrence count equals the number of matching successful throws

### Requirement: Longest throw-in presentation

The Personal stats putting view SHALL display the longest throw-in using whole meters and SHALL use the existing highlight occurrence suffix convention. When occurrence descriptors are available, the complete displayed value SHALL be an accessible tooltip trigger whose tooltip lists competition, round, and hole for every occurrence.

#### Scenario: Single longest throw-in is displayed
- **WHEN** an overall or yearly longest-throw-in highlight has one occurrence
- **THEN** the view displays the rounded meter value without a multiple-occurrence suffix
- **AND** the value is a focusable tooltip trigger listing the occurrence

#### Scenario: Tied longest throw-ins are displayed
- **WHEN** an overall or yearly highlight has multiple exact-feet occurrences
- **THEN** the view displays the rounded meter value followed by the existing occurrence suffix
- **AND** the tooltip lists every competition, round, and hole occurrence

#### Scenario: Legacy export remains readable
- **WHEN** an older putting export has no longest-throw-in field or has no occurrence descriptors
- **THEN** the page does not create an empty tooltip trigger
- **AND** existing putting highlights remain readable

### Requirement: Concise putting highlight tooltips

The Personal stats putting view SHALL render tooltip content for Most C2+, Best C1x, and Longest throw-in highlights as context entries only. It SHALL omit the metric name, displayed metric value, and section headings such as `Rounds:` or `Occurrences:`. Each matching round or occurrence SHALL appear on a separate line.

#### Scenario: Round highlight tooltip contains context lines
- **WHEN** a Most C2+ or Best C1x highlight has matching round descriptors
- **THEN** the tooltip contains one competition-and-round line for each matching round
- **AND** the tooltip does not contain the metric label, displayed value, or a `Rounds:` heading

#### Scenario: Longest throw-in tooltip contains occurrence lines
- **WHEN** a Longest throw-in highlight has matching occurrence descriptors
- **THEN** the tooltip contains one competition, round, and hole line for each occurrence
- **AND** the tooltip does not contain the metric label, displayed value, or an `Occurrences:` heading

#### Scenario: Multiple entries remain separated
- **WHEN** a highlight has multiple matching rounds or occurrences
- **THEN** each entry is separated by a newline
- **AND** no entries are merged into one line

#### Scenario: Accessible trigger context is preserved
- **WHEN** a putting highlight is rendered as a tooltip trigger
- **THEN** its accessible label continues to identify the metric and displayed value
- **AND** the concise tooltip content is used only as the tooltip text


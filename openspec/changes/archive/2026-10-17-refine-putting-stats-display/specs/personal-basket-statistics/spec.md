## MODIFIED Requirements

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

## ADDED Requirements

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

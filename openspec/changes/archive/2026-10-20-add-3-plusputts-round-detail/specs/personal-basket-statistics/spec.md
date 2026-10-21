## MODIFIED Requirements

### Requirement: Personal putting round detail presentation

The Personal stats putting view SHALL display overall putting totals immediately and represented round details collapsed by default, but SHALL omit incomplete rounds that have no recorded putting attempts or three-plus-putt lost throws. Overall and yearly aggregate C1x and C2 metrics SHALL be displayed on separate lines. Round-detail text SHALL use 14px typography while preserving its existing content, ordering, emphasis, and incomplete-round styling. A displayed round detail SHALL append `3+putts: N` after the C2 metric, separated by a comma, only when the round's `threePlusPuttLostThrows` value is greater than zero.

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

- **WHEN** an exported round has `complete: false`, both its C1x and C2 total counts are zero, and its three-plus-putt count is zero or absent
- **THEN** the round is omitted from the rendered round-detail list
- **AND** the round does not cause an empty round-details control or container to be displayed

#### Scenario: Incomplete round with recorded attempts is styled as excluded

- **WHEN** an expanded round aggregate has `complete: false` and either its C1x or C2 total count is greater than zero
- **THEN** its entire round-detail line is rendered in italic text
- **AND** its entire round-detail line has strikethrough styling

#### Scenario: Incomplete round with three-plus-putts is visible

- **WHEN** an exported round has `complete: false`, both its C1x and C2 total counts are zero, and its three-plus-putt count is greater than zero
- **THEN** the round is included in the rendered round-detail list
- **AND** its entire round-detail line is rendered in italic text
- **AND** its entire round-detail line has strikethrough styling

#### Scenario: Complete round remains visible

- **WHEN** an exported round has `complete: true`
- **THEN** the round is rendered even when all three metrics have zero counts

#### Scenario: Legacy round without completeness metadata

- **WHEN** an older exported round aggregate has no `complete` field
- **THEN** the UI treats it as complete for rendering compatibility
- **AND** the round remains visible

#### Scenario: Round detail control is available

- **WHEN** the selected player's export contains one or more displayable round aggregates
- **THEN** the view displays a show/hide round-details control
- **AND** the control indicates a collapsed state initially

#### Scenario: All rounds are filtered

- **WHEN** the selected player's export contains only incomplete rounds with zero C1x totals, zero C2 totals, and no three-plus-putt lost throws
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

- **WHEN** an expanded round aggregate has no three-plus-putt lost throws
- **THEN** it uses the format `[competition.simple_name] [round.abbreviated_label]: [C1x stats], [C2 stats]`
- **AND** the competition name and round label are bold
- **AND** the `C1x` and `C2` labels are bold

#### Scenario: Positive three-plus-putt value is appended

- **WHEN** an expanded round aggregate has `threePlusPuttLostThrows` greater than zero
- **THEN** it uses the format `[competition.simple_name] [round.abbreviated_label]: [C1x stats], [C2 stats], 3+putts: N`
- **AND** `N` is the round's three-plus-putt lost-throw count

#### Scenario: Missing or zero three-plus-putt value is omitted

- **WHEN** an expanded round aggregate has no three-plus-putt field, a null/non-numeric value, or a value of zero
- **THEN** the round line omits `3+putts`
- **AND** the existing C1x and C2 content remains unchanged

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

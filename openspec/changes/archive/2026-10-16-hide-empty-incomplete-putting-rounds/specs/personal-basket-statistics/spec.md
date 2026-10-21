## MODIFIED Requirements

### Requirement: Personal putting round detail presentation
The Personal stats putting view SHALL display overall putting totals immediately and represented round details collapsed by default, but SHALL omit incomplete rounds that have no recorded putting attempts.

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
- **WHEN** the selected player’s export contains one or more displayable round aggregates
- **THEN** the view displays a show/hide round-details control
- **AND** the control indicates a collapsed state initially

#### Scenario: All rounds are filtered
- **WHEN** the selected player’s export contains only incomplete rounds with zero C1x and C2 totals
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

#### Scenario: Round detail control is keyboard accessible
- **WHEN** a keyboard user focuses and activates the show/hide control
- **THEN** the round details toggle in the same way as pointer activation
- **AND** the control exposes its current expanded state through `aria-expanded`

## ADDED Requirements

### Requirement: Basket stats chart first render sizing
The static page SHALL render the initially selected Basket stats chart using the visible chart container dimensions.

#### Scenario: Basket stats chart is first opened
- **WHEN** the user opens the Basket stats view after the page has loaded a selected basket course and variation
- **THEN** the Basket stats chart is drawn at the same full chart width used after changing the selected variation

### Requirement: Basket stats count bucket connectivity
The static page SHALL encode Basket stats sample-count buckets through point connectivity and line pattern.

#### Scenario: Low count windows are unconnected points
- **WHEN** a Basket stats window has count bucket `50-99`
- **THEN** its SPR and VAR values are displayed as chart points without a connecting line segment representing that window

#### Scenario: Medium count windows use dotted lines
- **WHEN** a Basket stats line segment portion represents count bucket `100-199`
- **THEN** that segment portion is displayed as a dotted line

#### Scenario: High count windows use normal lines
- **WHEN** a Basket stats line segment portion represents count bucket `200+`
- **THEN** that segment portion is displayed as a normal solid line

#### Scenario: Bucket changes split line eligibility at midpoint
- **WHEN** two adjacent Basket stats points have different count buckets
- **THEN** each half of the connection is rendered or omitted according to the count bucket of the endpoint it represents

### Requirement: Basket stats chart tooltip values
The static page SHALL keep Basket stats chart point tooltips limited to the hovered metric's rating and value.

#### Scenario: SPR point tooltip is shown
- **WHEN** the user hovers over an SPR point in the Basket stats chart
- **THEN** the tooltip displays the rating and SPR value for that point
- **AND** the tooltip does not display rating range, sample count, count bucket, or VAR

#### Scenario: VAR point tooltip is shown
- **WHEN** the user hovers over a VAR point in the Basket stats chart
- **THEN** the tooltip displays the rating and VAR value for that point
- **AND** the tooltip does not display rating range, sample count, count bucket, or SPR

#### Scenario: Close Basket stats points show compact rows
- **WHEN** the user hovers close to multiple Basket stats chart points
- **THEN** the tooltip contains one compact row per matching point using only that point's metric name, rating, and metric value

### Requirement: Basket stats chart rating axis
The static page SHALL render the Basket stats chart rating X-axis with explicit whole-number rating labels and grid lines.

#### Scenario: Rating axis label is shown
- **WHEN** the Basket stats chart is displayed
- **THEN** the X-axis label is `Rating`

#### Scenario: Rating ticks use twenty-point spacing
- **WHEN** the Basket stats chart is displayed
- **THEN** the X-axis displays whole-number rating labels at 20-rating-point intervals such as `880`, `900`, and `920`

#### Scenario: Rating ticks have vertical grid lines
- **WHEN** the Basket stats chart displays an X-axis rating label
- **THEN** a vertical grid line is displayed at that rating value

### Requirement: Basket stats chart Y axes
The static page SHALL render Basket stats SPR and VAR Y-axis labels according to fixed chart-specific tick rules.

#### Scenario: SPR axis labels and grid lines are shown
- **WHEN** the Basket stats chart is displayed
- **THEN** the left SPR Y-axis displays labels at `0`, `0.5`, `1`, and `1.5`
- **AND** a horizontal grid line is displayed at each of those SPR values

#### Scenario: VAR axis labels are shown without grid lines
- **WHEN** the Basket stats chart is displayed
- **THEN** the right VAR Y-axis displays labels at `0.5` and `1`
- **AND** no horizontal grid line is displayed solely for those VAR labels

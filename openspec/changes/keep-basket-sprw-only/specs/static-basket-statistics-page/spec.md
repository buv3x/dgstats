## MODIFIED Requirements

### Requirement: Basket stats SPR-only line chart
The static page SHALL display precomputed basket variation sliding-window SPRW values in Basket stats without displaying raw SPR, SPR2, or VAR.

#### Scenario: Selected variation SPRW windows are displayed
- **WHEN** the user selects a basket variation with exported SPRW windows
- **THEN** the Basket stats chart displays those windows as a single SPRW line chart

#### Scenario: X axis uses rating midpoint
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** the chart uses each window's `ratingMidpoint` as the X-axis value

#### Scenario: SPRW uses fixed left axis
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** SPRW values are plotted against the Y-axis fixed from `-0.5` to `2.0`

#### Scenario: Raw SPR series is not rendered
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** the chart does not render raw SPR line segments or raw SPR points

#### Scenario: SPR2 series is not rendered
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** the chart does not render SPR2 line segments or SPR2 points

#### Scenario: VAR series is not rendered
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** the chart does not render VAR line segments or VAR points

#### Scenario: VAR axis is not rendered
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** the chart does not render a VAR Y-axis, VAR tick labels, or a VAR axis label

#### Scenario: Basket stats chart labels mention only SPRW
- **WHEN** the Basket stats chart is displayed
- **THEN** the visible chart title, Y-axis label, legend, tooltip metric labels, and accessible chart label mention SPRW
- **AND** they do not mention raw SPR, SPR2, or VAR

#### Scenario: Empty window series is reported
- **WHEN** the selected basket variation has no exported windows
- **THEN** the page displays a no-window-results message instead of an empty Basket stats chart

### Requirement: Basket stats SPR-only count bucket connectivity
The static page SHALL encode Basket stats weighted sample-count buckets through point connectivity and line pattern for SPRW values only.

#### Scenario: Low weighted count SPRW windows are unconnected points
- **WHEN** a Basket stats window has `sprwCountBucket` of `50-99`
- **THEN** its SPRW value is displayed as a chart point without a connecting line segment representing that window

#### Scenario: Medium weighted count SPRW windows use dotted lines
- **WHEN** a Basket stats SPRW line segment portion represents `sprwCountBucket` of `100-199`
- **THEN** that segment portion is displayed as a dotted line

#### Scenario: High weighted count SPRW windows use normal lines
- **WHEN** a Basket stats SPRW line segment portion represents `sprwCountBucket` of `200+`
- **THEN** that segment portion is displayed as a normal solid line

#### Scenario: Bucket changes split SPRW line eligibility at midpoint
- **WHEN** two adjacent Basket stats SPRW points have different `sprwCountBucket` values
- **THEN** each half of the connection is rendered or omitted according to the `sprwCountBucket` of the endpoint it represents

#### Scenario: Raw count bucket is not used for connectivity
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** chart point connectivity and line pattern are based on `sprwCountBucket`, not `countBucket`

### Requirement: Basket stats SPR-only tooltip values
The static page SHALL keep Basket stats chart point tooltips limited to the hovered SPRW rating and value.

#### Scenario: SPRW point tooltip is shown
- **WHEN** the user hovers over an SPRW point in the Basket stats chart
- **THEN** the tooltip displays the rating and SPRW value for that point
- **AND** the tooltip does not display rating range, sample count, count bucket, raw SPR, SPR2, or VAR

#### Scenario: Close Basket stats SPRW points show compact rows
- **WHEN** the user hovers close to multiple Basket stats SPRW chart points
- **THEN** the tooltip contains one compact row per matching point using only the SPRW metric name, rating, and SPRW value

#### Scenario: Tooltip hides when no Basket stats SPRW points are hovered
- **WHEN** the pointer is not close to any Basket stats SPRW chart point
- **THEN** the Basket stats tooltip is hidden

### Requirement: Basket stats chart Y axes
The static page SHALL render Basket stats SPRW Y-axis labels according to fixed chart-specific tick rules.

#### Scenario: SPRW axis labels and grid lines are shown
- **WHEN** the Basket stats chart is displayed
- **THEN** the left SPRW Y-axis displays labels at `0`, `0.5`, `1`, and `1.5`
- **AND** a horizontal grid line is displayed at each of those SPRW values

#### Scenario: VAR axis labels are not shown
- **WHEN** the Basket stats chart is displayed
- **THEN** the chart does not display a right VAR Y-axis or VAR labels

## MODIFIED Requirements

### Requirement: Basket stats SPR-only line chart
The static page SHALL display precomputed basket variation sliding-window SPR and SPR2 values in Basket Stats without displaying VAR.

#### Scenario: Selected variation SPR windows are displayed
- **WHEN** the user selects a basket variation with exported windows
- **THEN** the Basket Stats chart displays those windows as an SPR line chart

#### Scenario: Selected variation SPR2 windows are displayed
- **WHEN** the user selects a basket variation with exported windows containing SPR2 values
- **THEN** the Basket Stats chart displays those SPR2 values as a second line on the same chart

#### Scenario: X axis uses rating midpoint
- **WHEN** Basket Stats sliding-window statistics are displayed
- **THEN** the chart uses each window's `ratingMidpoint` as the X-axis value

#### Scenario: SPR metrics use fixed left axis
- **WHEN** Basket Stats sliding-window statistics are displayed
- **THEN** SPR and SPR2 values are plotted against the Y-axis fixed from `-0.5` to `2.0`

#### Scenario: VAR series is not rendered
- **WHEN** Basket Stats sliding-window statistics are displayed
- **THEN** the chart does not render VAR line segments or VAR points

#### Scenario: VAR axis is not rendered
- **WHEN** Basket Stats sliding-window statistics are displayed
- **THEN** the chart does not render a VAR Y-axis, VAR tick labels, or a VAR axis label

#### Scenario: Basket stats chart labels mention SPR2
- **WHEN** the Basket Stats chart is displayed
- **THEN** the visible chart title, legend, and accessible chart label mention SPR and SPR2
- **AND** they do not mention VAR

#### Scenario: Empty window series is reported
- **WHEN** the selected basket variation has no exported windows
- **THEN** the page displays a no-window-results message instead of an empty Basket Stats chart

### Requirement: Basket stats SPR-only count bucket connectivity
The static page SHALL encode Basket Stats sample-count buckets through point connectivity and line pattern for SPR and SPR2 values.

#### Scenario: Low count SPR windows are unconnected points
- **WHEN** a Basket Stats window has count bucket `50-99`
- **THEN** its SPR value is displayed as a chart point without a connecting line segment representing that window

#### Scenario: Medium count SPR windows use dotted lines
- **WHEN** a Basket Stats SPR line segment portion represents count bucket `100-199`
- **THEN** that segment portion is displayed as a dotted line

#### Scenario: High count SPR windows use normal lines
- **WHEN** a Basket Stats SPR line segment portion represents count bucket `200+`
- **THEN** that segment portion is displayed as a normal solid line

#### Scenario: Bucket changes split SPR line eligibility at midpoint
- **WHEN** two adjacent Basket Stats SPR points have different count buckets
- **THEN** each half of the connection is rendered or omitted according to the count bucket of the endpoint it represents

#### Scenario: Low weighted count SPR2 windows are unconnected points
- **WHEN** a Basket Stats window has SPR2 count bucket `50-99`
- **THEN** its SPR2 value is displayed as a chart point without a connecting line segment representing that window

#### Scenario: Medium weighted count SPR2 windows use dotted lines
- **WHEN** a Basket Stats SPR2 line segment portion represents SPR2 count bucket `100-199`
- **THEN** that segment portion is displayed as a dotted line

#### Scenario: High weighted count SPR2 windows use normal lines
- **WHEN** a Basket Stats SPR2 line segment portion represents SPR2 count bucket `200+`
- **THEN** that segment portion is displayed as a normal solid line

#### Scenario: Bucket changes split SPR2 line eligibility at midpoint
- **WHEN** two adjacent Basket Stats SPR2 points have different SPR2 count buckets
- **THEN** each half of the connection is rendered or omitted according to the SPR2 count bucket of the endpoint it represents

### Requirement: Basket stats SPR-only tooltip values
The static page SHALL keep Basket Stats chart point tooltips limited to the hovered metric's rating and value.

#### Scenario: SPR point tooltip is shown
- **WHEN** the user hovers over an SPR point in the Basket Stats chart
- **THEN** the tooltip displays the rating and SPR value for that point
- **AND** the tooltip does not display rating range, sample count, count bucket, SPR2, or VAR

#### Scenario: SPR2 point tooltip is shown
- **WHEN** the user hovers over an SPR2 point in the Basket Stats chart
- **THEN** the tooltip displays the rating and SPR2 value for that point
- **AND** the tooltip does not display rating range, sample count, count bucket, SPR, or VAR

#### Scenario: Close Basket Stats SPR points show compact rows
- **WHEN** the user hovers close to multiple Basket Stats SPR or SPR2 chart points
- **THEN** the tooltip contains one compact row per matching point using only that point's metric name, rating, and metric value

#### Scenario: Tooltip hides when no Basket Stats SPR points are hovered
- **WHEN** the pointer is not close to any Basket Stats SPR or SPR2 chart point
- **THEN** the Basket Stats tooltip is hidden

## ADDED Requirements

### Requirement: Course stats SPR/VAR scatter chart filtered sample threshold
The static page SHALL display Course stats SPR/VAR scatter chart markers only for basket variations with at least 50 matching score samples after the current rating bounds are applied.

#### Scenario: Chart point is calculated after rating filtering
- **WHEN** the page calculates Course stats for a selected basket course and rating bounds
- **AND** a basket variation has at least 50 score samples matching those rating bounds
- **AND** the matching score samples do not all have the same rating
- **THEN** the Course stats SPR/VAR chart displays one marker for that basket variation

#### Scenario: Small filtered sample groups are omitted
- **WHEN** a basket variation has fewer than 50 score samples matching the selected Course stats rating bounds
- **THEN** the Course stats SPR/VAR chart omits that basket variation

#### Scenario: Course stats table keeps existing row eligibility
- **WHEN** a basket variation has at least one score sample matching the selected Course stats rating bounds
- **THEN** the Course stats table may display that basket variation row even when the Course stats SPR/VAR chart omits it for having fewer than 50 matching samples

#### Scenario: Course stats VAR remains displayed
- **WHEN** the Course stats SPR/VAR chart displays an eligible basket variation marker
- **THEN** the marker's vertical value is VAR calculated as the average absolute difference between actual score and the score expected from that variation's regression line

#### Scenario: Empty chart result is reported after threshold filtering
- **WHEN** the selected basket course and rating bounds produce no basket variations with at least 50 matching score samples and rating variance
- **THEN** the page displays a no-chart-results message instead of an empty Course stats chart

### Requirement: Basket stats SPR-only line chart
The static page SHALL display precomputed basket variation sliding-window SPR values in Basket stats without displaying VAR.

#### Scenario: Selected variation SPR windows are displayed
- **WHEN** the user selects a basket variation with exported windows
- **THEN** the Basket stats chart displays those windows as an SPR line chart

#### Scenario: X axis uses rating midpoint
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** the chart uses each window's `ratingMidpoint` as the X-axis value

#### Scenario: SPR uses fixed left axis
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** SPR values are plotted against the Y-axis fixed from `-0.5` to `2.0`

#### Scenario: VAR series is not rendered
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** the chart does not render VAR line segments or VAR points

#### Scenario: VAR axis is not rendered
- **WHEN** Basket stats sliding-window statistics are displayed
- **THEN** the chart does not render a VAR Y-axis, VAR tick labels, or a VAR axis label

#### Scenario: Basket stats chart labels omit VAR
- **WHEN** the Basket stats chart is displayed
- **THEN** the visible chart title, legend, and accessible chart label do not mention VAR

#### Scenario: Empty window series is reported
- **WHEN** the selected basket variation has no exported windows
- **THEN** the page displays a no-window-results message instead of an empty Basket stats chart

### Requirement: Basket stats SPR-only count bucket connectivity
The static page SHALL encode Basket stats sample-count buckets through point connectivity and line pattern for SPR values only.

#### Scenario: Low count SPR windows are unconnected points
- **WHEN** a Basket stats window has count bucket `50-99`
- **THEN** its SPR value is displayed as a chart point without a connecting line segment representing that window

#### Scenario: Medium count SPR windows use dotted lines
- **WHEN** a Basket stats SPR line segment portion represents count bucket `100-199`
- **THEN** that segment portion is displayed as a dotted line

#### Scenario: High count SPR windows use normal lines
- **WHEN** a Basket stats SPR line segment portion represents count bucket `200+`
- **THEN** that segment portion is displayed as a normal solid line

#### Scenario: Bucket changes split SPR line eligibility at midpoint
- **WHEN** two adjacent Basket stats SPR points have different count buckets
- **THEN** each half of the connection is rendered or omitted according to the count bucket of the endpoint it represents

### Requirement: Basket stats SPR-only tooltip values
The static page SHALL keep Basket stats chart point tooltips limited to the hovered SPR rating and value.

#### Scenario: SPR point tooltip is shown
- **WHEN** the user hovers over an SPR point in the Basket stats chart
- **THEN** the tooltip displays the rating and SPR value for that point
- **AND** the tooltip does not display rating range, sample count, count bucket, or VAR

#### Scenario: Close Basket stats SPR points show compact rows
- **WHEN** the user hovers close to multiple Basket stats SPR chart points
- **THEN** the tooltip contains one compact row per matching point using only the SPR metric name, rating, and SPR value

#### Scenario: Tooltip hides when no Basket stats SPR points are hovered
- **WHEN** the pointer is not close to any Basket stats SPR chart point
- **THEN** the Basket stats tooltip is hidden

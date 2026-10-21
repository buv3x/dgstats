## MODIFIED Requirements

### Requirement: Basket stats SPR-only line chart
The static page SHALL display precomputed basket variation sliding-window SPRW values in Hole stats without displaying raw SPR, SPR2, or VAR, and SHALL display the separate Score Distribution chart described below.

#### Scenario: Selected variation SPRW windows are displayed
- **WHEN** the user selects a hole variation with exported SPRW windows
- **THEN** the Hole stats view displays those windows as the SPRW line chart

#### Scenario: X axis uses rating midpoint
- **WHEN** Hole stats sliding-window statistics are displayed
- **THEN** the SPRW chart uses each window's `ratingMidpoint` as the X-axis value

#### Scenario: SPRW uses fixed left axis
- **WHEN** Hole stats sliding-window statistics are displayed
- **THEN** SPRW values are plotted against the Y-axis fixed from `-0.5` to `2.0`

#### Scenario: Raw SPR series is not rendered
- **WHEN** Hole stats sliding-window statistics are displayed
- **THEN** the SPRW chart does not render raw SPR line segments or raw SPR points

#### Scenario: SPR2 series is not rendered
- **WHEN** Hole stats sliding-window statistics are displayed
- **THEN** the SPRW chart does not render SPR2 line segments or SPR2 points

#### Scenario: VAR series is not rendered
- **WHEN** Hole stats sliding-window statistics are displayed
- **THEN** the SPRW chart does not render VAR line segments or VAR points

#### Scenario: VAR axis is not rendered
- **WHEN** Hole stats sliding-window statistics are displayed
- **THEN** the SPRW chart does not render a VAR Y-axis, VAR tick labels, or a VAR axis label

#### Scenario: SPRW chart labels mention only SPRW
- **WHEN** the SPRW chart is displayed
- **THEN** its visible chart title, Y-axis label, legend, tooltip metric labels, and accessible chart label mention SPRW
- **AND** they do not mention raw SPR, SPR2, or VAR

#### Scenario: Basket stats chart labels mention only SPRW
- **WHEN** the SPRW chart is displayed
- **THEN** its visible chart title, Y-axis label, legend, tooltip metric labels, and accessible chart label mention SPRW
- **AND** they do not mention raw SPR, SPR2, or VAR

#### Scenario: Empty window series is reported
- **WHEN** the selected hole variation has no exported windows
- **THEN** the page displays a no-window-results message instead of empty SPRW and Score Distribution charts

## ADDED Requirements

### Requirement: Hole stats Score Distribution chart
The static page SHALL display a Score Distribution chart below SPRW for a selected hole variation using the same chart dimensions, rating midpoints, and eligible windows as SPRW.

#### Scenario: Distribution chart is positioned below SPRW
- **WHEN** a selected hole variation has exported sliding-window statistics
- **THEN** the Score Distribution chart is displayed below the SPRW chart
- **AND** the chart has the same rendered size as SPRW

#### Scenario: Distribution uses required score buckets
- **WHEN** the Score Distribution chart is displayed
- **THEN** its possible series are `1-2`, `3`, `4`, `5`, `6`, `7`, and `8+`

#### Scenario: Distribution header tooltip is shown
- **WHEN** the user hovers over or focuses the Score Distribution header
- **THEN** the custom tooltip displays `Score distribution around a given rating`

### Requirement: Weighted score distribution calculation
The exporter SHALL calculate each window's score-bucket percentage using the same triangular rating weight as SPRW, with the selected window midpoint and the same rating range.

#### Scenario: Window percentages use triangular weights
- **WHEN** a score sample belongs to a window's SPRW rating range
- **THEN** its contribution to its score bucket is weighted by the triangular SPRW weight around that window's midpoint
- **AND** the bucket percentage equals the bucket's weighted contribution divided by total weighted contribution, multiplied by 100

#### Scenario: Distribution is calculated at five-point rating gaps
- **WHEN** sliding-window statistics are exported
- **THEN** distribution values are provided for the same rating midpoints as SPRW, separated by 5 rating points

#### Scenario: Distribution buckets sum to 100 percent
- **WHEN** a window has valid weighted distribution data
- **THEN** the percentages for all seven score buckets sum to 100 percent, subject to normal floating-point rounding

### Requirement: Bucket line presence and default visibility
The page SHALL render one Score Distribution line for every bucket with at least one score anywhere in the selected hole variation's complete sample set, and SHALL default-enable only buckets whose overall unweighted percentage is greater than 5%.

#### Scenario: Globally present bucket is rendered
- **WHEN** a bucket has at least one score in the selected hole variation's complete sample set
- **THEN** the chart renders that bucket's line across the available rating windows

#### Scenario: Globally absent bucket is not rendered
- **WHEN** a bucket has no score in the selected hole variation's complete sample set
- **THEN** the chart does not render a line or toggle for that bucket

#### Scenario: Low-share present bucket starts disabled
- **WHEN** a present bucket's overall unweighted percentage is less than or equal to 5%
- **THEN** its line is initially hidden
- **AND** its toggle is available to enable the line

#### Scenario: Common present bucket starts enabled
- **WHEN** a present bucket's overall unweighted percentage is greater than 5%
- **THEN** its line is initially visible

#### Scenario: Zero local percentage remains part of a present line
- **WHEN** a globally present bucket has a zero weighted percentage in an individual rating window
- **THEN** the bucket line remains the same series and plots that window at zero rather than being removed

### Requirement: Distribution series toggles and colors
The page SHALL provide an independent accessible toggle for each rendered score bucket and SHALL use fixed colors across all holes.

#### Scenario: Bucket line is toggled off
- **WHEN** the user activates an enabled bucket toggle
- **THEN** that bucket's line and points are hidden
- **AND** all other bucket series retain their visibility state

#### Scenario: Bucket line is toggled on
- **WHEN** the user activates a disabled bucket toggle
- **THEN** that bucket's line and points are displayed

#### Scenario: Fixed bucket colors are used
- **WHEN** score-bucket series are displayed
- **THEN** `1-2` uses dark blue, `3` light blue, `4` green, `5` yellow, `6` orange, `7` magenta, and `8+` dark red
- **AND** each bucket uses the same color in lines, points, and legend controls across all holes

### Requirement: Distribution confidence styling
The Score Distribution chart SHALL use each window's SPRW weighted-count bucket for point connectivity and line pattern.

#### Scenario: Low-confidence distribution windows are unconnected
- **WHEN** a distribution window has `sprwCountBucket` of `50-99`
- **THEN** its bucket values are displayed as points without connecting line segments

#### Scenario: Medium-confidence distribution segments are dotted
- **WHEN** a distribution segment represents `sprwCountBucket` of `100-199`
- **THEN** that segment is displayed as a dotted line

#### Scenario: High-confidence distribution segments are solid
- **WHEN** a distribution segment represents `sprwCountBucket` of `200+`
- **THEN** that segment is displayed as a solid line

### Requirement: Distribution percentage axis and interaction
The Score Distribution chart SHALL use a fixed percentage Y-axis and SHALL expose rating and percentage values through chart tooltips.

#### Scenario: Percentage axis is displayed
- **WHEN** the Score Distribution chart is displayed
- **THEN** its Y-axis spans 0% through 100% and uses percentage labels

#### Scenario: Distribution point tooltip is shown
- **WHEN** the user hovers over a Score Distribution point
- **THEN** the tooltip displays the bucket, rating midpoint, and weighted percentage for that point

#### Scenario: Distribution tooltip hides when not targeted
- **WHEN** the pointer is not close to a Score Distribution point
- **THEN** the distribution chart tooltip is hidden

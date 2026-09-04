## ADDED Requirements

### Requirement: Basket statistics view navigation
The static page SHALL provide top-level navigation between the current Course stats view and the Basket stats view.

#### Scenario: Course stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Course stats`

#### Scenario: Basket stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Basket stats`

#### Scenario: Course stats preserves current display
- **WHEN** the user selects `Course stats`
- **THEN** the page displays the existing course-level statistics controls, SPR/VAR scatter chart, and table

#### Scenario: Basket stats switches to basket controls
- **WHEN** the user selects `Basket stats`
- **THEN** the page displays Basket stats course and basket variation controls instead of the Course stats rating controls and table

### Requirement: Basket stats data loading
The static page SHALL load Basket stats data for the selected course using the manifest-provided basket stats path.

#### Scenario: Basket stats course selector is populated
- **WHEN** the statistics manifest loads successfully
- **THEN** the Basket stats course selector is populated using exported basket course names that have basket stats data

#### Scenario: Selected basket stats file is loaded
- **WHEN** the user selects a basket course in Basket stats
- **THEN** the page fetches that course's basket stats file using the manifest-provided `basketStatsPath`

#### Scenario: Basket variation selector is populated
- **WHEN** a selected course's basket stats file loads successfully
- **THEN** the page populates a basket variation selector from the exported eligible basket variations

#### Scenario: Missing basket stats file is reported
- **WHEN** the page cannot load the selected course's basket stats file
- **THEN** it displays a clear selected-course basket-stats missing-data message

#### Scenario: Empty basket variation list is reported
- **WHEN** a selected course has no basket variations with eligible sliding-window statistics
- **THEN** the page displays a no-basket-variations message instead of an empty chart

### Requirement: Basket stats SPR/VAR line chart
The static page SHALL display precomputed basket variation SPR and VAR sliding-window statistics as a dual-axis line chart.

#### Scenario: Selected variation windows are displayed
- **WHEN** the user selects a basket variation with exported windows
- **THEN** the page displays that variation's windows on a line chart

#### Scenario: X axis uses rating midpoint
- **WHEN** basket sliding-window statistics are displayed
- **THEN** the chart uses each window's `ratingMidpoint` as the X-axis value

#### Scenario: SPR uses left fixed axis
- **WHEN** basket sliding-window statistics are displayed
- **THEN** SPR values are plotted against the left Y-axis fixed from `-0.5` to `2.0`

#### Scenario: VAR uses right fixed axis
- **WHEN** basket sliding-window statistics are displayed
- **THEN** VAR values are plotted against the right Y-axis fixed from `0.0` to `1.5`

#### Scenario: SPR and VAR use different colors
- **WHEN** basket sliding-window statistics are displayed
- **THEN** SPR and VAR are rendered as two differently colored lines

#### Scenario: Empty window series is reported
- **WHEN** the selected basket variation has no exported windows
- **THEN** the page displays a no-window-results message instead of an empty chart

### Requirement: Basket stats count bucket styling
The static page SHALL style Basket stats line segments according to each window's exported sample-count bucket.

#### Scenario: Low count bucket is dotted
- **WHEN** a line segment portion represents a window with count bucket `50-99`
- **THEN** that segment portion is displayed as a dotted line

#### Scenario: Medium count bucket is normal
- **WHEN** a line segment portion represents a window with count bucket `100-199`
- **THEN** that segment portion is displayed as a normal line

#### Scenario: High count bucket is bold
- **WHEN** a line segment portion represents a window with count bucket `200+`
- **THEN** that segment portion is displayed as a bold line

#### Scenario: Style changes at midpoint between different buckets
- **WHEN** two adjacent chart points have different count buckets
- **THEN** the connecting line changes style at the midpoint between those points

### Requirement: Basket stats tooltip behavior
The static page SHALL expose exact sliding-window values through chart tooltips.

#### Scenario: Hovering a chart point shows window details
- **WHEN** the user hovers over a Basket stats chart point
- **THEN** the page displays a tooltip with rating range, rating midpoint, sample count, count bucket, SPR, and VAR

#### Scenario: Hovering close points shows all matches
- **WHEN** the user hovers close to multiple Basket stats chart points
- **THEN** the page displays one tooltip containing details for every hovered matching point

#### Scenario: Tooltip hides when no basket stats points are hovered
- **WHEN** the pointer is not close to any Basket stats chart point
- **THEN** the Basket stats tooltip is hidden

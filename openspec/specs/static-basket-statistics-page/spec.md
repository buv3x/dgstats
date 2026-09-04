# static-basket-statistics-page Specification

## Purpose
TBD - created by archiving change add-static-basket-statistics-export. Update Purpose after archive.
## Requirements
### Requirement: Static basket statistics assets
The system SHALL provide a GitHub Pages-ready static basket statistics page with separated assets.

#### Scenario: Static page file exists
- **WHEN** the static visualization files are reviewed
- **THEN** `docs/index.html` provides the basket statistics page

#### Scenario: Static assets are separated
- **WHEN** the static visualization files are reviewed
- **THEN** the page behavior and styling are placed in separate JavaScript and CSS files under `docs/`

#### Scenario: Manifest is loaded from relative data path
- **WHEN** the static basket statistics page loads
- **THEN** it fetches the statistics manifest from `data/statistics.json`

#### Scenario: Selected course data is loaded from manifest path
- **WHEN** the user selects a basket course from the manifest
- **THEN** the page fetches that course's statistics file using the manifest-provided relative path

### Requirement: Statistics controls
The static page SHALL provide spreadsheet-like controls for selecting basket course and applying rating bounds.

#### Scenario: Course selector is populated
- **WHEN** the statistics manifest loads successfully
- **THEN** the page populates a basket course selector using exported basket course names

#### Scenario: Competition selector is not displayed
- **WHEN** the statistics page is displayed
- **THEN** the page does not display a competition filter

#### Scenario: Rating inputs are available
- **WHEN** the statistics page is displayed
- **THEN** the page provides numeric Rating from and Rating to inputs

#### Scenario: Filter button applies controls
- **WHEN** the user clicks the Filter button
- **THEN** the page recalculates displayed statistics using the selected basket course and rating inputs

### Requirement: Rating filter semantics
The static page SHALL filter samples by `round_result.rating` values exported in the selected basket course statistics file.

#### Scenario: Both rating bounds are inclusive
- **WHEN** the user filters with Rating from and Rating to values
- **THEN** the page includes samples with ratings greater than or equal to Rating from and less than or equal to Rating to

#### Scenario: Empty lower bound is open-ended
- **WHEN** Rating from is empty and Rating to has a value
- **THEN** the page includes samples with ratings less than or equal to Rating to

#### Scenario: Empty upper bound is open-ended
- **WHEN** Rating from has a value and Rating to is empty
- **THEN** the page includes samples with ratings greater than or equal to Rating from

#### Scenario: Empty bounds include all rated samples
- **WHEN** both rating inputs are empty
- **THEN** the page includes all exported samples for the selected basket course

### Requirement: Basket variation statistics table
The static page SHALL display aggregated score statistics grouped by basket and basket variation for the selected basket course.

#### Scenario: All competitions are aggregated for selected course
- **WHEN** the page calculates statistics for a selected basket course
- **THEN** it aggregates matching score samples across all competitions represented in the selected course statistics file

#### Scenario: Basket rows are grouping rows
- **WHEN** statistics are displayed
- **THEN** each basket appears as a grouping row with no score statistics

#### Scenario: Variation rows contain statistics
- **WHEN** statistics are displayed
- **THEN** each basket variation row displays Count, Average, and score bucket percentages

#### Scenario: Basket and variation rows are sorted by id
- **WHEN** statistics are displayed
- **THEN** basket groups are sorted by basket id and variation rows are sorted by variation id

#### Scenario: Empty rows are hidden
- **WHEN** a basket variation has no samples after filtering
- **THEN** that variation row is not displayed

#### Scenario: Empty basket groups are hidden
- **WHEN** all variations for a basket have no samples after filtering
- **THEN** that basket group is not displayed

### Requirement: Spreadsheet-style statistic formatting
The static page SHALL format statistics to match the spreadsheet-style table.

#### Scenario: Count is row-level sample count
- **WHEN** a variation row is displayed
- **THEN** Count equals the number of included hole-score samples for that variation

#### Scenario: Average is rounded to three decimals
- **WHEN** a variation row is displayed
- **THEN** Average is shown rounded to 3 decimal places

#### Scenario: Score buckets are displayed as percentages
- **WHEN** a variation row is displayed
- **THEN** columns `1-2`, `3`, `4`, `5`, `6`, `7`, and `8+` show percentages rounded to 1 decimal place

#### Scenario: Low score bucket includes scores up to two
- **WHEN** a score is less than or equal to 2
- **THEN** it contributes to the `1-2` bucket

#### Scenario: High score bucket includes scores eight and above
- **WHEN** a score is greater than or equal to 8
- **THEN** it contributes to the `8+` bucket

#### Scenario: Zero percentages are displayed
- **WHEN** a bucket has no matching samples for a displayed variation row
- **THEN** the bucket displays `0.0`

### Requirement: Static page empty and metadata states
The static page SHALL communicate manifest state, selected course file state, and empty filter results.

#### Scenario: Snapshot freshness is displayed
- **WHEN** the statistics manifest includes export time metadata
- **THEN** the page displays the snapshot export time

#### Scenario: Missing manifest is reported
- **WHEN** the page cannot load `data/statistics.json`
- **THEN** it displays a clear missing-data message

#### Scenario: Missing selected course data is reported
- **WHEN** the page cannot load the selected course statistics file
- **THEN** it displays a clear selected-course missing-data message

#### Scenario: Empty course list is reported
- **WHEN** the manifest contains no basket courses with statistics
- **THEN** the page displays a no-courses message instead of the statistics table

#### Scenario: Empty filter result is reported
- **WHEN** the selected basket course and rating filter produce no visible variation rows
- **THEN** the page displays a no-results message instead of an empty statistics table

### Requirement: SPR/VAR scatter chart
The static page SHALL display a scatter chart for the selected basket course showing basket variation SPR against VAR using the current rating bounds.

#### Scenario: Chart uses selected course and rating filters
- **WHEN** the page calculates statistics for a selected basket course and rating bounds
- **THEN** the chart uses the same selected course score samples that match those rating bounds

#### Scenario: Chart point is calculated per basket variation
- **WHEN** a basket variation has at least 10 matching score samples and rating values are not all identical
- **THEN** the chart displays one point for that basket variation

#### Scenario: Small sample groups are omitted
- **WHEN** a basket variation has fewer than 10 matching score samples
- **THEN** the chart omits that basket variation

#### Scenario: Groups without rating variance are omitted
- **WHEN** all matching score samples for a basket variation have the same rating
- **THEN** the chart omits that basket variation

#### Scenario: SPR is shown on horizontal axis
- **WHEN** a basket variation chart point is calculated
- **THEN** its horizontal value is `-100` multiplied by the linear regression slope of score over rating

#### Scenario: VAR is shown on vertical axis
- **WHEN** a basket variation chart point is calculated
- **THEN** its vertical value is the average absolute difference between actual score and the score expected from that variation's regression line

#### Scenario: SPR axis uses fixed range
- **WHEN** the SPR/VAR chart is displayed
- **THEN** the horizontal axis range is fixed from `-0.25` to `1.75`

#### Scenario: VAR axis uses fixed range
- **WHEN** the SPR/VAR chart is displayed
- **THEN** the vertical axis range is fixed from `0.25` to `1.25`

#### Scenario: Off-scale chart point is shown as edge arrow
- **WHEN** an eligible basket variation has an SPR or VAR value outside the fixed chart ranges
- **THEN** the chart displays that basket variation as a small arrow on the nearest chart edge instead of hiding it

#### Scenario: Edge arrow points toward actual value
- **WHEN** an eligible basket variation is displayed as an edge arrow
- **THEN** the arrow points in the direction of that variation's actual off-chart SPR and VAR values

#### Scenario: Empty chart result is reported
- **WHEN** the selected basket course and rating bounds produce no eligible chart points
- **THEN** the page displays a no-chart-results message instead of an empty chart

### Requirement: SPR/VAR chart tooltip behavior
The static page SHALL keep the SPR/VAR chart uncluttered by rendering basket variation details in hover tooltips instead of persistent point or arrow labels.

#### Scenario: Points are displayed without persistent labels
- **WHEN** the SPR/VAR chart is displayed
- **THEN** basket variation names are not rendered as persistent labels next to chart points or edge arrows

#### Scenario: Hovering a point shows details
- **WHEN** the user hovers over a chart point
- **THEN** the page displays a tooltip with basket label, variation label, sample count, SPR, and VAR for that point

#### Scenario: Hovering an edge arrow shows details
- **WHEN** the user hovers over an edge arrow
- **THEN** the page displays a tooltip with basket label, variation label, sample count, SPR, and VAR for that arrow's basket variation

#### Scenario: Hovering close points shows all matches
- **WHEN** the user hovers close to multiple chart points or edge arrows
- **THEN** the page displays one tooltip containing details for every hovered matching marker

#### Scenario: Tooltip hides when no markers are hovered
- **WHEN** the pointer is not close to any chart point or edge arrow
- **THEN** the chart tooltip is hidden

### Requirement: SPR/VAR chart loading state
The static page SHALL communicate when SPR/VAR chart data is loading or recalculating.

#### Scenario: Chart loading is shown while course data loads
- **WHEN** selected basket course statistics are loading
- **THEN** the chart area displays a loading state

#### Scenario: Chart calculating is shown while values are recalculated
- **WHEN** selected course data is loaded and rating filter changes require chart values to be recalculated
- **THEN** the chart area displays a calculating state until the chart is updated

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


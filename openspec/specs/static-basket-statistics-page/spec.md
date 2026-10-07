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

#### Scenario: Basket and variation rows use explicit order
- **WHEN** statistics are displayed
- **THEN** basket groups with non-null `sortOrder` are ordered by ascending `sortOrder`, followed by baskets with null `sortOrder` ordered by basket ID
- **AND** variation rows use the same ordering rule with variation `sortOrder` and variation ID

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
The static page SHALL communicate manifest state, selected course file state, and empty filter results without displaying snapshot export-time metadata.

#### Scenario: Snapshot freshness is not displayed
- **WHEN** the statistics manifest includes export time metadata
- **THEN** the page does not display an `Exported at ...` label or the manifest export time in the page header

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
The static page SHALL provide top-level navigation between the Course stats view, Basket stats view, and Personal stats view.

#### Scenario: Course stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Course stats`

#### Scenario: Basket stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Basket stats`

#### Scenario: Personal stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Personal stats`

#### Scenario: Course stats preserves current display
- **WHEN** the user selects `Course stats`
- **THEN** the page displays the existing course-level statistics controls, SPR/VAR scatter chart, and table

#### Scenario: Basket stats switches to basket controls
- **WHEN** the user selects `Basket stats`
- **THEN** the page displays Basket stats course and basket variation controls instead of the Course stats rating controls and table

#### Scenario: Personal stats switches to player controls
- **WHEN** the user selects `Personal stats`
- **THEN** the page displays Personal stats player autocomplete controls and personal variation results instead of the Course stats and Basket stats controls

### Requirement: Personal stats data loading
The static page SHALL load personal statistics data using manifest-provided paths.

#### Scenario: Player lookup data is loaded
- **WHEN** the statistics manifest includes a player lookup path
- **THEN** the page fetches the player lookup data from that relative path

#### Scenario: Missing player lookup data is reported
- **WHEN** the page cannot load the player lookup data
- **THEN** it displays a clear personal-statistics missing-data message in the Personal stats view

#### Scenario: Empty player lookup is reported
- **WHEN** the player lookup data contains no eligible players
- **THEN** the Personal stats view displays a no-eligible-players message

#### Scenario: Selected player data is loaded
- **WHEN** the user selects a player from the Personal stats autocomplete
- **THEN** the page fetches that player's personal statistics file using the selected player's exported path

#### Scenario: Missing selected player data is reported
- **WHEN** the page cannot load the selected player's personal statistics file
- **THEN** it displays a clear selected-player missing-data message

### Requirement: Personal stats autocomplete
The static page SHALL provide autocomplete selection for eligible players and SHALL apply a committed player selection immediately after the browser has finalized the input value.

#### Scenario: Autocomplete uses eligible player labels
- **WHEN** player lookup data loads successfully
- **THEN** the Personal stats player input offers autocomplete options using exported player display labels

#### Scenario: Selecting a player applies exact identity
- **WHEN** the user selects an autocomplete option
- **THEN** the page uses that option's player id and personal statistics path rather than matching by free-form text alone

#### Scenario: Selecting a player updates immediately in Chromium
- **WHEN** the user selects an autocomplete option in Chrome or Edge
- **THEN** the page processes the committed option value without requiring another click, focus, or blur interaction

#### Scenario: Related input events do not duplicate loading
- **WHEN** selecting an option produces closely related `input` and `change` events
- **THEN** the page performs one effective player-selection update and does not issue duplicate personal-statistics requests for that selection

#### Scenario: Clearing player selection resets personal results
- **WHEN** the user clears the selected player input
- **THEN** the page hides the personal statistics list and shows an unselected state

### Requirement: Personal stats variation list
The static page SHALL display one list of personal basket variation ratings for the selected player, ordered by calculated rating from highest to lowest.

#### Scenario: Personal list displays rows
- **WHEN** a selected player's personal statistics file contains variation rows
- **THEN** the Personal stats view displays one table or list containing those rows

#### Scenario: Personal row fields are displayed
- **WHEN** a personal variation row is displayed
- **THEN** the row shows basket course, basket label, variation label, rounded rating, personal result count, and comma-separated scores

#### Scenario: Personal rows use calculated rating order
- **WHEN** personal variation rows are rendered
- **THEN** rows are ordered by exported decimal calculated rating from highest to lowest
- **AND** course, basket, and variation ordering is used only as deterministic tie-breaking

#### Scenario: Personal row order survives filtering
- **WHEN** Personal stats course or minimum-count filters are applied
- **THEN** the matching rows remain ordered by exported decimal calculated rating from highest to lowest

#### Scenario: Empty selected player results are reported
- **WHEN** the selected player's personal statistics file contains no variation rows
- **THEN** the Personal stats view displays a no-personal-results message instead of an empty list

### Requirement: Personal stats client-side filters
The static page SHALL provide Personal stats filters for basket course and minimum personal result count, applied only in the browser to the selected player's loaded personal statistics rows.

#### Scenario: Course selector uses selected player rows
- **WHEN** a selected player's personal statistics file loads with variation rows
- **THEN** the Personal stats course selector is populated with an all-courses option and only the basket courses present in those loaded rows

#### Scenario: Course filter is optional
- **WHEN** the Personal stats course selector is set to the all-courses option
- **THEN** the Personal stats list includes rows from every basket course in the selected player's loaded personal statistics rows that also match the minimum count filter, ordered by rating descending

#### Scenario: Selected course filters rows
- **WHEN** the user selects a basket course in the Personal stats course selector
- **THEN** the Personal stats list includes only selected-player variation rows whose `basketCourseId` matches the selected course and whose `count` matches the minimum count filter, ordered by rating descending

#### Scenario: Minimum count defaults to two
- **WHEN** the Personal stats controls are shown
- **THEN** the minimum count filter defaults to `2`

#### Scenario: Minimum count cannot be lower than two
- **WHEN** the user enters a minimum count value lower than `2`, empty, or invalid
- **THEN** the page normalizes the minimum count filter to `2` before applying it

#### Scenario: Minimum count filters rows
- **WHEN** the Personal stats minimum count filter is set to a value of `2` or greater
- **THEN** the Personal stats list includes only selected-player variation rows whose `count` is greater than or equal to that value and whose course matches the course filter, ordered by rating descending

#### Scenario: Empty filtered result is reported
- **WHEN** the selected player's personal statistics file contains variation rows but none match the active Personal stats filters
- **THEN** the Personal stats view displays a filtered-empty message instead of an empty table

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

### Requirement: Basket stats chart first render sizing
The static page SHALL render the initially selected Basket stats chart using the visible chart container dimensions.

#### Scenario: Basket stats chart is first opened
- **WHEN** the user opens the Basket stats view after the page has loaded a selected basket course and variation
- **THEN** the Basket stats chart is drawn at the same full chart width used after changing the selected variation

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

### Requirement: Public course selector ordering
All course select boxes on the public `docs/` statistics page SHALL order course options by total exported result count descending without displaying that count.

#### Scenario: Course stats selector uses result count
- **WHEN** the statistics manifest loads successfully
- **THEN** the Course stats selector orders courses by descending `sampleCount`
- **AND** option labels contain only the course name

#### Scenario: Basket stats selector uses result count
- **WHEN** the statistics manifest loads successfully
- **THEN** the Basket stats selector orders eligible courses by descending `sampleCount`
- **AND** option labels contain only the course name

#### Scenario: Personal stats selector uses result count
- **WHEN** a selected player's personal statistics contain rows from multiple courses
- **THEN** the Personal stats course selector orders those courses by the corresponding manifest `sampleCount` descending
- **AND** the all-courses option remains available

#### Scenario: Course count ties are deterministic
- **WHEN** two courses have the same total exported result count
- **THEN** they are ordered by course name and then course ID

### Requirement: Public Basket stats variation selector ordering
The Basket stats variation selector SHALL use explicit basket and variation ordering from the exported data.

#### Scenario: Variation selector uses entity order
- **WHEN** a selected course's Basket stats file loads successfully
- **THEN** variations are ordered by basket `sortOrder`, then basket ID, variation `sortOrder`, then variation ID, with null sort orders after non-null values

### Requirement: Description tab navigation and default view
The static page SHALL display a Description tab before Course stats, Basket stats, and Personal stats, and SHALL make Description the default active view.

#### Scenario: Description tab is first
- **WHEN** the static basket statistics page is displayed
- **THEN** the navigation order is Description, Course stats, Basket stats, Personal stats

#### Scenario: Description is selected initially
- **WHEN** the static basket statistics page first loads successfully
- **THEN** the Description tab is active and its content is visible
- **AND** the Course stats, Basket stats, and Personal stats content is hidden

#### Scenario: Existing statistics views remain selectable
- **WHEN** the user selects Course stats, Basket stats, or Personal stats
- **THEN** the selected existing view becomes visible and Description becomes inactive

### Requirement: Description content asset
The static page SHALL render the reviewed content maintained in `description/description.txt` through a relative static asset without hard-coding a second copy of the prose in the page script.

#### Scenario: Description source is loaded
- **WHEN** the Description view is loaded
- **THEN** the page fetches the description asset using a relative path and renders its headings and paragraphs in the Description view

#### Scenario: Description asset is unavailable
- **WHEN** the description asset cannot be loaded
- **THEN** the Description view displays a clear description-unavailable message

### Requirement: Description formatting markers
The page SHALL convert the supported `<italic>...</italic>` and `<bold>...</bold>` markers in the description source to italic and bold rendered text while treating the source as data rather than arbitrary executable HTML.

#### Scenario: Inline formatting is rendered
- **WHEN** the description contains supported italic or bold markers
- **THEN** the enclosed text is rendered with the corresponding formatting

#### Scenario: Unsupported markup is not executed
- **WHEN** the description contains unsupported tags or markup-like text
- **THEN** the page does not execute it as HTML or script

### Requirement: Description placeholder resolution
The page SHALL replace `[competition_count]`, `[players_count]`, and `[latest_competition]` using the corresponding values from the loaded statistics manifest.

#### Scenario: Placeholders use manifest values
- **WHEN** the manifest contains description metadata
- **THEN** the Description view replaces all supported placeholders with the exported competition count, player count, and latest competition value

#### Scenario: Missing placeholder metadata is handled
- **WHEN** one or more supported placeholder values are absent from the manifest
- **THEN** the Description view displays a deterministic unavailable value for each missing placeholder and remains readable

### Requirement: Description loading state
The page SHALL communicate Description loading and failure states without preventing access to the statistics views.

#### Scenario: Description is loading
- **WHEN** the description asset is being fetched
- **THEN** the Description view displays a loading state

#### Scenario: Statistics manifest is unavailable
- **WHEN** the statistics manifest cannot be loaded
- **THEN** the Description view may render its static text but displays an unavailable state for manifest-backed placeholders
- **AND** existing statistics views retain their current missing-manifest state

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
The static page SHALL render Basket stats SPRW Y-axis labels according to fixed chart-specific tick rules.

#### Scenario: SPRW axis labels and grid lines are shown
- **WHEN** the Basket stats chart is displayed
- **THEN** the left SPRW Y-axis displays labels at `0`, `0.5`, `1`, and `1.5`
- **AND** a horizontal grid line is displayed at each of those SPRW values

#### Scenario: VAR axis labels are not shown
- **WHEN** the Basket stats chart is displayed
- **THEN** the chart does not display a right VAR Y-axis or VAR labels

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

### Requirement: Description accordion sections

The static basket statistics page SHALL render each top-level section of the Description content as an independently collapsible section, with General Information expanded by default.

#### Scenario: Description headings become accordion sections

- **WHEN** the Description source is loaded successfully
- **THEN** each top-level heading and the content that follows it are rendered as one collapsible section
- **AND** the sections preserve the order of the source headings

#### Scenario: General Information is open initially

- **WHEN** the Description view finishes its initial successful render
- **THEN** the General Information section is expanded
- **AND** every other Description section is collapsed

#### Scenario: Sections expand independently

- **WHEN** the user expands or collapses a Description section
- **THEN** only that section's content visibility changes
- **AND** the open or closed state of every other section remains unchanged

#### Scenario: Multiple sections remain open

- **WHEN** the user expands two or more Description sections
- **THEN** all of those sections remain expanded simultaneously

#### Scenario: Accordion controls are accessible

- **WHEN** the Description sections are displayed
- **THEN** each section has a keyboard-operable disclosure control with an accessible section label
- **AND** the control exposes whether its section is expanded or collapsed through native disclosure semantics

#### Scenario: Section content retains existing rendering

- **WHEN** a Description section is expanded
- **THEN** its paragraphs, placeholders, and supported italic or bold markers are rendered as they are in the existing Description view

#### Scenario: Description loading and error states are preserved

- **WHEN** the Description asset is loading or unavailable
- **THEN** the existing loading or unavailable message is displayed
- **AND** accordion sections are not displayed until the content has loaded successfully

### Requirement: Course stats chart first render sizing

The static page SHALL render the initially selected Course stats chart using the visible Course stats chart container dimensions.

#### Scenario: Course stats chart is first opened after Description

- **WHEN** the page loads with Description active and Course stats data has already loaded
- **AND** the user opens the Course stats view
- **THEN** the Course stats chart is drawn at the same full chart width used after changing the Course stats filters

#### Scenario: Course stats data finishes loading after the view is opened

- **WHEN** the user opens Course stats before its selected course data has finished loading
- **AND** the selected course data finishes loading while Course stats is visible
- **THEN** the Course stats chart is drawn using the visible chart container dimensions

#### Scenario: Course stats data loads while another view is active

- **WHEN** the selected Course stats data finishes loading while Course stats is hidden
- **THEN** the page does not measure and draw the Course stats chart using hidden-container dimensions
- **AND** the chart is drawn with the visible container dimensions when the user opens Course stats
### Requirement: Explanatory custom tooltips

The static page SHALL provide custom tooltips for the specified statistics labels and table headers. Tooltip triggers SHALL be visually indicated with a dotted underline and SHALL expose the tooltip on pointer hover and keyboard focus.

#### Scenario: SPR and VAR have individual definitions
- **WHEN** the SPR/VAR chart heading is displayed
- **THEN** `SPR` is an individual tooltip trigger with the text `Measurement of scoring separation based on a rating`
- **AND** `VAR` is an individual tooltip trigger with the text `Measurement of scoring variability`
- **AND** the two triggers are independently hoverable and focusable

#### Scenario: Course statistics headers explain their values
- **WHEN** the Course stats table is displayed
- **THEN** the `Basket` header has the tooltip `Basket layout [length in meters]`
- **AND** the `Average` header has the tooltip `Average score on a basket layout`
- **AND** the `1-2` header has the tooltip `Percentage of times a basket layout was completed in 1 or 2 throws`
- **AND** each of the `3`, `4`, `5`, `6`, and `7` headers has a tooltip explaining the percentage of times a basket layout was completed in that exact number of throws
- **AND** the `8+` header has the tooltip `Percentage of times a basket layout was completed in 8 or more throws`
- **AND** the `Count` header has the tooltip `Number of recorded results`

#### Scenario: Basket stats SPRW is explained
- **WHEN** the Basket stats chart heading is displayed
- **THEN** `SPRW` is a custom tooltip trigger with the text `Scoring separation calculated around a given rating`

#### Scenario: Personal statistics headers explain their values
- **WHEN** the Personal stats table is displayed
- **THEN** the `Rating` header has the tooltip `Average expected rating for a player on a given layout`
- **AND** the `Count` header has the tooltip `Number of recorded results for a player`
- **AND** the `Scores` header has the tooltip `Scores for a player on a given layout in chronological order`

#### Scenario: Tooltip triggers are keyboard discoverable
- **WHEN** a user focuses any requested tooltip trigger using the keyboard
- **THEN** the same custom tooltip content shown for pointer hover is displayed
- **AND** the trigger has a visible focus indication

### Requirement: Statistics terminology uses basket layout wording

The static page SHALL use the requested basket-layout terminology without changing data semantics.

#### Scenario: Basket stats selector uses layout terminology
- **WHEN** the Basket stats filters are displayed
- **THEN** the basket selector label is `Basket layout`

#### Scenario: Personal stats table uses layout terminology
- **WHEN** the Personal stats table is displayed
- **THEN** the fourth column header is `Layout` instead of `Variation`
- **AND** the table continues to display the same variation label values in that column

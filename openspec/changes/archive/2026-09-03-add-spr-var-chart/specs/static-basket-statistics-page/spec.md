## ADDED Requirements

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

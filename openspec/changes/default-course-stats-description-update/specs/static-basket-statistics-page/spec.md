## MODIFIED Requirements

### Requirement: Description tab navigation and default view
The static page SHALL display the Course stats, Basket stats, Personal stats, and Description navigation items in that order, and SHALL make Course stats the default active view.

#### Scenario: Description tab is last
- **WHEN** the static basket statistics page is displayed
- **THEN** the navigation order is Course stats, Basket stats, Personal stats, Description

#### Scenario: Course stats is selected initially
- **WHEN** the static basket statistics page first loads
- **THEN** the Course stats tab is active and its content is visible
- **AND** the Basket stats, Personal stats, and Description content is hidden

#### Scenario: Existing views remain selectable
- **WHEN** the user selects Description, Basket stats, or Personal stats
- **THEN** the selected view becomes visible and Course stats becomes inactive

### Requirement: Description content asset
The static page SHALL render the current reviewed content from `description/description.txt` as embedded HTML in `docs/index.html` without loading a separate Description asset at runtime.

#### Scenario: Current Description topics are displayed
- **WHEN** the static basket statistics page is loaded
- **THEN** the Description view contains General Information, Hole Layouts, Ratings, SPR/VAR Calculations, SPRW Calculations, Personal Stats, and Future Improvements and Suggestions in that order
- **AND** each topic is displayed as a regular heading with immediately visible content

#### Scenario: Description content remains embedded
- **WHEN** `description/description.txt` or `docs/description.txt` is absent at runtime
- **THEN** the embedded Description content remains displayable
- **AND** the page does not attempt to fetch either file

### Requirement: Description placeholder resolution
The page SHALL replace the embedded Description placeholders for competition count, player count, and latest competition using the corresponding values from the loaded statistics manifest.

#### Scenario: Placeholders use manifest values
- **WHEN** the manifest contains `metadata.description`
- **THEN** the page displays the exported competition count, player count, and latest competition in the embedded Description content

#### Scenario: Missing placeholder metadata is handled
- **WHEN** one or more supported placeholder values are absent from the manifest or the manifest cannot be loaded
- **THEN** the page displays `unavailable` for each missing value
- **AND** the embedded Description content remains readable

## REMOVED Requirements

### Requirement: Description accordion sections
**Reason**: Description topics are already regular always-visible headings, and this change refreshes that embedded structure rather than restoring accordions.
**Migration**: Keep all updated Description topics visible as ordinary headings and content.

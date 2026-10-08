## MODIFIED Requirements

### Requirement: Description tab navigation and default view
The static page SHALL display the Course stats, Basket stats, Personal stats, and Description navigation items in that order, and SHALL make Description the default active view.

#### Scenario: Description tab is last
- **WHEN** the static basket statistics page is displayed
- **THEN** the navigation order is Course stats, Basket stats, Personal stats, Description

#### Scenario: Description is selected initially
- **WHEN** the static basket statistics page first loads
- **THEN** the Description tab is active and its content is visible
- **AND** the Course stats, Basket stats, and Personal stats content is hidden

#### Scenario: Existing statistics views remain selectable
- **WHEN** the user selects Course stats, Basket stats, or Personal stats
- **THEN** the selected existing view becomes visible and Description becomes inactive

### Requirement: Description content presentation
The static page SHALL render the reviewed Description content as embedded HTML in `docs/index.html`, using ordinary headings and paragraphs rather than loading a separate Description text asset.

#### Scenario: Embedded Description content is displayed
- **WHEN** the static basket statistics page is loaded
- **THEN** the Description view contains the Description topics in their defined order
- **AND** each topic is displayed with a regular heading and its content is immediately visible

#### Scenario: Description does not depend on a text asset
- **WHEN** `docs/description.txt` is absent or unavailable
- **THEN** the embedded Description content remains displayable
- **AND** the page does not show a Description asset failure state

#### Scenario: Description headings are not accordions
- **WHEN** the Description view is displayed
- **THEN** its topic headings are ordinary heading elements
- **AND** no accordion or disclosure control is required to reveal their content

### Requirement: Description placeholder resolution
The page SHALL replace the embedded Description placeholders for competition count, player count, and latest competition using the corresponding values from the loaded statistics manifest.

#### Scenario: Placeholders use manifest values
- **WHEN** the manifest contains `metadata.description`
- **THEN** the page displays the exported competition count, player count, and latest competition in the embedded Description content

#### Scenario: Missing placeholder metadata is handled
- **WHEN** one or more supported placeholder values are absent from the manifest or the manifest cannot be loaded
- **THEN** the page displays `unavailable` for each missing value
- **AND** the embedded Description content remains readable

### Requirement: Description formatting
The embedded Description content SHALL use safe semantic HTML for supported emphasis, and SHALL not execute arbitrary Description text as script or markup.

#### Scenario: Emphasis is rendered
- **WHEN** the embedded Description contains emphasized or bold text
- **THEN** it is represented with semantic `em` or `strong` elements

#### Scenario: Dynamic values are inserted safely
- **WHEN** a manifest-backed Description value is displayed
- **THEN** it is inserted as text content
- **AND** the value cannot execute HTML or script

## REMOVED Requirements

### Requirement: Description accordion sections
**Reason**: Description topics are now regular always-visible headings and paragraphs.
**Migration**: Render the embedded topics directly in the Description article; remove disclosure state and accordion-specific styling.

### Requirement: Description loading state
**Reason**: Description is embedded in the HTML and no longer has an independently fetched asset.
**Migration**: Keep the existing statistics-manifest failure handling and placeholder fallback, but remove Description asset loading and unavailable states.

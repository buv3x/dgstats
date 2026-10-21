## MODIFIED Requirements

### Requirement: Description content presentation
The static page SHALL render the reviewed Description content as embedded HTML in `docs/index.html`, using ordinary headings and paragraphs rather than loading a separate Description text asset.

#### Scenario: Embedded Description content is displayed
- **WHEN** the static basket statistics page is loaded
- **THEN** the Description view contains the Description topics in their defined order
- **AND** each topic is displayed with a regular heading and its content is immediately visible

#### Scenario: Scoring Distribution content follows SPRW Calculations
- **WHEN** the Description view is displayed
- **THEN** it contains a `Scoring Distribution` topic immediately after `SPRW Calculations`
- **AND** the topic explains score groupings, Course stats filtering, Hole stats triangular weighting used by SPRW, line toggles, and the default 5% visibility threshold using the approved wording

#### Scenario: Description does not depend on a text asset
- **WHEN** `docs/description.txt` is absent or unavailable
- **THEN** the embedded Description content remains displayable
- **AND** the page does not show a Description asset failure state

#### Scenario: Description headings are not accordions
- **WHEN** the Description view is displayed
- **THEN** its topic headings are ordinary heading elements
- **AND** no accordion or disclosure control is required to reveal their content

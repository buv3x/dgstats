## MODIFIED Requirements

### Requirement: Description content presentation
The static page SHALL render the reviewed Description content as embedded HTML in `docs/index.html`, using ordinary headings, paragraphs, and semantic lists rather than loading a separate Description text asset. Description paragraphs and ordered-list text SHALL use the same 920px readable line-width constraint when the viewport is wide enough to apply it.

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

#### Scenario: Ordered-list text matches paragraph width
- **WHEN** the Description view is displayed at a viewport wider than the readable content limit
- **THEN** text in the putting-statistics ordered list wraps within the same 920px readable line-width constraint used by Description paragraphs
- **AND** the list retains visible numbering and its existing indentation

#### Scenario: Ordered-list layout remains responsive
- **WHEN** the Description view is displayed at a viewport narrower than the readable content limit
- **THEN** the ordered list shrinks with the Description article
- **AND** it does not create horizontal overflow

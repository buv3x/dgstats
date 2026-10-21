## MODIFIED Requirements

### Requirement: Description content presentation
The static page SHALL render the reviewed Description content as embedded HTML in `docs/index.html`, using ordinary headings and paragraphs rather than loading a separate Description text asset. The content SHALL include a `Leaderboards` topic immediately after `Personal stats. Putting stats`.

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

#### Scenario: Leaderboards description follows Putting stats
- **WHEN** the Description view is displayed
- **THEN** the `Leaderboards` heading appears after the `Personal stats. Putting stats` content
- **AND** its content explains non-Aces top-ten coverage, one-entry-per-player behavior, occurrence-count tie-breakers, and aggregate thresholds
- **AND** its content explains that Aces uses results and hole information, can include earlier years, does not require entered statistics, covers PDGA tournaments with live scoring, and can list one player multiple times

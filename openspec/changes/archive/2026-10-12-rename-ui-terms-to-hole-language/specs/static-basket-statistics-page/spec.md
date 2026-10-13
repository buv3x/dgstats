## ADDED Requirements

### Requirement: Static page branding terminology

The static statistics page SHALL display `LT PDGA Statistics` as both its browser document title and global page header.

#### Scenario: Browser title uses the requested name
- **WHEN** the static statistics page is loaded
- **THEN** the browser tab title is `LT PDGA Statistics`

#### Scenario: Global header uses the requested name
- **WHEN** the static statistics page is displayed
- **THEN** the global page header is `LT PDGA Statistics`

## MODIFIED Requirements

### Requirement: Basket statistics view navigation

The static page SHALL provide top-level navigation between the Course stats view, Hole stats view, Personal stats view, and Description view.

#### Scenario: Course stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Course stats`

#### Scenario: Hole stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Hole stats`

#### Scenario: Personal stats navigation item is displayed
- **WHEN** the static basket statistics page is displayed
- **THEN** it shows a top navigation item labeled `Personal stats`

#### Scenario: Course stats preserves current display
- **WHEN** the user selects `Course stats`
- **THEN** the page displays the existing course-level statistics controls, SPR/VAR scatter chart, and table

#### Scenario: Hole stats switches to hole controls
- **WHEN** the user selects `Hole stats`
- **THEN** the page displays the existing hole-statistics course and hole-layout controls instead of the Course stats rating controls and table

#### Scenario: Personal stats switches to player controls
- **WHEN** the user selects `Personal stats`
- **THEN** the page displays Personal stats player autocomplete controls and personal variation results instead of the Course stats and Hole stats controls

### Requirement: Explanatory custom tooltips

The static page SHALL provide custom tooltips for the specified statistics labels and table headers. Tooltip triggers SHALL be visually indicated with a dotted underline and SHALL expose the tooltip on pointer hover and keyboard focus.

#### Scenario: SPR and VAR have individual definitions
- **WHEN** the SPR/VAR chart heading is displayed
- **THEN** `SPR` is an individual tooltip trigger with the text `Measurement of scoring separation based on a rating`
- **AND** `VAR` is an individual tooltip trigger with the text `Measurement of scoring variability`
- **AND** the two triggers are independently hoverable and focusable

#### Scenario: Course statistics headers explain hole values
- **WHEN** the Course stats table is displayed
- **THEN** the `Hole / Layout` header has the tooltip `Hole layout [length in meters]`
- **AND** the `Average` header has the tooltip `Average score on a hole layout`
- **AND** the `1-2` header has the tooltip `Percentage of times a hole layout was completed in 1 or 2 throws`
- **AND** each of the `3`, `4`, `5`, `6`, and `7` headers has a tooltip explaining the percentage of times a hole layout was completed in that exact number of throws
- **AND** the `8+` header has the tooltip `Percentage of times a hole layout was completed in 8 or more throws`
- **AND** the `Count` header has the tooltip `Number of recorded results`

#### Scenario: Hole stats SPRW is explained
- **WHEN** the Hole stats chart heading is displayed
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

### Requirement: Statistics terminology uses hole and layout wording

The static page SHALL use the requested hole and layout terminology in public controls and table headings without changing data semantics.

#### Scenario: Course stats selector uses course terminology
- **WHEN** the Course stats filters are displayed
- **THEN** the course selector label is `Course`

#### Scenario: Course stats table uses hole terminology
- **WHEN** the Course stats table is displayed
- **THEN** its first column header is `Hole / Layout`

#### Scenario: Hole stats selectors use requested terminology
- **WHEN** the Hole stats filters are displayed
- **THEN** the course selector label is `Course`
- **AND** the hole-layout selector label is `Hole Layout`

#### Scenario: Hole stats heading uses metric-only terminology
- **WHEN** the Hole stats chart heading is displayed
- **THEN** its heading is `SPRW` without a `Basket` prefix

#### Scenario: Personal stats course selector uses course terminology
- **WHEN** the Personal stats filters are displayed
- **THEN** the course selector label is `Course`

#### Scenario: Personal stats table uses course and hole terminology
- **WHEN** the Personal stats table is displayed
- **THEN** its first column header is `Course`
- **AND** its second column header is `Hole`
- **AND** the existing `Layout` column remains present and continues to display variation label values

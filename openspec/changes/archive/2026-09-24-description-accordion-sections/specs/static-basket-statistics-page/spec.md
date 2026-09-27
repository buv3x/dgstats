## ADDED Requirements

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

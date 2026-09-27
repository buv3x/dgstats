# Description tab Specification

## Purpose

Define the public Description view for the static basket statistics page.

## ADDED Requirements

### Requirement: Description tab navigation and default view

The static page SHALL display a Description tab before Course stats, Basket
stats, and Personal stats, and SHALL make Description the default active view.

#### Scenario: Description tab is first

- **WHEN** the static basket statistics page is displayed
- **THEN** the navigation order is Description, Course stats, Basket stats,
  Personal stats

#### Scenario: Description is selected initially

- **WHEN** the static basket statistics page first loads successfully
- **THEN** the Description tab is active and its content is visible
- **AND** the Course stats, Basket stats, and Personal stats content is hidden

#### Scenario: Existing statistics views remain selectable

- **WHEN** the user selects Course stats, Basket stats, or Personal stats
- **THEN** the selected existing view becomes visible and Description becomes
  inactive

### Requirement: Description content asset

The static page SHALL render the reviewed content maintained in
`description/description.txt` through a relative static asset without
hard-coding a second copy of the prose in the page script.

#### Scenario: Description source is loaded

- **WHEN** the Description view is loaded
- **THEN** the page fetches the description asset using a relative path
- **AND** renders its headings and paragraphs in the Description view

#### Scenario: Description asset is unavailable

- **WHEN** the description asset cannot be loaded
- **THEN** the Description view displays a clear description-unavailable message

### Requirement: Description formatting markers

The page SHALL convert the supported `<italic>...</italic>` and
`<bold>...</bold>` markers in the description source to italic and bold rendered
text while treating the source as data rather than arbitrary executable HTML.

#### Scenario: Inline formatting is rendered

- **WHEN** the description contains supported italic or bold markers
- **THEN** the enclosed text is rendered with the corresponding formatting

#### Scenario: Unsupported markup is not executed

- **WHEN** the description contains unsupported tags or markup-like text
- **THEN** the page does not execute it as HTML or script

### Requirement: Description placeholder resolution

The page SHALL replace `[competition_count]`, `[players_count]`, and
`[latest_competition]` using the corresponding values from the loaded statistics
manifest.

#### Scenario: Placeholders use manifest values

- **WHEN** the manifest contains description metadata
- **THEN** the Description view replaces all supported placeholders with the
  exported competition count, player count, and latest competition value

#### Scenario: Missing placeholder metadata is handled

- **WHEN** one or more supported placeholder values are absent from the
  manifest
- **THEN** the Description view displays a deterministic unavailable value for
  each missing placeholder and remains readable

### Requirement: Description loading state

The page SHALL communicate Description loading and failure states without
preventing access to the statistics views.

#### Scenario: Description is loading

- **WHEN** the description asset is being fetched
- **THEN** the Description view displays a loading state

#### Scenario: Statistics manifest is unavailable

- **WHEN** the statistics manifest cannot be loaded
- **THEN** the Description view may render its static text but displays an
  unavailable state for manifest-backed placeholders
- **AND** existing statistics views retain their current missing-manifest state

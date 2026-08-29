## ADDED Requirements

### Requirement: Round copy controls
The system SHALL provide copy controls on the basket variation mapping page for copying values from other displayed rounds into a target round.

#### Scenario: Copy buttons are shown for other rounds
- **WHEN** the local user views the basket variation mapping editor with more than one round table
- **THEN** each round label area displays a copy button for each other displayed round

#### Scenario: Current round is not offered as a source
- **WHEN** copy buttons are displayed for a round table
- **THEN** the system does not display a copy button that copies the round into itself

#### Scenario: Single round has no copy buttons
- **WHEN** the local user views the basket variation mapping editor with only one round table
- **THEN** no round copy buttons are displayed

### Requirement: Unsaved round value copying
The system SHALL copy mapping values between round tables in the browser without saving them.

#### Scenario: Copying does not submit the mapping form
- **WHEN** the local user clicks a round copy button
- **THEN** the page updates matching target round select values without submitting the form

#### Scenario: Existing save action persists copied values
- **WHEN** the local user copies values into a target round and then clicks "Save Mappings"
- **THEN** the copied values are submitted through the existing mapping save form

#### Scenario: Copied values include pending source edits
- **WHEN** the local user changes source round select values and then copies from that source round before saving
- **THEN** the target round receives the source round's current browser form values

### Requirement: Round copy cell matching
The system SHALL match copied cells by division identity and basket ordinal.

#### Scenario: Matching cells are overwritten
- **WHEN** the local user copies from a source round into a target round
- **THEN** each target select with a matching source division identity and basket ordinal receives the source select value

#### Scenario: Blank matching source clears target value
- **WHEN** a matching source select has the empty "No mapping" value
- **THEN** the matching target select is set to the empty value

#### Scenario: Unmatched target cells remain unchanged
- **WHEN** a target select has no matching source division identity and basket ordinal
- **THEN** the target select value remains unchanged

#### Scenario: Column order does not control matching
- **WHEN** source and target round tables have divisions in different positions or missing divisions
- **THEN** copied values are matched by division identity and basket ordinal rather than by table column position

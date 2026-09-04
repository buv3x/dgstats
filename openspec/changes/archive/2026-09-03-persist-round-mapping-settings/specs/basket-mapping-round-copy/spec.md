## MODIFIED Requirements

### Requirement: Unsaved round value copying
The system SHALL copy mapping values and round-level mapping settings between round tables in the browser without saving them.

#### Scenario: Copying does not submit the mapping form
- **WHEN** the local user clicks a round copy button
- **THEN** the page updates the target round's basket course, same-layout value, and matching select values without submitting the form

#### Scenario: Existing save action persists copied values
- **WHEN** the local user copies values into a target round and then clicks "Save Mappings"
- **THEN** the copied round settings and copied mapping values are submitted through the existing mapping save form

#### Scenario: Copied values include pending source edits
- **WHEN** the local user changes source round settings or select values and then copies from that source round before saving
- **THEN** the target round receives the source round's current browser form values

### Requirement: Round copy cell matching
The system SHALL match copied cells by division identity and basket ordinal after applying the copied round-level basket course setting.

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

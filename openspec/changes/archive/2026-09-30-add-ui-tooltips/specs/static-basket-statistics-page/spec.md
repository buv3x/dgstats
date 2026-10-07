## ADDED Requirements

### Requirement: Explanatory custom tooltips

The static page SHALL provide custom tooltips for the specified statistics labels and table headers. Tooltip triggers SHALL be visually indicated with a dotted underline and SHALL expose the tooltip on pointer hover and keyboard focus.

#### Scenario: SPR and VAR have individual definitions
- **WHEN** the SPR/VAR chart heading is displayed
- **THEN** `SPR` is an individual tooltip trigger with the text `Measurement of scoring separation based on a rating`
- **AND** `VAR` is an individual tooltip trigger with the text `Measurement of scoring variability`
- **AND** the two triggers are independently hoverable and focusable

#### Scenario: Course statistics headers explain their values
- **WHEN** the Course stats table is displayed
- **THEN** the `Basket` header has the tooltip `Basket layout [length in meters]`
- **AND** the `Average` header has the tooltip `Average score on a basket layout`
- **AND** the `1-2` header has the tooltip `Percentage of times a basket layout was completed in 1 or 2 throws`
- **AND** each of the `3`, `4`, `5`, `6`, and `7` headers has a tooltip explaining the percentage of times a basket layout was completed in that exact number of throws
- **AND** the `8+` header has the tooltip `Percentage of times a basket layout was completed in 8 or more throws`
- **AND** the `Count` header has the tooltip `Number of recorded results`

#### Scenario: Basket stats SPRW is explained
- **WHEN** the Basket stats chart heading is displayed
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

### Requirement: Statistics terminology uses basket layout wording

The static page SHALL use the requested basket-layout terminology without changing data semantics.

#### Scenario: Basket stats selector uses layout terminology
- **WHEN** the Basket stats filters are displayed
- **THEN** the basket selector label is `Basket layout`

#### Scenario: Personal stats table uses layout terminology
- **WHEN** the Personal stats table is displayed
- **THEN** the fourth column header is `Layout` instead of `Variation`
- **AND** the table continues to display the same variation label values in that column

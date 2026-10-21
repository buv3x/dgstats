## MODIFIED Requirements

### Requirement: Explanatory custom tooltips

The static page SHALL provide custom tooltips for the specified statistics labels, Personal stats putting metric labels, and table headers. Tooltip triggers SHALL be visually indicated with a dotted underline and SHALL expose the tooltip on pointer hover and keyboard focus.

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

#### Scenario: Personal putting C1x and C2 labels are explained
- **WHEN** the Personal stats putting view displays an overall, yearly, or round-detail C1x or C2 metric
- **THEN** the `C1x` label is a tooltip trigger with the text `C1x: Circle 1 excluding bullseye (3 - 10 meters)`
- **AND** the `C2` label is a tooltip trigger with the text `C2: Circle 2 (10 - 20 meters)`

#### Scenario: Personal putting compound labels use focused metric triggers
- **WHEN** the Personal stats putting view displays `C2+ per round`
- **THEN** only the `C2+` text is a tooltip trigger with the text `C2+: Circle 2 and beyond (10+ meters)`
- **AND** the `per round` text remains part of the visible label outside that trigger
- **WHEN** the Personal stats putting view displays `Lost 3+putting per round`
- **THEN** only the `3+putting` text is a tooltip trigger with the text `3+putting: Taking more than 2 throws to complete the hole after landing inside Circle 2`
- **AND** the `Lost ` prefix and ` per round` suffix remain visible outside that trigger

#### Scenario: Tooltip triggers are keyboard discoverable
- **WHEN** a user focuses any requested tooltip trigger using the keyboard
- **THEN** the same custom tooltip content shown for pointer hover is displayed
- **AND** the trigger has a visible focus indication

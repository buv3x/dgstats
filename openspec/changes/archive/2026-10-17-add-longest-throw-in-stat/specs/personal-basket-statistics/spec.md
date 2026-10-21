## ADDED Requirements

### Requirement: Longest throw-in export

The personal statistics export SHALL provide an overall and yearly longest-throw-in highlight when at least one complete-round successful throw-in has a recorded distance. The highlight SHALL include the displayed whole-meter distance, the exact-feet tie count, and one occurrence descriptor per matching successful throw with competition, abbreviated round, round identity, and hole ordinal.

#### Scenario: Overall longest throw-in is exported
- **WHEN** a player has at least one complete-round successful throw-in with a recorded distance
- **THEN** the overall putting summary contains the longest-throw-in highlight
- **AND** the highlight contains the rounded meter display value and matching occurrence descriptors

#### Scenario: Yearly longest throw-in is exported
- **WHEN** a year contains at least one complete-round successful throw-in with a recorded distance
- **THEN** that yearly putting summary contains its longest-throw-in highlight

#### Scenario: No qualifying distance produces no highlight
- **WHEN** no complete-round successful throw-in has a recorded distance
- **THEN** the corresponding overall or yearly longest-throw-in highlight is absent

#### Scenario: Occurrences identify holes
- **WHEN** the exact maximum feet value occurs more than once
- **THEN** the exported occurrence list contains every matching competition, round, and hole
- **AND** the occurrence count equals the number of matching successful throws

### Requirement: Longest throw-in presentation

The Personal stats putting view SHALL display the longest throw-in using whole meters and SHALL use the existing highlight occurrence suffix convention. When occurrence descriptors are available, the complete displayed value SHALL be an accessible tooltip trigger whose tooltip lists competition, round, and hole for every occurrence.

#### Scenario: Single longest throw-in is displayed
- **WHEN** an overall or yearly longest-throw-in highlight has one occurrence
- **THEN** the view displays the rounded meter value without a multiple-occurrence suffix
- **AND** the value is a focusable tooltip trigger listing the occurrence

#### Scenario: Tied longest throw-ins are displayed
- **WHEN** an overall or yearly highlight has multiple exact-feet occurrences
- **THEN** the view displays the rounded meter value followed by the existing occurrence suffix
- **AND** the tooltip lists every competition, round, and hole occurrence

#### Scenario: Legacy export remains readable
- **WHEN** an older putting export has no longest-throw-in field or has no occurrence descriptors
- **THEN** the page does not create an empty tooltip trigger
- **AND** existing putting highlights remain readable

## ADDED Requirements

### Requirement: Complete-year visibility filtering
The Personal stats putting view SHALL display a year accordion only when that year's exported round list contains at least one round with `complete: true`.

#### Scenario: Year with a complete round is displayed
- **WHEN** a player's year contains at least one round whose `complete` field is `true`
- **THEN** the year is rendered as an available year accordion
- **AND** its existing year metrics and round-details behavior remain available

#### Scenario: Year with only incomplete rounds is hidden
- **WHEN** a player's year contains no round whose `complete` field is `true`
- **THEN** the year accordion is not rendered
- **AND** its incomplete rounds are not rendered in the page

#### Scenario: Complete zero-attempt round qualifies its year
- **WHEN** a player's year contains a round with `complete: true` and zero C1x and C2 totals
- **THEN** the year accordion is rendered
- **AND** the year is not filtered out based on metric counts

#### Scenario: Mixed year retains incomplete round details
- **WHEN** a player's year contains at least one complete round and one incomplete round with recorded attempts
- **THEN** the year accordion is rendered
- **AND** the incomplete round remains available in that year's existing round-details list
- **AND** the incomplete round retains its excluded styling when displayed

#### Scenario: No complete years are available
- **WHEN** none of a player's exported years contains a round with `complete: true`
- **THEN** no year accordions are rendered
- **AND** global putting totals and aggregate highlights remain visible
- **AND** no empty year container or unknown-year placeholder is rendered

#### Scenario: Global metrics are unaffected by year filtering
- **WHEN** one or more exported years are filtered from the display
- **THEN** the global C1x/C2 totals and global aggregate highlights retain their exported values

#### Scenario: Missing completeness metadata does not qualify a year
- **WHEN** a year contains rounds without `complete: true` or with missing completeness metadata
- **THEN** those rounds do not qualify the year for display
- **AND** the page remains compatible with the rest of the legacy putting payload

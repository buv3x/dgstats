## ADDED Requirements

### Requirement: Putting attempt classification
The system SHALL classify imported throw records into C1x and C2 putting attempts per player.

#### Scenario: Valid hole is processed
- **WHEN** a round-result hole has throw records ordered by score ordinal and the final record has zone 5
- **THEN** each preceding record is evaluated as a putting attempt
- **AND** a record is successful when the next record has zone 5
- **AND** otherwise the record is unsuccessful

#### Scenario: Invalid hole is omitted
- **WHEN** the final throw record for a hole does not have zone 5
- **THEN** the hole contributes no putting attempts

#### Scenario: Penalty ordinal gaps are supported
- **WHEN** score ordinals skip one or more values because of penalties
- **THEN** records are ordered by their ordinal values and classified by adjacent records in that ordering
- **AND** no sequential ordinal value is required

### Requirement: C1x and C2 zone classification
The system SHALL classify attempts using zone and distance rules expressed in feet.

#### Scenario: Circle 2 attempt
- **WHEN** an evaluated record has zone 4
- **THEN** it contributes one C2 attempt regardless of its distance value

#### Scenario: C1x distance attempt
- **WHEN** an evaluated record has zone 3 and distance is between 12 and 32 feet inclusive
- **THEN** it contributes one C1x attempt

#### Scenario: Bullseye successful attempt
- **WHEN** an evaluated zone 3 record is successful and its distance is below 12 feet
- **THEN** it contributes neither a C1x successful count nor a C1x total count

#### Scenario: Bullseye unsuccessful attempt
- **WHEN** an evaluated zone 3 record is unsuccessful and its distance is below 12 feet
- **THEN** it contributes one C1x total count
- **AND** it contributes no C1x successful count

#### Scenario: Distance fallback attempt
- **WHEN** an evaluated record is not classified by zone 3 or zone 4
- **THEN** the system checks `distance_to_target` first
- **AND** when that value is null, checks `drop_distance_to_target`
- **AND** 12–32 feet contributes C1x while 33–65 feet contributes C2

#### Scenario: Unclassifiable attempt
- **WHEN** an evaluated record has no qualifying zone and neither available distance falls within 12–65 feet
- **THEN** it contributes to neither statistic

### Requirement: Player putting aggregation
The system SHALL aggregate successful and total C1x and C2 counts per player.

#### Scenario: Successful and total counts are aggregated
- **WHEN** a player has classified successful and unsuccessful attempts across valid holes
- **THEN** each statistic exposes its successful count and total count
- **AND** total equals successful plus unsuccessful attempts, including unsuccessful bullseye C1x attempts

#### Scenario: Percentage is calculated
- **WHEN** a statistic has a total greater than zero
- **THEN** its percentage is successful divided by total and rounded to two decimal places for display

#### Scenario: Empty statistic is represented
- **WHEN** a player has zero total attempts for C1x or C2
- **THEN** the exported counts are zero
- **AND** the UI displays an em dash percentage with `(0 / 0)`

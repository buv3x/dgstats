## MODIFIED Requirements

### Requirement: C1x and C2 zone classification
The system SHALL classify attempts using zone and distance rules expressed in feet, with unsuccessful zone 3 attempts taking precedence over distance availability.

#### Scenario: Circle 2 attempt
- **WHEN** an evaluated record has zone 4
- **THEN** it contributes one C2 attempt regardless of its distance value

#### Scenario: C1x distance attempt
- **WHEN** an evaluated record has zone 3 and distance is between 12 and 32 feet inclusive
- **THEN** it contributes one C1x attempt

#### Scenario: Successful C1x distance attempt
- **WHEN** an evaluated record has zone 3, is successful, and distance is between 12 and 32 feet inclusive
- **THEN** it contributes one successful C1x attempt

#### Scenario: Unsuccessful zone 3 attempt without distance
- **WHEN** an evaluated record has zone 3, is unsuccessful, and distance is null
- **THEN** it contributes one C1x total count
- **AND** it contributes no C1x successful count

#### Scenario: Unsuccessful zone 3 attempt outside the C1x distance range
- **WHEN** an evaluated record has zone 3, is unsuccessful, and distance is below 12 feet or above 32 feet
- **THEN** it contributes one C1x total count
- **AND** it contributes no C1x successful count

#### Scenario: Bullseye successful attempt
- **WHEN** an evaluated zone 3 record is successful and its distance is below 12 feet
- **THEN** it contributes neither a C1x successful count nor a C1x total count

#### Scenario: Successful bullseye attempt
- **WHEN** an evaluated zone 3 record is successful and its distance is below 12 feet
- **THEN** it contributes neither a C1x successful count nor a C1x total count

#### Scenario: Bullseye unsuccessful attempt
- **WHEN** an evaluated zone 3 record is unsuccessful and its distance is below 12 feet
- **THEN** it contributes one C1x total count
- **AND** it contributes no C1x successful count

#### Scenario: Unsuccessful bullseye attempt
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

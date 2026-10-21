## MODIFIED Requirements

### Requirement: C1x and C2 zone classification
The system SHALL classify putting attempts using an explicitly provided `drop_zone_id` as the starting area when available, otherwise using `zone_id`, together with distance rules expressed in feet. Unsuccessful effective-zone-3 attempts SHALL take precedence over distance availability.

#### Scenario: Drop-zone C2 attempt
- **WHEN** an evaluated record has `drop_zone_id` equal to 4
- **THEN** it contributes one C2 attempt regardless of `zone_id` or distance values

#### Scenario: Drop-zone C1x distance attempt
- **WHEN** an evaluated record has `drop_zone_id` equal to 3 and its selected distance is between 12 and 32 feet inclusive
- **THEN** it contributes one C1x attempt

#### Scenario: Drop-zone C1x successful distance attempt
- **WHEN** an evaluated record has `drop_zone_id` equal to 3, is successful, and its selected distance is between 12 and 32 feet inclusive
- **THEN** it contributes one successful C1x attempt

#### Scenario: Drop-zone C1 bullseye follows zone-3 rules
- **WHEN** an evaluated record has `drop_zone_id` equal to 3 and its selected distance is below 12 feet
- **THEN** a successful attempt contributes neither a C1x successful count nor a C1x total count
- **AND** an unsuccessful attempt contributes one C1x total count and no C1x successful count

#### Scenario: Drop-zone C1 attempt without distance
- **WHEN** an evaluated record has `drop_zone_id` equal to 3, is unsuccessful, and both distances are null
- **THEN** it contributes one C1x total count
- **AND** it contributes no C1x successful count

#### Scenario: Zone classification without a drop zone
- **WHEN** `drop_zone_id` is null
- **THEN** the system applies the existing `zone_id` classification rules

#### Scenario: Distance fallback for a drop record
- **WHEN** an evaluated record has no effective zone of 3 or 4
- **THEN** the system checks `distance_to_target` first
- **AND** when that value is null, checks `drop_distance_to_target`
- **AND** 12–32 feet contributes C1x while 33–65 feet contributes C2

#### Scenario: Drop-zone precedence over distance
- **WHEN** an evaluated record has `drop_zone_id` equal to 4 and a distance in the C1x range
- **THEN** it contributes C2 rather than C1x

#### Scenario: Unclassifiable attempt
- **WHEN** an evaluated record has no qualifying effective zone and neither available distance falls within 12–65 feet
- **THEN** it contributes to neither statistic

### Requirement: Putting attempt classification
The system SHALL continue to determine whether an evaluated record is a putting attempt from the ordered throw timeline: the final record for a hole MUST have `zone_id` equal to 5, and each preceding record is successful when the next record has `zone_id` equal to 5. `drop_zone_id` SHALL NOT replace the basket marker.

#### Scenario: Drop-zone record before basket
- **WHEN** a record with `drop_zone_id` equal to 3 or 4 is immediately followed by a record with `zone_id` equal to 5
- **THEN** the record is evaluated as a successful putt using its effective drop zone

#### Scenario: Drop-zone record before a miss
- **WHEN** a record with `drop_zone_id` equal to 3 or 4 is followed by a non-basket record
- **THEN** the record is evaluated as an unsuccessful putt using its effective drop zone

#### Scenario: Valid hole is processed
- **WHEN** a round-result hole has throw records ordered by score ordinal and the final record has `zone_id` equal to 5
- **THEN** each preceding record is evaluated as a putting attempt
- **AND** a record is successful when the next record has `zone_id` equal to 5
- **AND** otherwise the record is unsuccessful

#### Scenario: Invalid hole is omitted
- **WHEN** the final throw record for a hole does not have `zone_id` equal to 5
- **THEN** the hole contributes no putting attempts

#### Scenario: Penalty ordinal gaps are supported
- **WHEN** score ordinals skip one or more values because of penalties
- **THEN** records are ordered by their ordinal values and classified by adjacent records in that ordering
- **AND** no sequential ordinal value is required

# putting-statistics Specification

## Purpose

Define how imported throw timelines are classified into putting attempts and aggregated, including player-round completeness and drop-area handling.

## Requirements

### Requirement: Putting attempt classification
The system SHALL classify imported throw records into C1x and C2 putting attempts per player, but SHALL only include attempts from player rounds that are complete under the round-completeness requirement.

#### Scenario: Valid hole is processed
- **WHEN** a round-result hole has throw records ordered by score ordinal and the final record has zone 5
- **THEN** each preceding record is evaluated as a putting attempt
- **AND** a record is successful when the next record has zone 5
- **AND** otherwise the record is unsuccessful

#### Scenario: Invalid hole is omitted from round metrics
- **WHEN** the final throw record for a hole does not have zone 5
- **THEN** the hole contributes no putting attempts
- **AND** the containing player round is marked incomplete

#### Scenario: Missing hole makes player round incomplete
- **WHEN** the player round's expected layout hole count includes an ordinal with no throw sequence
- **THEN** the player round is marked incomplete
- **AND** no attempts from any hole in that player round contribute to overall totals

#### Scenario: Penalty ordinal gaps are supported
- **WHEN** score ordinals skip one or more values because of penalties
- **THEN** records are ordered by their ordinal values and classified by adjacent records in that ordering
- **AND** no sequential ordinal value is required

#### Scenario: Drop-zone record before basket
- **WHEN** a record with `drop_zone_id` equal to 3 or 4 is immediately followed by a record with `zone_id` equal to 5
- **THEN** the record is evaluated as a successful putt using its effective drop zone

#### Scenario: Drop-zone record before a miss
- **WHEN** a record with `drop_zone_id` equal to 3 or 4 is followed by a non-basket record
- **THEN** the record is evaluated as an unsuccessful putt using its effective drop zone

### Requirement: C1x and C2 zone classification
The system SHALL classify putting attempts using an explicitly provided `drop_zone_id` as the starting area when available, otherwise using `zone_id`, together with distance rules expressed in feet. Unsuccessful effective-zone-3 attempts SHALL take precedence over distance availability.

#### Scenario: Circle 2 attempt
- **WHEN** an evaluated record has effective zone 4
- **THEN** it contributes one C2 attempt regardless of its distance value

#### Scenario: C1x distance attempt
- **WHEN** an evaluated record has effective zone 3 and its selected distance is between 12 and 32 feet inclusive
- **THEN** it contributes one C1x attempt

#### Scenario: Successful C1x distance attempt
- **WHEN** an evaluated record has effective zone 3, is successful, and its selected distance is between 12 and 32 feet inclusive
- **THEN** it contributes one successful C1x attempt

#### Scenario: Drop-zone C1 bullseye follows zone-3 rules
- **WHEN** an evaluated record has `drop_zone_id` equal to 3 and its selected distance is below 12 feet
- **THEN** a successful attempt contributes neither a C1x successful count nor a C1x total count
- **AND** an unsuccessful attempt contributes one C1x total count and no C1x successful count

#### Scenario: Unsuccessful effective zone 3 attempt without distance
- **WHEN** an evaluated record has effective zone 3, is unsuccessful, and both distances are null
- **THEN** it contributes one C1x total count
- **AND** it contributes no C1x successful count

#### Scenario: Unsuccessful effective zone 3 attempt outside the C1x distance range
- **WHEN** an evaluated record has effective zone 3, is unsuccessful, and its selected distance is below 12 feet or above 32 feet
- **THEN** it contributes one C1x total count
- **AND** it contributes no C1x successful count

#### Scenario: Distance fallback attempt
- **WHEN** an evaluated record has no effective zone of 3 or 4
- **THEN** the system checks `distance_to_target` first
- **AND** when that value is null, checks `drop_distance_to_target`
- **AND** 12–32 feet contributes C1x while 33–65 feet contributes C2

#### Scenario: Drop-zone precedence over distance
- **WHEN** an evaluated record has `drop_zone_id` equal to 4 and a distance in the C1x range
- **THEN** it contributes C2 rather than C1x

#### Scenario: Zone classification without a drop zone
- **WHEN** `drop_zone_id` is null
- **THEN** the system applies the equivalent `zone_id` classification rules

#### Scenario: Unclassifiable attempt
- **WHEN** an evaluated record has no qualifying effective zone and neither available distance falls within 12–65 feet
- **THEN** it contributes to neither statistic

### Requirement: Player putting aggregation
The system SHALL aggregate successful and total C1x and C2 counts per player from complete player rounds only, while retaining separate metrics for incomplete player rounds.

#### Scenario: Complete round contributes to totals
- **WHEN** a player round has every expected hole represented by a valid throw sequence ending in zone 5
- **THEN** classified attempts from that round contribute to the player's overall C1x and C2 counts

#### Scenario: Incomplete round is excluded from totals
- **WHEN** any expected hole is missing or has a throw sequence that does not end in zone 5
- **THEN** the round's classified attempts do not contribute to the player's overall C1x or C2 counts
- **AND** the round remains available as an incomplete round summary

#### Scenario: Empty throw data remains represented
- **WHEN** a player round has no imported throw rows
- **THEN** the round is represented as incomplete with zero round-level putting counts
- **AND** it contributes nothing to overall totals

#### Scenario: Completeness is evaluated per player round result
- **WHEN** one player's round result has a missing or invalid hole
- **THEN** that player's round is incomplete
- **AND** another player's complete round result for the same competition round remains complete

#### Scenario: Successful and total counts are aggregated
- **WHEN** a player has classified successful and unsuccessful attempts across complete rounds
- **THEN** each statistic exposes its successful count and total count
- **AND** total equals successful plus unsuccessful attempts, including unsuccessful bullseye C1x attempts

#### Scenario: Percentage is calculated
- **WHEN** a statistic has a total greater than zero
- **THEN** its percentage is successful divided by total and rounded to two decimal places for display

#### Scenario: Empty statistic is represented
- **WHEN** a player has zero total attempts for C1x or C2
- **THEN** the exported counts are zero
- **AND** the UI displays an em dash percentage with `(0 / 0)`

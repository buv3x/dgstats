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

### Requirement: Successful throw-in distance collection

The putting aggregation SHALL collect a successful throw-in candidate when a preceding throw in a valid hole is immediately followed by a basket throw and the preceding throw has a selected distance. The selected distance SHALL be `distance_to_target` when present, otherwise `drop_distance_to_target`.

#### Scenario: Successful throw with distance is collected
- **WHEN** a valid hole contains a throw immediately followed by a basket throw and the throw has a selected distance
- **THEN** the throw contributes one longest-throw-in candidate with its distance in feet

#### Scenario: Successful throw without distance is ignored
- **WHEN** a valid hole contains a successful throw and both distance fields are null
- **THEN** the throw contributes no longest-throw-in candidate

#### Scenario: Drop distance is used as fallback
- **WHEN** a successful throw has no `distance_to_target` and has a `drop_distance_to_target`
- **THEN** the candidate uses the drop distance in feet

#### Scenario: Invalid hole contributes no candidate
- **WHEN** a hole does not end with a basket throw
- **THEN** none of its throws contribute longest-throw-in candidates

### Requirement: Longest throw-in maximum and tie calculation

The putting aggregation SHALL select the maximum successful throw-in distance using the exact distance in feet. It SHALL preserve every occurrence whose exact feet value equals the selected maximum and SHALL calculate the displayed meter value by converting the maximum feet value with `feet * 0.3048` and rounding to a whole number.

#### Scenario: Maximum uses exact feet
- **WHEN** successful candidates have different distances in feet
- **THEN** the candidate with the greatest feet value is selected
- **AND** its displayed value is the rounded meter conversion

#### Scenario: Exact feet values are tied
- **WHEN** multiple successful candidates have the same maximum feet value
- **THEN** all matching candidates are retained as occurrences

#### Scenario: Rounded meter values are not ties
- **WHEN** different maximum candidates convert to the same rounded meter value but have different feet values
- **THEN** only the candidate with the greatest exact feet value is selected

#### Scenario: Complete rounds only affect aggregate highlights
- **WHEN** a successful candidate belongs to an incomplete player round
- **THEN** it remains available only in round-level processing and does not affect the overall or yearly longest-throw-in highlight

### Requirement: Three-plus-putt calculation

The putting aggregation SHALL calculate lost throws for each valid completed hole by finding the first row whose effective starting area is C1 or C2, counting subsequent throw transitions to the basket, adding penalties recorded after that initial landing, and subtracting the two expected combined throws with a floor of zero.

#### Scenario: One-putt finish has no lost throws

- **WHEN** the first qualifying C1/C2 landing is immediately followed by the basket
- **THEN** the hole contributes zero lost throws

#### Scenario: Two-putt finish has no lost throws

- **WHEN** the first qualifying C1/C2 landing is followed by one intermediate row and then the basket
- **THEN** the hole contributes zero lost throws

#### Scenario: Three-putt finish contributes one lost throw

- **WHEN** three throw transitions are required from the first qualifying C1/C2 landing to the basket
- **THEN** the hole contributes one lost throw

#### Scenario: Penalties increase lost throws

- **WHEN** two throw transitions follow the first qualifying landing and one penalty is recorded after that landing
- **THEN** the hole contributes one lost throw

#### Scenario: Multiple penalties are counted

- **WHEN** the records after the first qualifying landing contain multiple penalty counts
- **THEN** every recorded penalty is added to the post-landing throw count before subtracting the two expected throws

#### Scenario: First qualifying landing is used only once

- **WHEN** a hole leaves C1/C2 after its first qualifying landing and later returns to C1/C2
- **THEN** the later return does not reset the putt or lost-throw calculation

#### Scenario: Hole without qualifying landing is ignored

- **WHEN** a valid hole never has a qualifying C1/C2 landing before the basket
- **THEN** the hole contributes no lost throws

### Requirement: Three-plus-putt initial landing classification

The initial landing detector SHALL use the same effective zone and distance precedence as existing putting statistics: `drop_zone_id` overrides `zone_id`, and `distance_to_target` overrides `drop_distance_to_target`. Effective zone 3 or 4 SHALL qualify; otherwise a selected distance from 12 through 65 feet inclusive SHALL qualify.

#### Scenario: Zone identifiers qualify regardless of distance

- **WHEN** the first effective zone is 3 or 4
- **THEN** that row is treated as the initial C1/C2 landing

#### Scenario: Drop zone overrides source zone

- **WHEN** `drop_zone_id` is 3 or 4 and `zone_id` is different
- **THEN** the drop zone determines whether the row is the initial landing

#### Scenario: Distance fallback qualifies

- **WHEN** no effective zone 3 or 4 is present and the selected distance is between 12 and 65 feet inclusive
- **THEN** that row is treated as the initial landing

#### Scenario: Distance precedence is preserved

- **WHEN** `distance_to_target` is present and `drop_distance_to_target` is also present
- **THEN** only `distance_to_target` is used for initial-landing classification

#### Scenario: Out-of-range distance does not qualify

- **WHEN** no effective zone 3 or 4 is present and the selected distance is below 12 or above 65 feet
- **THEN** the row does not establish the initial landing

### Requirement: Three-plus-putt round aggregation

The aggregation SHALL calculate the metric independently for each complete round and normalize it to an 18-hole equivalent using the round's expected hole count. It SHALL expose the resulting overall and yearly values without adding three-plus-putt fields to per-round detail records.

#### Scenario: Complete round is normalized

- **WHEN** a complete round has `lostThrows` total and `expectedHoleCount` holes
- **THEN** its contribution is `lostThrows * 18 / expectedHoleCount`

#### Scenario: Yearly values use matching rounds

- **WHEN** complete rounds span multiple years
- **THEN** each yearly metric includes only rounds whose round date belongs to that year

#### Scenario: Existing putting metrics remain unchanged

- **WHEN** the new metric is calculated
- **THEN** C1x, C2, C2+, longest throw-in, and best-round calculations retain their existing semantics

### Requirement: Per-round three-plus-putt summary value

The putting exporter SHALL include the already-calculated number of lost throws from three-plus-putt holes in each represented `PuttingRoundSummary`.

#### Scenario: Round summary includes three-plus-putt count

- **WHEN** a represented round has valid holes whose post-C1/C2 throws exceed the expected two throws
- **THEN** its round summary includes a `threePlusPuttLostThrows` value equal to the accumulated lost-throw count

#### Scenario: Round summary reports zero when no three-plus-putts occurred

- **WHEN** a represented round has no lost throws from three-plus-putt holes
- **THEN** its round summary includes `threePlusPuttLostThrows` equal to zero

#### Scenario: Existing aggregate calculation is unchanged

- **WHEN** overall or yearly putting summaries are generated
- **THEN** their existing `threePlusPuttPerRound` calculation remains unchanged
- **AND** adding the round-level field does not add the metric to leaderboard records


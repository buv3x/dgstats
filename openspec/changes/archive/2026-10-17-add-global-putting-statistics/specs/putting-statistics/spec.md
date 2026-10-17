## MODIFIED Requirements

### Requirement: Putting attempt classification
The system SHALL classify imported throw records into C1x, C2, and C2+ putting attempts per player, but SHALL only include attempts from player rounds that are complete under the round-completeness requirement for global aggregation.

#### Scenario: Existing C1x and C2 classification remains unchanged
- **WHEN** a throw record is evaluated for putting statistics
- **THEN** its existing C1x and C2 classification rules continue to apply

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

#### Scenario: Successful C2+ attempt uses inclusive distance
- **WHEN** a successful evaluated record has an effective zone other than 3 and an effective distance of 33 feet or more
- **THEN** it contributes one successful C2+ attempt

#### Scenario: Successful C2+ attempt accepts an empty distance
- **WHEN** a successful evaluated record has an effective zone other than 3 and both distance values are null
- **THEN** it contributes one successful C2+ attempt

#### Scenario: Zone 3 is excluded from C2+
- **WHEN** a successful evaluated record has effective zone 3
- **THEN** it contributes no C2+ attempt regardless of distance availability

#### Scenario: Unsuccessful record is excluded from C2+
- **WHEN** an evaluated record does not successfully reach the basket
- **THEN** it contributes no C2+ attempt

### Requirement: Player putting aggregation
The system SHALL aggregate successful and total C1x and C2 counts per player from complete player rounds only, while retaining separate metrics for incomplete player rounds and calculating the new global C2+ and best-round metrics from complete rounds only.

#### Scenario: Complete round contributes to new global metrics
- **WHEN** a player round has every expected hole represented by a valid throw sequence ending in zone 5
- **THEN** its successful C2+ attempts are eligible for normalized average and maximum calculations
- **AND** its C1x metric is eligible for best-C1x-round selection

#### Scenario: Complete round contributes to existing totals
- **WHEN** a player round has every expected hole represented by a valid throw sequence ending in zone 5
- **THEN** classified attempts from that round contribute to the player's overall C1x and C2 counts

#### Scenario: Incomplete round is excluded from new global metrics
- **WHEN** any expected hole is missing or has a throw sequence that does not end in zone 5
- **THEN** the round's C2+ attempts do not contribute to global C2+ metrics
- **AND** the round's C1x metric does not contribute to best-C1x-round selection
- **AND** the round remains available as an incomplete round summary

#### Scenario: Incomplete round is excluded from existing totals
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

#### Scenario: C2+ round value is normalized to 18 holes
- **WHEN** a complete round has `n` holes and `c` successful C2+ attempts
- **THEN** its normalized C2+ value is `c * 18 / n`

#### Scenario: Average C2+ per round is calculated
- **WHEN** a player has one or more complete rounds
- **THEN** C2+ per round is the arithmetic mean of the normalized C2+ values
- **AND** its displayed value is rounded to two decimal places

#### Scenario: Most C2+ round reports ties
- **WHEN** multiple complete rounds share the same maximum normalized C2+ value
- **THEN** the export reports that exact maximum, its rounded display value, and the number of rounds reaching it

#### Scenario: Best C1x round prefers percentage
- **WHEN** complete rounds have C1x attempts
- **THEN** the selected round has the highest exact successful-to-total C1x ratio

#### Scenario: Best C1x round breaks percentage ties by attempts
- **WHEN** multiple complete rounds share the highest exact C1x percentage
- **THEN** the selected result uses the highest C1x total-attempt count
- **AND** the export reports how many rounds share both the winning percentage and total-attempt count

#### Scenario: No eligible global round metric is represented
- **WHEN** a player has no complete rounds with C2+ attempts for C2+ metrics or no complete rounds with C1x attempts for best C1x
- **THEN** the corresponding global metric is exported as unavailable rather than fabricated from incomplete or zero-attempt rounds

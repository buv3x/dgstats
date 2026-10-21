## MODIFIED Requirements

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
- **WHEN** the player round’s expected layout hole count includes an ordinal with no throw sequence
- **THEN** the player round is marked incomplete
- **AND** no attempts from any hole in that player round contribute to overall totals

#### Scenario: Penalty ordinal gaps are supported
- **WHEN** score ordinals skip one or more values because of penalties
- **THEN** records are ordered by their ordinal values and classified by adjacent records in that ordering
- **AND** no sequential ordinal value is required

### Requirement: Player putting aggregation
The system SHALL aggregate successful and total C1x and C2 counts per player from complete player rounds only, while retaining separate metrics for incomplete player rounds.

#### Scenario: Complete round contributes to totals
- **WHEN** a player round has every expected hole represented by a valid throw sequence ending in zone 5
- **THEN** classified attempts from that round contribute to the player’s overall C1x and C2 counts

#### Scenario: Incomplete round is excluded from totals
- **WHEN** any expected hole is missing or has a throw sequence that does not end in zone 5
- **THEN** the round’s classified attempts do not contribute to the player’s overall C1x or C2 counts
- **AND** the round remains available as an incomplete round summary

#### Scenario: Empty throw data remains represented
- **WHEN** a player round has no imported throw rows
- **THEN** the round is represented as incomplete with zero round-level putting counts
- **AND** it contributes nothing to overall totals

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

## ADDED Requirements

### Requirement: Player-round completeness identity
The system SHALL evaluate completeness per player `round_result_id`, not across all players sharing a competition round.

#### Scenario: One player is incomplete
- **WHEN** one player’s round result has a missing or invalid hole
- **THEN** that player’s round is incomplete
- **AND** another player’s complete round result for the same competition round remains complete

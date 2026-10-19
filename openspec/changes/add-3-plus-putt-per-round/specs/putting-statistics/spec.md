## ADDED Requirements

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

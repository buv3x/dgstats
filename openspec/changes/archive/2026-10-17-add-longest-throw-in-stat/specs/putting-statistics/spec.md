## ADDED Requirements

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

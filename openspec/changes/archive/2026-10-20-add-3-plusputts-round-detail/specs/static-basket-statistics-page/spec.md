## ADDED Requirements

### Requirement: Conditional three-plus-putt round-detail rendering

The static Personal stats putting view SHALL render a positive round-level `threePlusPuttLostThrows` value after C2 as `3+putts: N`, separated by a comma, and SHALL omit the metric for zero, missing, null, or non-numeric values.

#### Scenario: Positive value is rendered after C2

- **WHEN** a round detail has `threePlusPuttLostThrows` greater than zero
- **THEN** the rendered line contains C1x, C2, and `3+putts: N` in that order
- **AND** the metrics are separated by comma-space text

#### Scenario: Non-positive or missing value is omitted

- **WHEN** a round detail has no usable positive `threePlusPuttLostThrows` value
- **THEN** the rendered line contains its existing C1x and C2 metrics
- **AND** it does not contain `3+putts`

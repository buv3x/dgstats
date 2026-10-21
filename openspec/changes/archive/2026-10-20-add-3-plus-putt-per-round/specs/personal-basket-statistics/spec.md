## ADDED Requirements

### Requirement: Three-plus-putt personal summary metric

The personal statistics export SHALL provide a nullable `threePlusPuttPerRound` metric in both the overall putting summary and every yearly putting summary. The metric SHALL represent the average complete-round rate of throws lost to taking more than two combined throws after the first C1/C2 landing, including penalties.

#### Scenario: Overall metric is exported

- **WHEN** a player has one or more complete rounds with a calculated three-plus-putt value
- **THEN** the overall putting summary contains `threePlusPuttPerRound`
- **AND** its value is rounded to two decimal places

#### Scenario: Yearly metric is exported

- **WHEN** a player has one or more complete rounds in a year with a calculated three-plus-putt value
- **THEN** that yearly putting summary contains `threePlusPuttPerRound`
- **AND** its value uses only complete rounds from that year

#### Scenario: Rounds are weighted as 18-hole equivalents

- **WHEN** complete rounds have different expected hole counts
- **THEN** each round's lost-throw total is multiplied by `18 / expected hole count` before the rounds are averaged

#### Scenario: No eligible rounds produce no metric

- **WHEN** a player has no complete round with a qualifying metric value
- **THEN** `threePlusPuttPerRound` is null or omitted
- **AND** existing putting metrics remain available

#### Scenario: Incomplete rounds do not affect the metric

- **WHEN** a represented round is incomplete
- **THEN** its lost throws do not contribute to overall or yearly `threePlusPuttPerRound`
- **AND** existing incomplete-round detail behavior remains unchanged

## ADDED Requirements

### Requirement: Three-plus-putt personal-statistics display

The Personal stats putting view SHALL display an available `threePlusPuttPerRound` value as the last metric in the overall summary and in each yearly summary, labeled `3+putt per round` and formatted with exactly two decimal places.

#### Scenario: Overall metric is displayed last

- **WHEN** an overall putting summary contains `threePlusPuttPerRound`
- **THEN** the Personal stats view displays a line labeled `3+putt per round`
- **AND** that line appears after the existing longest-throw-in line
- **AND** the value has two digits after the decimal point

#### Scenario: Yearly metric is displayed last

- **WHEN** a yearly putting summary contains `threePlusPuttPerRound`
- **THEN** the expanded yearly section displays a line labeled `3+putt per round`
- **AND** that line appears after the existing yearly highlight lines
- **AND** the value has two digits after the decimal point

#### Scenario: Missing metric remains backward compatible

- **WHEN** an older personal-statistics file does not contain `threePlusPuttPerRound`
- **THEN** the view omits only the new line
- **AND** all existing putting metrics remain readable

#### Scenario: Metric is not added to leaderboard UI

- **WHEN** the user views Leaderboards
- **THEN** no `3+putt per round` category or row is displayed

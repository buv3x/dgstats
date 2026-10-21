## ADDED Requirements

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

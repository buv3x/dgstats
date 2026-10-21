## ADDED Requirements

### Requirement: Concise putting highlight tooltips

The Personal stats putting view SHALL render tooltip content for Most C2+, Best C1x, and Longest throw-in highlights as context entries only. It SHALL omit the metric name, displayed metric value, and section headings such as `Rounds:` or `Occurrences:`. Each matching round or occurrence SHALL appear on a separate line.

#### Scenario: Round highlight tooltip contains context lines
- **WHEN** a Most C2+ or Best C1x highlight has matching round descriptors
- **THEN** the tooltip contains one competition-and-round line for each matching round
- **AND** the tooltip does not contain the metric label, displayed value, or a `Rounds:` heading

#### Scenario: Longest throw-in tooltip contains occurrence lines
- **WHEN** a Longest throw-in highlight has matching occurrence descriptors
- **THEN** the tooltip contains one competition, round, and hole line for each occurrence
- **AND** the tooltip does not contain the metric label, displayed value, or an `Occurrences:` heading

#### Scenario: Multiple entries remain separated
- **WHEN** a highlight has multiple matching rounds or occurrences
- **THEN** each entry is separated by a newline
- **AND** no entries are merged into one line

#### Scenario: Accessible trigger context is preserved
- **WHEN** a putting highlight is rendered as a tooltip trigger
- **THEN** its accessible label continues to identify the metric and displayed value
- **AND** the concise tooltip content is used only as the tooltip text

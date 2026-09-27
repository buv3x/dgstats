## ADDED Requirements

### Requirement: Course stats chart first render sizing

The static page SHALL render the initially selected Course stats chart using the visible Course stats chart container dimensions.

#### Scenario: Course stats chart is first opened after Description

- **WHEN** the page loads with Description active and Course stats data has already loaded
- **AND** the user opens the Course stats view
- **THEN** the Course stats chart is drawn at the same full chart width used after changing the Course stats filters

#### Scenario: Course stats data finishes loading after the view is opened

- **WHEN** the user opens Course stats before its selected course data has finished loading
- **AND** the selected course data finishes loading while Course stats is visible
- **THEN** the Course stats chart is drawn using the visible chart container dimensions

#### Scenario: Course stats data loads while another view is active

- **WHEN** the selected Course stats data finishes loading while Course stats is hidden
- **THEN** the page does not measure and draw the Course stats chart using hidden-container dimensions
- **AND** the chart is drawn with the visible container dimensions when the user opens Course stats

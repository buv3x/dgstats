## ADDED Requirements

### Requirement: Leaderboard columns have stable alignment and widths
The leaderboard table SHALL use the same fixed column geometry for every category tab and year scope: the place column SHALL remain compact and right-aligned, the player-name column SHALL be 300px wide and left-aligned, and the value column SHALL be 200px wide and right-aligned.

#### Scenario: Category changes preserve column positions
- **WHEN** the user switches between any leaderboard category tabs
- **THEN** the place, name, and value columns retain the same horizontal positions and configured widths
- **AND** names remain left-aligned
- **AND** values remain right-aligned

#### Scenario: Year changes preserve column positions
- **WHEN** the user switches between Overall and any available leaderboard year
- **THEN** the place, name, and value columns retain the same horizontal positions and configured widths regardless of the selected rows

#### Scenario: Long content does not resize the columns
- **WHEN** a selected leaderboard contains names or values of different lengths
- **THEN** the name column remains 300px wide
- **AND** the value column remains 200px wide
- **AND** the table does not resize those columns based on cell content

#### Scenario: Narrow viewport remains usable
- **WHEN** the leaderboard is viewed in a viewport narrower than the fixed table width
- **THEN** the table remains horizontally scrollable within its existing wrapper
- **AND** the fixed column widths and alignments are preserved

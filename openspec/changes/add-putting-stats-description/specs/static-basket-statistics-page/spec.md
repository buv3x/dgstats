## ADDED Requirements

### Requirement: Personal putting statistics description
The static Description view SHALL identify the existing hole-level personal statistics section as `Personal Stats. Hole Stats` and SHALL display a `Personal stats. Putting stats` subsection after the hole-statistics paragraphs.

#### Scenario: Personal statistics headings are distinguished
- **WHEN** the user opens the Description view
- **THEN** the hole-level section heading is `Personal Stats. Hole Stats`
- **AND** a later subsection heading is `Personal stats. Putting stats`

#### Scenario: Putting data source and completeness are explained
- **WHEN** the putting-statistics subsection is displayed
- **THEN** it states that putting statistics are calculated from statistics entered by players in the PDGA Live app
- **AND** it states that a round is included only when statistics are fully entered for all holes
- **AND** it states that incomplete rounds appear in round details with strikethrough formatting and do not affect overall or yearly values

#### Scenario: Putting metrics are defined
- **WHEN** the putting-statistics subsection is displayed
- **THEN** it defines C1x as the percentage of made putts from Circle 1 excluding the bullseye at 3–10 meters
- **AND** it defines C2 as the percentage of made putts from Circle 2 at 10–20 meters
- **AND** it defines C2+ per round as the weighted average number of putts and throw-ins from C2 and beyond at 10+ meters per round, weighted for rounds with more than 18 holes
- **AND** it defines Most C2+ in a round as the maximum number of C2+ putts and throw-ins in a single round for a selected year or overall
- **AND** it defines Best C1x round as the highest C1x percentage in a single round for a selected year or overall, using total putts as a secondary criterion
- **AND** it defines Longest throw-in as the longest recorded distance from which a hole was completed, including aces when distance is entered
- **AND** it defines Lost 3+putting per round as the weighted average number of strokes lost to 3+ putting per round, weighted for rounds with more than 18 holes

#### Scenario: Three-plus-putting rules are explained
- **WHEN** the Lost 3+putting per round definition is displayed
- **THEN** it states that a 3+ putt occurs when a player fails to complete a hole within one or two throws after first landing in C1 or C2
- **AND** it states that penalty strokes are included
- **AND** it states that every throw beyond the second counts as a stroke lost to 3+ putting

#### Scenario: Future Improvements remains unchanged
- **WHEN** the Description view is updated with the putting-statistics subsection
- **THEN** the existing Future Improvements section remains present and unchanged


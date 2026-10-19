## ADDED Requirements

### Requirement: C1x leaderboard sample threshold
The C1x leaderboard SHALL include a player in a selected Overall or yearly scope only when the player has at least 100 C1x putts thrown in that scope.

#### Scenario: Player meets the Overall C1x threshold
- **WHEN** a player has exactly 100 or more aggregate C1x putts in Overall
- **THEN** the player is eligible for the Overall C1x leaderboard

#### Scenario: Player is below the Overall C1x threshold
- **WHEN** a player has fewer than 100 aggregate C1x putts in Overall
- **THEN** the player is omitted from the Overall C1x leaderboard

#### Scenario: Yearly C1x threshold is independent
- **WHEN** a player has 100 or more C1x putts in one year but fewer than 100 in another year
- **THEN** the player is eligible only in the year meeting the threshold

### Requirement: C1x threshold explanation
The C1x leaderboard SHALL display a small italic note below its table stating that at least 100 C1x putts are required.

#### Scenario: C1x note is visible
- **WHEN** the C1x category is selected
- **THEN** the threshold note appears below the leaderboard table
- **AND** the note uses small italic presentation

### Requirement: C2+ per round hole threshold
The C2+ per round leaderboard SHALL include a player in a selected Overall or yearly scope only when the sum of expected hole counts across the player’s complete rounds in that scope is at least 180 holes.

#### Scenario: Player meets the Overall hole threshold
- **WHEN** a player has exactly 180 or more played holes across complete Overall rounds
- **THEN** the player is eligible for the Overall C2+ per round leaderboard

#### Scenario: Player is below the Overall hole threshold
- **WHEN** a player has fewer than 180 played holes across complete Overall rounds
- **THEN** the player is omitted from the Overall C2+ per round leaderboard

#### Scenario: Yearly hole threshold is independent
- **WHEN** a player has 180 or more played holes in one year but fewer than 180 in another year
- **THEN** the player is eligible only in the year meeting the threshold

### Requirement: C2+ threshold explanation
The C2+ per round leaderboard SHALL display a small italic note below its table stating that at least 180 holes played are required.

#### Scenario: C2+ note is visible
- **WHEN** the C2+ per round category is selected
- **THEN** the threshold note appears below the leaderboard table
- **AND** the note uses small italic presentation

### Requirement: Best C1x ranking order
The Best C1x round leaderboard SHALL rank eligible players by best-round percentage descending, then putts thrown in that best round descending, then the number of times the exact percentage-and-putt-count result occurred descending, then player name alphabetically.

#### Scenario: Percentage is the primary order
- **WHEN** two players have different best-round C1x percentages
- **THEN** the player with the higher percentage receives the better place

#### Scenario: Putts thrown break an equal percentage
- **WHEN** two players have the same best-round C1x percentage
- **THEN** the player whose best round contains more C1x putts receives the better place

#### Scenario: Exact-result count breaks remaining equality
- **WHEN** two players have the same best-round percentage and putt count
- **THEN** the player whose exact result occurred more times receives the better place

#### Scenario: Full equality produces a tie
- **WHEN** two players have equal percentage, putt count, and exact-result count
- **THEN** they share the same competition place and are sorted alphabetically by name

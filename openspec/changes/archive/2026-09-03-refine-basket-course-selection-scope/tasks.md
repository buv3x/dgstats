## 1. Remove Persisted Settings

- [x] 1.1 Add a forward Liquibase changeset that drops `datas.basket_variation_round_settings`.
- [x] 1.2 Remove the `BasketVariationRoundSettings` entity.
- [x] 1.3 Remove the `BasketVariationRoundSettingsRepository`.
- [x] 1.4 Remove round-settings repository injection and load/save usage from the mapping admin service.

## 2. Request Model And Validation

- [x] 2.1 Add request/model fields for competition-level basket course, competition-level same-layout, and `courseSelectionByRound`.
- [x] 2.2 Add request/model fields for round-level basket course, round-level same-layout, and `courseSelectionByGroup`.
- [x] 2.3 Add request/model fields for group-level basket course selections by round division.
- [x] 2.4 Build effective course assignment per mapping cell from competition, round, or group scope.
- [x] 2.5 Validate each submitted mapping value against the effective basket course for that cell.
- [x] 2.6 Apply same-layout expansion only at competition scope or round scope, and never for group-scoped rounds.

## 3. Editor Rendering

- [x] 3.1 Restore default competition-level basket course and same-layout controls above the round tables.
- [x] 3.2 Add the competition-level `Course selection by round` checkbox, unchecked by default.
- [x] 3.3 Render round-level basket course, same-layout, and `Course selection by group` controls only when round-scoped course selection is enabled.
- [x] 3.4 Render group-level basket course selectors in division headers only when group-scoped course selection is enabled for that round.
- [x] 3.5 Disable and uncheck same-layout for group-scoped rounds.
- [x] 3.6 Filter mapping select options according to the effective course scope for each cell.

## 4. Browser Behavior

- [x] 4.1 Hide or disable round copy controls whenever course selection by round is enabled.
- [x] 4.2 Update option-filtering JavaScript for competition, round, and group course scopes.
- [x] 4.3 Update same-layout JavaScript so competition-scoped same-layout mirrors each round and round-scoped same-layout mirrors only its round.
- [x] 4.4 Ensure enabling group-level course selection clears/disables same-layout for that round and updates affected cell options.

## 5. Verification

- [x] 5.1 Compile the project with Maven.
- [x] 5.2 Review the generated template paths for expected control visibility, field names, and copy-control availability.

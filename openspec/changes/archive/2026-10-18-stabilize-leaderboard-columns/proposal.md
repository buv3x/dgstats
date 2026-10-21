## Why

Leaderboard columns currently use automatic browser sizing, so switching between category tabs or years can move the name and value columns as content lengths change. A stable layout will make comparisons easier and prevent the leaderboard from appearing jumpy.

## What Changes

- Keep the leaderboard place column at its existing compact width.
- Fix the player-name column at 300px and left-align its contents.
- Fix the leaderboard-value column at 200px and right-align its contents.
- Use a fixed leaderboard table layout consistently across all categories and year selections.
- Preserve horizontal scrolling for narrow screens.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Define stable leaderboard column alignment and widths across category tabs and year scopes.

## Impact

- Affects the static leaderboard page presentation in `docs/styles.css` and, if needed to express the fixed columns explicitly, `docs/index.html`.
- No changes to leaderboard data, ranking, filtering, navigation, or backend/export behavior.

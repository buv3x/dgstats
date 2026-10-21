## Why

The static Description view documents Personal stats and Putting stats but does not explain how the Leaderboards view is calculated or why the Aces leaderboard behaves differently. Adding this explanation will help users interpret ranking coverage, tie handling, eligibility thresholds, and the source of ace records.

## What Changes

- Add a `Leaderboards` section to the embedded Description content after `Personal stats. Putting stats`.
- Explain that non-Aces leaderboards show the top 10 players for putting metrics, excluding Circle 2 percentage and 3+putting because the back edge of Circle 2 can be difficult to define consistently.
- Explain one-entry-per-player behavior and occurrence-count tie-breakers for Most C2+ in a round, Best C1x round, and Longest throw-in.
- Explain that the aggregated C1x and C2+ per round leaderboards require minimum numbers of putts thrown or holes played, with thresholds shown below the relevant leaderboard.
- Explain that Aces uses results and hole information rather than player-entered statistics, can include earlier years and players without entered statistics, captures aces from PDGA tournaments with live scoring, and may show a player multiple times.

## Capabilities

### New Capabilities

- None.

### Modified Capabilities

- `static-basket-statistics-page`: Extend the reviewed embedded Description content with the Leaderboards explanation and preserve its topic ordering.

## Impact

- `docs/index.html`: Add the Leaderboards heading and explanatory paragraphs to the embedded Description article.
- No API, export, ranking, calculation, or generated data changes.

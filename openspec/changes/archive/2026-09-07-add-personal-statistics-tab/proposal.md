## Why

The static basket statistics page currently explains course and basket variation behavior, but it cannot answer a player-focused question: which mapped basket variations a selected person plays best or worst relative to the field. The existing export also intentionally omits player identity, so personal statistics require an explicit, separate export contract.

## What Changes

- Add a `Personal stats` tab to the static statistics page.
- Export an autocomplete-ready player list containing only players with at least one eligible personal basket variation result.
- After a player is selected, display one list of that player's eligible basket variations ordered by calculated rating from highest to lowest.
- Calculate each row from equally weighted personal hole-score results on globally eligible basket variations.
- Treat a basket variation as globally eligible only when it has at least 50 mapped, rated recorded results and a usable unweighted score-over-rating regression line.
- Treat a player/variation row as personally eligible only when the selected player has played that variation at least twice.
- Display rounded integer row ratings while keeping decimal precision for calculation and sorting.
- Display each row's personal result count and comma-separated scores sorted by round date, then round id, ascending.

## Capabilities

### New Capabilities
- `personal-basket-statistics`: Player lookup and player-specific best/worst basket variation statistics.

### Modified Capabilities
- `basket-statistics-export`: Export player lookup data, personal statistics data, and precomputed global variation regression lines needed by personal statistics.
- `static-basket-statistics-page`: Add the Personal stats tab, autocomplete person selection, and personal variation ratings list.

## Impact

- Extends the statistics export service and repository projection to include player, round date, and round id fields for personal-statistics export only.
- Adds static JSON files under `docs/data/` for eligible players and player-specific personal statistics.
- Updates the manifest so the static page can discover the personal-statistics export paths.
- Updates `docs/index.html`, `docs/app.js`, and `docs/styles.css` to support the third tab and autocomplete/list workflow.
- Adds or updates focused tests around export eligibility, regression inversion, sorting, and static page rendering behavior.

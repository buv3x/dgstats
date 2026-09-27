## Context

The current statistics export produces `docs/data/statistics.json`, course sample files under `docs/data/courses/`, and precomputed Basket Stats files under `docs/data/basket-stats/`. Course sample files intentionally omit player identity, so the static page cannot derive personal statistics from existing JSON.

Personal statistics need both player identity for lookup and enough hole-score context to calculate player-specific basket variation ratings. The relevant database entities already contain the needed data: `Player`, `RoundResult`, `Round`, `Competition`, `HoleScore`, and `BasketVariationRoundDivision`.

## Goals / Non-Goals

**Goals:**
- Add a static `Personal stats` tab with autocomplete player selection.
- Export only players who have at least one eligible personal basket variation row.
- Calculate one ordered list of player basket variation ratings, highest to lowest.
- Use all mapped, rated global results without weighting to build a regression line per eligible basket variation.
- Use each selected player's equally weighted hole-score results to calculate an average personal rating per variation.
- Preserve existing Course stats and Basket stats behavior.

**Non-Goals:**
- No dynamic backend API for the static page.
- No authentication, privacy controls, or per-user account model.
- No rating-window or SPRW calculation for personal statistics.
- No separate best and worst sections in the first version.
- No manual player merge or disambiguation tooling.

## Decisions

### Export personal statistics as static JSON

The export will add a player lookup file and player-scoped personal statistics files:

```text
docs/data/players.json
docs/data/personal-stats/{playerId}.json
```

The manifest will include paths that let the static page discover these files, for example `playersPath` and `personalStatsPathTemplate`.

Alternative considered: put all personal statistics into one JSON file. That would simplify fetching but can make the static page load far more data than needed before a player is selected. Player-scoped files keep initial load small and align with the existing course-scoped export pattern.

### Use one raw export row model with personal fields

The export service should extend or add a repository projection that includes:

- player id, display name, PDGA number
- basket course, basket, and variation descriptors
- round id and round date
- round result rating
- hole score

Existing public course sample files should continue to omit player identity. The personal export should be the only JSON surface that exposes player identifiers.

Alternative considered: infer personal rows from existing course files in the browser. This is impossible because current course files deliberately exclude player identity.

### Precompute global variation regression lines

For each basket variation, gather all mapped scores with non-null `round_result.rating`. A variation is globally eligible when:

- it has at least 50 recorded results
- rating variance is non-zero
- the unweighted score-over-rating regression slope is negative

The regression line is:

```text
expectedScore = intercept + slope * rating
```

The personal rating for a score is the inverse:

```text
rating = (score - intercept) / slope
```

Because lower scores are better, a usable line should have a negative slope. Zero or positive slopes are omitted to avoid misleading inverted ratings.

Alternative considered: use the existing SPRW weighted windows. That would make personal ratings local to a rating window and would not satisfy the requirement that regression lines are independent of the selected person and can be precalculated per variation.

### Export authoritative personal row ratings

The backend export should calculate the decimal average rating per player/variation and write it to the player file. The static page should sort by this decimal value and display the rounded integer value.

Alternative considered: export regression lines and raw scores, then calculate personal ratings in JavaScript. That duplicates math in the browser and makes test coverage weaker. Exporting final row ratings keeps the static page focused on display.

### Sort personal scores by event order

Within a player/variation row, scores will be sorted by round date ascending and then round id ascending. The exported score list can be a compact integer list for display, while optional result descriptors can be retained only if needed for future traceability.

Alternative considered: sort scores by score value. That makes the list easier to scan for extremes but loses the requested chronological progression.

## Risks / Trade-offs

- Personal JSON exposes player names and PDGA numbers in the public static data set -> Limit the player list to players with eligible personal statistics and avoid exporting profile URLs, city, country, or other unnecessary fields.
- Some variations may produce unstable or counterintuitive regression lines -> Require at least 50 global samples, non-zero rating variance, and negative slope.
- A single player file per eligible player can create many files -> This keeps initial page load small; export diagnostics should report counts so growth is visible.
- Rounded display ratings may appear tied while decimal sorting differs -> Sort by decimal rating and use deterministic tie-breakers after rating.
- Null round dates can affect ordering -> Sort null dates after dated rounds, then use round id ascending.

## Migration Plan

- Existing exports remain readable by Course stats and Basket stats.
- After implementation, run the local export to generate `players.json` and `personal-stats/*.json`.
- If personal export fails or yields no eligible players, the static page should show a clear empty state while preserving the existing two tabs.

## Open Questions

- None for the initial implementation. The current decisions use the user's answers: one list, only eligible players in autocomplete, `Name (PDGA #)` display, equal weighting, and global/personal eligibility as clarified.

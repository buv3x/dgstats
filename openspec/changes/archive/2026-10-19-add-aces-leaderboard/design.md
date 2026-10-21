## Context

The static statistics page exports leaderboard data from `BasketStatisticsExportService` and renders it from `docs/data/leaderboards.json`. Existing putting and Longest throw-in categories are derived from `round_result_throws`, are limited to complete rounds, and aggregate to one entry per player.

Aces are different: an ace is a `hole_score` row whose score is exactly `1`. Its distance comes from the `basket_variation` mapped to the same round division and hole. The ace record must therefore be collected independently of putting rows and must remain valid even when the round is incomplete or has no imported throw timeline.

## Goals / Non-Goals

**Goals:**

- Add an occurrence-level Aces category to the exported leaderboard data and UI.
- Preserve each ace as a separate row, including repeated aces by one player.
- Rank and place entries by exact distance in meters only.
- Provide competition, round, and hole tooltip context.
- Provide ace-derived year options, including years before 2025, without changing year options for other categories.
- Append `m` to Aces and Longest throw-in leaderboard values.

**Non-Goals:**

- Do not use `round_result_throws` for ace detection, completeness, or distance.
- Do not apply complete-round filters or C1x/C2+ eligibility thresholds to Aces.
- Do not change personal statistics or other leaderboard calculations.
- Do not add database schema changes.

## Decisions

### Query ace facts directly from hole scores and mappings

Extend the hole-score export projection or add a dedicated repository projection containing player, round, competition, hole ordinal, score, and mapped basket variation distance. Filter ace candidates by `score = 1` and a non-null mapped distance. Extract ace candidates before any existing rating-based filtering, because ace eligibility is unrelated to rating.

Using the existing putting export is rejected because it is based on `round_result_throws` and its complete-round processing would incorrectly exclude valid aces.

### Build a separate occurrence collection

Maintain ace occurrences by player and by year in the leaderboard export model. Each occurrence contains the player name, round date/year, distance in meters, and tooltip labels. Overall contains all occurrences; yearly scopes contain all occurrences whose round date belongs to that year.

An ace occurrence is not collapsed into a player maximum or count. The exported category list contains one leaderboard entry per occurrence.

### Rank all ace occurrences by distance

Sort Aces by distance descending. Equal distances receive the same competition place, and no occurrence count, player name, date, or other attribute participates in placement. For deterministic rendering only, equal-distance rows may be ordered by stable occurrence fields after placement has been assigned; this ordering is not a ranking tie-breaker.

Unlike all existing categories, Aces is not truncated to ten entries and is not subject to the one-entry-per-player rule.

### Make year selection category-aware

Keep the existing 2025-through-current-year plus Overall options for the other categories. When Aces is selected, derive year options from exported ace scopes, include only years with at least one ace, sort them newest first, and append Overall. If the previously selected year is unavailable for Aces, select the newest ace year; if no ace years exist, select Overall. Switching back to another category restores a valid standard-category selection.

### Keep display formatting local to leaderboard values

Format both Aces and Longest throw-in visible values with a trailing ` m`. Existing personal-statistics formatting is left unchanged.

## Risks / Trade-offs

- [Incomplete rounds contain invalid or partial hole data] → Treat every persisted `hole_score.score = 1` with a mapped distance as an ace, as explicitly required; do not use throw import completeness.
- [A mapped variation is missing] → Exclude the occurrence because no authoritative distance is available; retain no guessed distance.
- [Many historical aces make the table long] → Preserve the requirement to show all occurrences; rely on the existing table layout and scrolling behavior.
- [A year selected for one category is unavailable in another] → Reconcile the selected value when the category changes using the category-specific option list.
- [Existing generated JSON is stale] → Regenerate the leaderboard asset through the normal export workflow and verify both historical and current-year scopes.

## Migration Plan

1. Deploy the exporter and static UI changes together.
2. Regenerate `docs/data/leaderboards.json`.
3. Verify Aces with complete and incomplete rounds, repeated players, historical years, equal distances, and missing mappings.
4. Roll back by reverting the exporter/UI change and regenerating the prior leaderboard asset if necessary.

## Open Questions

None. Aces explicitly ignore round completeness and all `round_result_throws` data.

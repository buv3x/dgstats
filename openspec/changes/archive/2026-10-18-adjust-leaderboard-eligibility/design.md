## Context

The completed leaderboard export currently admits any player with a non-empty metric. The generated Overall leaderboard therefore includes very small samples, and the Best C1x ranking uses only percentage plus occurrence count even though the number of attempts is a more useful confidence signal.

The exporter already has the required source data: aggregate C1x totals, complete round expected-hole counts, and Best C1x round fields (`percentage`, `total`, and `count`). The static UI already renders a category table and can add explanatory content below it.

## Goals / Non-Goals

**Goals:**

- Apply thresholds independently to Overall and each yearly scope.
- Explain the two thresholds below the relevant leaderboard table in small italic text.
- Refine Best C1x ordering without changing existing Personal stats calculations.
- Regenerate the consolidated leaderboard asset.

**Non-Goals:**

- Changing the source putting classification or complete-round rules.
- Applying thresholds to C1x, C2+ per round, or other Personal stats displays.
- Adding thresholds to the other leaderboard categories.

## Decisions

### Filter in the exporter

Filter candidates before ranking so excluded players cannot affect top-ten boundaries or tie places. For C1x, use the selected scope’s aggregate C1x total. For C2+ per round, sum `expectedHoleCount` across the selected scope’s complete rounds; this represents holes played even when a particular hole produced no classified C2+ attempt.

The thresholds are inclusive: 100 C1x putts and 180 holes qualify.

### Provide labels as UI metadata

Add a category-level note in the leaderboard payload, or derive the fixed note from the category in the existing renderer. The renderer displays the note below the table only for C1x and C2+ per round, using small italic styling. Keeping the note category-specific avoids duplicating it on unrelated tabs.

### Refine Best C1x ranking

Represent Best C1x ranking fields separately rather than overloading one occurrence field:

1. best percentage descending;
2. putts thrown in the selected best round descending;
3. number of times that exact percentage-and-putt-count result occurred descending;
4. player name alphabetically.

The existing per-player Best C1x calculation already selects a player’s best round by percentage and putt count and computes exact-result occurrences. Export the selected round’s total as the second ranking field, then apply the new comparator before assigning places.

## Risks / Trade-offs

- [Thresholds remove legitimate small-sample results] → Show the requirement directly below each affected table so the ranking’s coverage rule is visible.
- [Yearly hole totals can differ from attempt totals] → Use complete rounds’ expected hole counts, matching the existing weighted-round denominator and the meaning of holes played.
- [Existing display values do not expose all ranking fields] → Keep putt count and exact-result count as export-only metadata; preserve the established visible Best C1x value format.

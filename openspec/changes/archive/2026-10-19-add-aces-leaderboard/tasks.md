## 1. Export ace occurrences

- [x] 1.1 Extend the hole-score export projection or add a dedicated repository projection to expose player, competition, round, hole, score, and mapped basket variation distance for ace processing.
- [x] 1.2 Collect ace occurrences from `hole_score.score = 1` before existing rating and putting filters, excluding only occurrences without a mapped distance or required display context.
- [x] 1.3 Add occurrence data structures and aggregate Overall/year scopes without using `round_result_throws`, complete-round filtering, or putting thresholds.
- [x] 1.4 Export one Aces entry per occurrence, with distance-based competition placement, all rows retained, and competition/round/hole tooltip data.

## 2. Update existing leaderboard export behavior

- [x] 2.1 Add the `aces` category to the generated leaderboard asset while preserving all existing category calculations and ranking rules.
- [x] 2.2 Append `m` to Longest throw-in leaderboard values and format Aces values with the same suffix.
- [x] 2.3 Ensure ace scopes include historical years before 2025 only when they contain at least one ace, while standard category scopes remain available from 2025 through the current year.

## 3. Update the static leaderboard UI

- [x] 3.1 Add the Aces second-level tab using the existing leaderboard table layout.
- [x] 3.2 Make leaderboard year options category-aware, including fallback selection when switching between standard categories and Aces.
- [x] 3.3 Render all Aces rows, repeated players, meter values, competition/round/hole tooltips, and competition places without introducing a times suffix.

## 4. Verify and regenerate static output

- [x] 4.1 Update the affected OpenSpec-aligned source documentation or generated fixture expectations for the new category and year behavior.
- [x] 4.2 Regenerate `docs/data/leaderboards.json` through the normal export workflow and confirm the manifest still references it.
- [x] 4.3 Compile the project and inspect the generated asset for complete/incomplete-round aces, repeated players, historical years, equal distances, meter suffixes, and unchanged non-Aces categories.

## Context

The static page embeds its Description content directly in `docs/index.html`. The Scoring Distribution explanation belongs immediately after the SPRW Calculations section and should explain both the Course stats and Hole stats views without changing calculations or chart behavior.

## Goals / Non-Goals

**Goals:**

- Add the approved Scoring Distribution section after SPRW Calculations.
- Use the approved wording, including the explanation that the Hole stats calculation uses the same “triangular weight” method used by SPRW.
- Preserve semantic HTML structure with one heading and readable paragraphs.

**Non-Goals:**

- Do not change exported data, score-bucket calculations, weighting, filters, charts, or tooltips.
- Do not revise other Description sections.

## Decisions

- Update the embedded Description markup in `docs/index.html`, placing an `h2` titled `Scoring Distribution` and the approved explanatory paragraphs immediately after the SPRW Calculations content.
- Keep score groupings and visibility thresholds stated in the copy: scores 1 and 2 together, scores above 8 together, and default-enabled lines above 5% overall occurrence.
- Use the following approved copy:

  > Scoring distribution shows the percentages of occurrences for specific scores, with scores of 1 and 2 grouped together, as well as all scores above 8. On the “Course stats” tab, these percentages are provided for the full range of results or for players within a specific rating range when a filter is selected.
  >
  > The chart on the “Hole stats” tab takes a slightly different approach: it calculates scoring around specific ratings using the same “triangular weight” method used by SPRW. For instance, 50% for score 4 at a rating of 850 does not mean that half of the players with a rating of exactly 850 played the hole with a score of 4, since there were likely not many players with that exact rating. Instead, it means that a score of 4 was recorded approximately half the time among all players around the 850 rating.
  >
  > The lines on the chart correspond to specific scores or groups of scores. A line is available if the score has ever occurred on the hole. Each line can be toggled on or off. By default, to avoid clutter, only lines representing scores that occurred at least 5% of the time overall are enabled.

## Risks / Trade-offs

- [Risk] The section could be inserted in the wrong location → Verify it appears directly after SPRW Calculations and before the next Description topic.
- [Risk] Curly quotation marks or punctuation could be normalized inconsistently → Preserve the approved copy exactly in the HTML text content.

## Context

The static page embeds its Description content directly in `docs/index.html`. The page already explains Personal stats and Putting stats, while the Leaderboards view is documented nowhere. The requested change is documentation-only and must preserve all existing leaderboard behavior and generated data.

## Goals / Non-Goals

**Goals:**

- Add a `Leaderboards` heading and the approved explanatory paragraphs after `Personal stats. Putting stats`.
- Explain the non-Aces top-ten and one-entry-per-player rules, occurrence-count tie-breakers, and aggregate thresholds.
- Explain the separate Aces data source, earlier-year availability, live-scoring coverage, and repeated player entries.

**Non-Goals:**

- Do not change leaderboard calculations, ranking, eligibility, displayed values, year selectors, or exports.
- Do not change the existing Putting stats description or add a separate text asset.

## Decisions

- **Edit the embedded Description article in `docs/index.html`.** This is the existing source of truth for the visible Description view, and keeping the text beside the related Putting stats explanation preserves the document's topic order.
- **Use ordinary HTML headings and paragraphs.** This matches the current Description presentation and requires no JavaScript or styling changes.
- **Use the approved wording with “putts thrown.”** The aggregate-threshold explanation will retain the user's chosen terminology while also mentioning holes played.
- **Describe Aces as a deliberate exception.** The paragraph will state that Aces uses results and hole information rather than player-entered statistics, can include earlier years, covers PDGA tournaments with live scoring, and permits multiple rows for one player.

## Risks / Trade-offs

- [Documentation can drift from leaderboard behavior] → Keep the text limited to behavior already represented by the current leaderboard specifications and implementation; make no calculation changes in this change.

## Context

Hole stats is a static view backed by JSON exported by `BasketStatisticsExportService`. Each selected hole variation currently exposes sliding windows at 5-point rating intervals. Every window contains SPRW, raw sample count, weighted sample count, and a weighted-count bucket used to choose point and line styles.

The existing course statistics data contains raw samples and calculates score-bucket percentages in the browser, but the Hole stats payload intentionally contains only precomputed windows. The new chart therefore needs distribution values exported alongside each window.

## Goals / Non-Goals

**Goals:**

- Provide weighted score-bucket percentages for every exported Hole stats window.
- Display the distribution below SPRW with matching rating range, dimensions, eligibility, and confidence styling.
- Keep one stable color per score bucket across all selected holes.
- Allow each displayed bucket line to be toggled independently and choose defaults from the variation-wide unweighted distribution.
- Preserve existing SPRW behavior and data compatibility for clients that ignore the new fields.

**Non-Goals:**

- Changing the SPRW weighting function, window radius, window step, or minimum sample rules.
- Adding raw samples to the Hole stats payload.
- Adding distribution charts to Course stats, Personal stats, or Leaderboards.
- Replacing the existing SPRW chart or its tooltip semantics.

## Decisions

### Export distribution values with each window

Extend each Hole stats variation with the buckets present in its complete sample set and the overall unweighted bucket percentages. Extend each window with weighted percentages keyed by the existing bucket labels. The exporter calculates these values using the same triangular weight function and midpoint used by SPRW.

This keeps the browser lightweight and avoids exposing raw samples. A map keyed by `1-2`, `3`, `4`, `5`, `6`, `7`, and `8+` is self-describing and avoids depending on array index conventions in the exported contract.

Alternative considered: load the course sample file in Hole stats and recalculate distributions in JavaScript. Rejected because it duplicates export logic, downloads substantially more data, and could allow the two weighting implementations to diverge.

### Reuse SPRW window eligibility and confidence styles

Distribution values are emitted only for windows already eligible for SPRW. The distribution renderer uses each window's existing `sprwCountBucket`: `50-99` produces unconnected points, `100-199` produces dotted segments, and `200+` produces solid segments. When adjacent windows have different confidence buckets, the segment is split at its midpoint exactly as SPRW is today.

This makes the confidence meaning consistent between the two charts and avoids introducing a second threshold system.

### Determine visible series from full-variation metadata

A line is created for every bucket listed as present in the variation's complete sample set, even if a particular rating window has a zero percentage for that bucket. The default visibility state is true only when that bucket's overall unweighted variation percentage is greater than 5%; all other present buckets start toggled off.

This directly distinguishes “the hole has ever produced this score” from “the score is common overall” and prevents lines appearing or disappearing as the rating changes.

### Use a fixed percentage axis and explicit bucket legend

The chart uses a 0–100 percentage Y-axis with percentage tick labels. Its legend contains an interactive control for each present bucket, using the fixed bucket color and an accessible checkbox/button state. Toggling a legend item redraws only the distribution series; it does not alter exported data or SPRW.

The fixed axis makes percentages comparable across holes and avoids the visual instability of a per-hole maximum.

### Keep chart lifecycle aligned with SPRW

The distribution chart is rendered whenever the selected Hole stats variation is rendered, after the visible chart container has been measured. It is cleared and rebuilt when the variation changes, so its toggle defaults are recalculated for the newly selected variation.

## Risks / Trade-offs

- **[Export payload grows]** → Add only seven percentage values per eligible window plus small variation-level metadata; do not include raw samples.
- **[Low-confidence windows can look visually busy with seven series]** → Reuse the existing SPRW point/dotted/solid conventions and default-hide buckets at or below 5% overall.
- **[A bucket can be present globally but zero in a local window]** → Keep its line present and plot zero-valued points, matching the explicit requirement that presence is based on the full hole sample set.
- **[Existing generated JSON is stale until re-exported]** → Keep the UI tolerant of missing distribution fields and regenerate all Hole stats exports as part of implementation verification.

## Migration Plan

1. Add the export fields and unit coverage for triangular weighted bucket percentages.
2. Regenerate `docs/data/basket-stats/*.json`.
3. Add the chart markup, rendering, legends, colors, and tooltip text.
4. Verify existing SPRW behavior and the new chart with representative variations containing sparse and common buckets.

Rollback is a source revert; older payloads remain readable because the distribution chart can report unavailable data while SPRW continues to work.

## Open Questions

- None blocking implementation. The percentage axis is specified as fixed 0–100% for cross-hole comparability.

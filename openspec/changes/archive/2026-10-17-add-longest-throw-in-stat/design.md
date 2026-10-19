## Context

Putting attempts are currently classified while `PuttingRoundBuilder` processes valid holes. Complete rounds feed the overall and yearly putting summaries, which already expose highlight DTOs and accessible tooltip triggers for the best C1x and most C2+ results. The new highlight needs more granular occurrence data because its tooltip must identify a hole, not only a round.

## Goals / Non-Goals

**Goals:**

- Collect the longest successful throw-in for overall and yearly complete-round summaries.
- Compare candidate distances using exact feet values.
- Display the selected distance as whole meters.
- Preserve every occurrence tied at the exact maximum feet value.
- Expose competition, round, and hole in the tooltip.
- Reuse existing highlight layout, tie suffix, tooltip, and backward-compatible rendering patterns.

**Non-Goals:**

- Changing C1x/C2 classification, success detection, or round completeness rules.
- Counting unsuccessful attempts or successful attempts without a selected distance.
- Adding a database column or changing the import format.
- Treating distances that merely round to the same meter value as ties.

## Decisions

### Collect candidates during valid-hole processing

When a hole ends at the basket, inspect each preceding throw together with its success result. A successful throw with a non-null effective distance becomes a candidate. The effective distance uses `distance_to_target` first and `drop_distance_to_target` as the existing fallback. Candidates are stored with exact feet and a descriptor containing the round identity, competition label, abbreviated round label, and hole ordinal.

This reuses the existing adjacency-based success calculation and ensures invalid holes cannot contribute candidates. Computing the stat from exported C1x/C2 totals was rejected because those totals omit some successful throws and do not retain hole-level distances.

### Aggregate maximums from complete rounds

Each round builder retains its longest successful candidate. The player summary and yearly summary inspect only complete rounds, matching the existing aggregate highlights. The maximum is compared by exact feet value; all round/hole occurrences with that exact value are exported. The displayed meters value is calculated only after selecting the maximum using `Math.round(feet * 0.3048)`.

This prevents display rounding from merging distinct source distances, for example 49 ft and 50 ft, even if both display as 15 m.

### Add a dedicated occurrence descriptor

Introduce a descriptor for longest-throw-in occurrences rather than extending the existing round-only descriptor. It includes the round identity and labels plus `holeOrdinal`, allowing duplicate occurrences in the same round to remain distinct and giving the UI the exact requested context.

### Reuse the existing tooltip and tie display conventions

Render the value as a tooltip trigger whenever occurrence descriptors are present. The visible value uses whole meters and appends the existing plural occurrence suffix only when the exact-feet maximum occurs more than once. Tooltip lines list competition, round, and hole for every occurrence. Older JSON without the new field remains readable without a tooltip.

## Risks / Trade-offs

- [A successful throw has no distance] → Exclude it from longest-throw-in candidates while retaining existing putting metrics.
- [A round contains multiple occurrences at the same maximum] → Preserve one descriptor per successful throw, including hole ordinal.
- [Distance fallback differs for drop-zone throws] → Use the existing effective-distance helper consistently.
- [Existing generated files are numerous] → Update the exporter and regenerate/verify data through the project’s normal export workflow rather than hand-editing player JSON.
- [Older exports lack the new field] → Make the UI render the existing highlight area without creating an empty tooltip.

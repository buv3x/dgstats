## Context

Putting statistics are exported by `BasketStatisticsExportService` from normalized `round_result_throws` timelines and rendered from each player's personal JSON snapshot. The exporter already groups rows by player round and hole, orders them by `score_ordinal`, identifies valid holes ending at zone 5, and excludes incomplete rounds from overall and yearly aggregates. `C2+ per round` establishes the required normalization pattern: calculate an 18-hole-equivalent value per complete round, then average those round values.

The new metric needs information that the current putting projection does not select: `penalty_count`. The normalized entity and importer already persist it, so the change is limited to the read projection and aggregation logic. The metric must not alter existing C1x/C2 classification or leaderboard data.

## Goals / Non-Goals

**Goals:**

- Export `3+putt per round` for overall personal putting statistics.
- Export the same metric independently for every yearly personal putting summary.
- Detect the first C1/C2 landing on each valid hole using effective zone and distance/drop-distance precedence already used by putting statistics.
- Count putts and penalties after the initial landing, with the first two combined throws treated as expected and only excess throws reported as lost.
- Apply complete-round filtering and the existing 18-hole weighting model.
- Render the metric as the final putting-summary line with two-decimal precision.
- Preserve compatibility with older personal JSON files that lack the field.

**Non-Goals:**

- No leaderboard category or leaderboard ranking.
- No changes to existing C1x, C2, C2+, longest throw-in, or best-round calculations.
- No per-round `3+putt` field in round-detail objects.
- No database migration or new API endpoint.

## Decisions

### Extend the existing putting projection with penalty counts

Select `rrt.penaltyCount` as part of `PuttingThrowRow` and expose it through the projection interface. This reuses the already normalized/imported value and avoids reconstructing penalties from ordinal gaps. A null penalty count is treated as zero for compatibility with any legacy data.

### Detect the initial landing independently from attempt classification

For each valid hole, scan the ordered timeline from the first row and select the first row whose effective starting area is C1 or C2:

- effective zone is `drop_zone_id` when present, otherwise `zone_id`;
- effective distance is `distance_to_target` when present, otherwise `drop_distance_to_target`;
- effective zone 3 or 4 qualifies as C1/C2 by zone identity;
- when no effective zone 3 or 4 is available, a distance from 12 through 65 feet inclusive qualifies.

This detection is intentionally separate from the existing C1x/C2 attempt classifier. In particular, a zone-3 bullseye may establish the initial C1 landing even though successful bullseye attempts are excluded from the C1x percentage under existing rules.

### Calculate lost throws from the remainder of the hole

After the first qualifying row at index `i`, the number of putt throws required to finish is the number of timeline transitions from that row to the final basket row: `lastIndex - i`. Penalties attached to records after the initial landing, including the completion record, are added to that count. The hole's lost-throw contribution is:

```text
max(0, (lastIndex - i) + sum(penalty_count after i) - 2)
```

If no qualifying landing exists, the hole contributes nothing. A valid hole with one or two combined throws after the initial landing contributes zero. Once the initial landing is found, later movement between zones does not reset the calculation.

### Aggregate with the existing complete-round boundary

Accumulate each valid hole's lost throws in the round builder. A complete round contributes:

```text
round lost throws per 18 holes = total lost throws × 18.0 / expected hole count
```

The overall metric is the arithmetic mean of these weighted values across complete rounds. The yearly metric uses only complete rounds whose round date belongs to that year. Empty input returns null, matching `c2PlusPerRound` behavior. Values are rounded to two decimal places only for export/display, not before averaging.

### Keep the export field scalar and optional

Add a nullable `Double threePlusPuttPerRound` field to both `PuttingSummary` and `PuttingYearSummary`. Do not add it to `PuttingRoundSummary`, `PuttingRoundDescriptor`, or leaderboard records. Jackson's existing non-null inclusion behavior and the UI's missing-field checks preserve readability of older exports.

### Render it last in both summary scopes

Append `3+putt per round` after the existing longest-throw-in line in the shared putting-highlight renderer. The overall summary and each expanded yearly section use the same renderer, ensuring identical label and `toFixed(2)` formatting. No leaderboard renderer is changed.

### Alternatives considered

- **Aggregate all lost throws and divide by total holes:** rejected because it would weight long and short rounds differently from the established C2+ per-round metric.
- **Infer penalties from score-ordinal gaps:** rejected because the imported penalty count is explicitly persisted and ordinal gaps do not reliably encode penalty multiplicity.
- **Reuse C1x/C2 attempt classification for initial landing:** rejected because the metric needs the first physical C1/C2 landing, including zone-based records that are not counted as successful C1x/C2 attempts.

## Risks / Trade-offs

- [Penalty-count semantics differ across legacy imports] → Treat null as zero, add tests for penalty counts on intermediate and completion records, and preserve ordinal ordering.
- [A malformed hole has no valid completion] → Reuse existing valid-hole processing; incomplete rounds remain excluded from overall/yearly metrics.
- [Floating-point differences affect displayed averages] → Sum unrounded per-round doubles and round only at the final export boundary, matching C2+ behavior.
- [Older personal files lack the field] → Render only when the value is finite; retain all existing putting lines when absent.
- [Initial zone-3 rows may be closer than the existing C1x range] → Keep initial-landing qualification separate and document the zone-first rule explicitly.

## Migration Plan

No database migration is required. Implement the projection, exporter model, aggregation, UI rendering, and focused tests; regenerate personal statistics JSON; then verify overall and yearly examples plus an older export without the field. Rollback is reverting the exporter/UI changes and regenerating the previous JSON format.

## Open Questions

None. The metric is explicitly required in both overall and yearly personal-statistics sections.

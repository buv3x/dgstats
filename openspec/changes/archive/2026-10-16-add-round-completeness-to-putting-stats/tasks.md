## 1. Throw projection and round discovery

- [x] 1.1 Update the putting projection to start from `RoundResult`, left-join normalized throws, and expose player-round metadata plus expected layout hole count.
- [x] 1.2 Update the projection model and null-row handling so a round result with no throw rows still creates one putting-round candidate.

## 2. Completeness and aggregation

- [x] 2.1 Group rows by player `round_result_id`, then by hole ordinal, and mark a player-round incomplete when layout metadata is unavailable, an expected hole is missing, a hole has no throws, or its ordered final throw is not zone 5.
- [x] 2.2 Preserve penalty ordinal-gap behavior by ordering available throws by score ordinal and evaluating adjacent records without requiring consecutive ordinals.
- [x] 2.3 Add completeness metadata to round summaries and retain classified metrics for incomplete rounds, including zero-attempt rounds.
- [x] 2.4 Feed only complete player-round attempts into overall C1x/C2 counters while preserving existing putting-zone classification and percentage rules.

## 3. Static putting UI

- [x] 3.1 Render the exported completeness flag on round-detail rows, treating missing flags in legacy JSON as complete.
- [x] 3.2 Add CSS styling for incomplete round rows with italic text and strikethrough.
- [x] 3.3 Preserve existing collapsed round-detail behavior, metric formatting, accessibility attributes, and total-only fallback for older exports.

## 4. Export regeneration and verification

- [x] 4.1 Regenerate personal statistics JSON and confirm complete rounds retain their totals while incomplete rounds remain visible with `complete: false` and do not affect totals.
- [x] 4.2 Verify representative cases: complete round, missing hole, invalid final zone, empty throw data, and two players with different completeness for the same competition round.
- [x] 4.3 Compile the application and inspect generated export structure for the new completeness field.

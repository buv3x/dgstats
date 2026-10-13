## Context

The PDGA round-result importer persists each score through `ImportService.savePlayer()`. The current implementation identifies numbered players by `pdga_num`, but identifies unnumbered players by a combination of name, city, and country. Because PDGA numbers may appear later and are not removed or reassigned, this can split one real player into two database rows.

The database already permits nullable player PDGA numbers and enforces uniqueness for non-null PDGA numbers. The change is limited to importer lookup behavior and repository query support.

## Goals / Non-Goals

**Goals:**

- Make PDGA number the authoritative identity when present.
- Reconcile a newly numbered result with an existing same-name player whose PDGA number is null.
- Match unnumbered results only to same-name players whose PDGA number is null.
- Remove city and country from identity matching while continuing to store them as player metadata.
- Preserve idempotent player and round-result updates.

**Non-Goals:**

- No database migration or cleanup of already duplicated player rows.
- No fuzzy, case-insensitive, normalized, or multi-field name matching.
- No changes to PDGA DTOs, public APIs, exports, or player metadata persistence.

## Decisions

### Use an ordered two-stage lookup

When a score has a PDGA number, the importer first calls `findByPdgaNum`. If that misses, it calls a repository method equivalent to `findFirstByNameAndPdgaNumIsNull`. If both miss, it creates a player. When a score has no PDGA number, it performs only the name-plus-null-PDGA lookup before creating a player.

This preserves the invariant that an assigned PDGA number takes precedence over name matching and permits a null-PDGA record to be upgraded when the number first appears.

### Add an explicit null-PDGA repository query

Add a Spring Data derived query for exact name equality and `pdga_num IS NULL`. This expresses the identity rule directly and avoids relying on nullable method arguments or city/country predicates.

### Keep metadata updates separate from identity selection

After selecting or creating the player, retain the existing assignments for PDGA number, names, city, country, nationality, and profile URL. These fields describe the latest source data; only PDGA number and exact name-plus-null-PDGA status participate in identity lookup.

### Test the decision table

Focused tests should cover PDGA hit, PDGA miss with null-PDGA name fallback, PDGA miss with new player creation, no-PDGA null-name match, no-PDGA creation, and differing city/country values. Tests should also verify that a same-name player with a different assigned PDGA number is not selected.

## Risks / Trade-offs

- [Risk] Existing duplicate null-PDGA rows with the same name may still exist → Mitigation: preserve the repository's `findFirst` behavior and keep data cleanup outside this scoped logic change.
- [Risk] Exact names may vary in capitalization or formatting → Mitigation: do not introduce undocumented normalization; use the source name exactly as requested.
- [Risk] A PDGA result with a number could conflict with malformed duplicate numbered rows → Mitigation: rely on the existing unique database constraint and surface the persistence failure rather than guessing between identities.

## Migration Plan

No schema migration is required. Deploy the repository and service logic together, then validate with focused importer tests. Rollback consists of reverting the application change; existing player rows are not modified except when a normal import matches and updates them.

## Open Questions

None for the requested behavior.

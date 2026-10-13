## Why

PDGA results can contain a player first without a PDGA number and later with a newly assigned number. The current importer uses city and country for no-PDGA lookup, so the later result can create a duplicate instead of upgrading the existing player record.

## What Changes

- Use PDGA number as the primary player identity whenever the incoming result contains one.
- If a PDGA-number lookup misses, fall back to an existing player with the same name and a `NULL` PDGA number.
- When the incoming result has no PDGA number, look up only by name among players whose PDGA number is `NULL`.
- Stop using city, country, nationality, profile URL, or other metadata for player identity lookup.
- Preserve the existing behavior of updating the matched player with the latest result metadata.
- Add focused coverage for PDGA priority, no-PDGA fallback, and metadata-independent matching.

## Capabilities

### New Capabilities

<!-- None. This change refines existing PDGA import behavior. -->

### Modified Capabilities

- `pdga-import-processing`: Define deterministic player identity lookup and upgrade behavior during round-result import.

## Impact

- `ImportService.savePlayer()` lookup and fallback logic.
- `PlayerRepository` query methods.
- PDGA import processing tests and related test fixtures.
- No database schema, API, or exported player data changes are required.

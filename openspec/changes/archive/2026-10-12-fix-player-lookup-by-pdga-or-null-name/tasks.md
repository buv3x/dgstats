## 1. Repository Lookup Support

- [x] 1.1 Replace the city/country player repository finder with an exact-name query constrained to rows whose PDGA number is null.
- [x] 1.2 Confirm the derived query expresses `name = ?` and `pdga_num IS NULL` without adding metadata predicates or schema changes.

## 2. Importer Identity Logic

- [x] 2.1 Update `ImportService.savePlayer()` so an incoming PDGA number is looked up first by PDGA number.
- [x] 2.2 Add the fallback from an unmatched incoming PDGA number to the same exact name with a null stored PDGA number.
- [x] 2.3 Update no-PDGA lookup to use only the exact name with a null stored PDGA number.
- [x] 2.4 Preserve player metadata updates and ensure a matched null-PDGA player is upgraded with the incoming PDGA number.

## 3. Verification

- [x] 3.1 Review the importer and repository call paths to confirm city, country, nationality, profile URL, and other metadata are not used for identity selection.
- [x] 3.2 Compile the project and resolve any errors caused by the repository method or importer changes.

## 1. Export Data Model

- [x] 1.1 Add a personal-statistics export projection that includes player id, player name, PDGA number, basket course descriptors, basket descriptors, variation descriptors, round id, round date, round result rating, and score.
- [x] 1.2 Keep existing Course stats and Basket stats exported sample records free of player identity.
- [x] 1.3 Add export record types for player lookup entries, player personal-statistics snapshots, and personal variation rows.
- [x] 1.4 Extend export diagnostics and manifest metadata with eligible personal player count, generated personal file count, player lookup path, and personal stats path template or per-player path support.

## 2. Personal Rating Calculation

- [x] 2.1 Group mapped rated score samples by basket variation and calculate unweighted score-over-rating regression lines.
- [x] 2.2 Treat a variation as globally eligible only when it has at least 50 samples, non-zero rating variance, and a negative regression slope.
- [x] 2.3 Group globally eligible samples by player and variation.
- [x] 2.4 Treat a player/variation row as eligible only when the player has at least two results for that variation.
- [x] 2.5 Calculate each personal score rating with `(score - intercept) / slope`, then calculate the row rating as an equally weighted arithmetic average.
- [x] 2.6 Sort each row's score list by round date ascending with null dates last, then round id ascending.
- [x] 2.7 Sort each player's variation rows by decimal calculated rating descending with deterministic tie-breakers.

## 3. Personal Export Files

- [x] 3.1 Write `docs/data/players.json` containing only players with at least one eligible personal variation row.
- [x] 3.2 Format player lookup labels as name plus PDGA number when available.
- [x] 3.3 Write one personal statistics file per eligible player under `docs/data/personal-stats/`.
- [x] 3.4 Ensure personal export files include only allowed player identity fields and omit profile URL, city, country, nationality, and other unnecessary player identity.
- [x] 3.5 Update the export administration result display to show personal-statistics diagnostics.

## 4. Static Page UI

- [x] 4.1 Add a `Personal stats` navigation tab and view while preserving existing Course stats and Basket stats behavior.
- [x] 4.2 Load player lookup data from the manifest-provided path and show missing or empty personal-data states.
- [x] 4.3 Add autocomplete player input using eligible player display labels and exact selected player ids.
- [x] 4.4 Load the selected player's personal statistics file from the exported player path.
- [x] 4.5 Render one personal variation list showing basket course, basket label, variation label, rounded rating, personal result count, and comma-separated scores.
- [x] 4.6 Preserve exported personal-row order in the UI and reset the list when the player selection is cleared.
- [x] 4.7 Add focused styling for the personal controls and list consistent with the existing static page.

## 5. Verification

- [x] 5.1 Run the local statistics export and inspect generated `players.json` and at least one `personal-stats/*.json` file for eligibility, ordering, and allowed identity fields.
- [x] 5.2 Inspect `docs/data/statistics.json` to confirm personal-statistics paths and diagnostics are present.
- [x] 5.3 Compile the project successfully.
- [x] 5.4 Manually inspect the static page files for the third tab, autocomplete wiring, empty states, and personal variation list rendering logic.

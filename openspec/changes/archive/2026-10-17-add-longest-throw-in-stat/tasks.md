## 1. Extend the putting export model

- [x] 1.1 Extend the putting throw projection/model with the hole information needed by longest-throw-in occurrence descriptors.
- [x] 1.2 Add round-level candidate collection for successful throws with an effective recorded distance, preserving the existing distance fallback and valid-hole rules.
- [x] 1.3 Add longest-throw-in DTOs and aggregation for overall and yearly summaries, comparing exact feet values and converting only the selected maximum to rounded meters.
- [x] 1.4 Export every exact-feet matching occurrence with competition, round, round identity, and hole ordinal, while excluding incomplete rounds from aggregate highlights.

## 2. Update the Personal stats presentation

- [x] 2.1 Render overall and yearly longest-throw-in highlights using the existing highlight layout and multiple-occurrence suffix convention.
- [x] 2.2 Build accessible tooltip content listing competition, round, and hole for every longest-throw-in occurrence.
- [x] 2.3 Preserve compatibility with older personal-statistics JSON that does not contain the new highlight or occurrence descriptors.

## 3. Verify the change

- [x] 3.1 Review representative exported JSON for single, tied, missing-distance, drop-distance, and differently-rounded source distances.
- [x] 3.2 Compile the affected Java sources and verify the generated static assets remain structurally valid.

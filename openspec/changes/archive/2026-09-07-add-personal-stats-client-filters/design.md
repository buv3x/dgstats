## Context

The static basket statistics page already has a Personal stats view that loads `players.json`, lets the user select an eligible player, fetches that player's `personal-stats/{playerId}.json`, and renders the exported `variations` list. Each personal variation row already includes `basketCourseId`, `basketCourseName`, and `count`, so the requested filters can operate on the selected player's loaded JSON without changing export generation.

## Goals / Non-Goals

**Goals:**

- Add a Personal stats basket course filter populated from the selected player's loaded variation rows.
- Add a Personal stats minimum count filter with default and minimum value `2`.
- Apply Personal stats filters in the browser after player data is loaded.
- Preserve exported row ordering for rows that remain after filtering.
- Preserve existing Course stats and Basket stats behavior.

**Non-Goals:**

- No backend API, repository, or export calculation changes.
- No changes to player eligibility or generated personal statistics files.
- No persistence of Personal stats filter settings across page reloads.
- No multi-course selection or additional Personal stats filters.

## Decisions

### Derive course options from selected player data

When a selected player's personal statistics file loads, the page will scan `variations` and build a unique course list from `basketCourseId` and `basketCourseName`. The selectbox will include an all-courses option plus only courses present in that selected player's rows.

Alternative considered: populate from the manifest course list. That would expose courses where the selected player has no personal rows, which contradicts the request that values come only from the player's data.

### Keep filtering as a render-time client concern

The page will keep the loaded player snapshot unchanged and calculate visible rows by applying the selected course and minimum count controls during Personal stats rendering. This keeps the exported JSON authoritative and avoids duplicating backend eligibility logic.

Alternative considered: re-export player files after applying filters. That would make filters static, require backend/export work, and prevent interactive filtering on the page.

### Enforce minimum count at the input boundary and render boundary

The minimum count input will be configured with `min="2"` and default `value="2"`. JavaScript will also normalize empty, invalid, or lower values to `2` before filtering, so direct DOM edits or browser differences cannot produce lower thresholds.

Alternative considered: rely only on HTML input attributes. That is weaker because scripts and some browser interactions can still produce unexpected values.

## Risks / Trade-offs

- Filter controls may appear before a player is selected -> Keep course options empty or reset to all-courses until a valid player file loads.
- A selected filter can remove all rows -> Show a filtered-empty message while keeping the selected player and controls visible.
- Course names can contain non-ASCII text -> Use existing exported names as text content and compare by stable numeric/string course id.
- The selected player's course list changes when another player is selected -> Rebuild and reset Personal stats course filtering on each selected player load.

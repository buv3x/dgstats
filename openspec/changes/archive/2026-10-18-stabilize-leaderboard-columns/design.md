## Context

The static statistics page renders leaderboard rows dynamically for several category tabs and year scopes. The leaderboard table currently uses the browser's automatic layout algorithm, so the longest name or value in the selected dataset determines column positions. Because those contents vary between selections, the table shifts horizontally when the user changes tabs or years.

## Goals / Non-Goals

**Goals:**

- Make the leaderboard's column geometry identical for every category and year selection.
- Give the name column a fixed 300px width and left alignment.
- Give the value column a fixed 200px width and right alignment.
- Preserve the existing place column styling and narrow-screen horizontal scrolling.

**Non-Goals:**

- Changing leaderboard values, sorting, ranking, tooltips, or data export.
- Adding table headers or changing the row structure.
- Changing widths or alignment of course, basket, or personal-statistics tables.

## Decisions

- Use a leaderboard-specific fixed table layout with explicit widths for all three columns. The place column retains its current compact width, while name and value use 300px and 200px respectively. Explicitly sizing the place column prevents the browser from distributing leftover space into it.
- Apply alignment by leaderboard column: place remains right-aligned, names are left-aligned, and values are right-aligned. This overrides the generic table rules only within `.leaderboards-table`.
- Keep the existing table wrapper overflow behavior. On narrow screens, the fixed desktop-oriented columns remain stable and the wrapper provides horizontal scrolling instead of shrinking columns and reintroducing content-driven movement.
- Prefer CSS-only changes because the existing DOM already renders each leaderboard row in the required place/name/value order. A `colgroup` or equivalent column selectors may be used if needed to make widths explicit without changing runtime rendering.

## Risks / Trade-offs

- [Fixed columns require horizontal scrolling on small screens] → Preserve the existing `.table-wrap` horizontal overflow and verify the mobile view remains usable.
- [Long names or values may not fit within the fixed columns] → Preserve the current no-wrap behavior and allow the existing wrapper to scroll; do not alter the displayed data.
- [Generic table styles could override leaderboard alignment] → Keep leaderboard-specific selectors after the generic table rules and test all three columns.

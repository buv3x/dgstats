## 1. Implement fixed leaderboard layout

- [x] 1.1 Update the leaderboard table styling in `docs/styles.css` to use a fixed layout and explicit place, name, and value column widths.
- [x] 1.2 Set leaderboard names to left alignment at 300px and values to right alignment at 200px while preserving the existing place-column alignment.
- [x] 1.3 Preserve the existing table-wrapper horizontal scrolling behavior for narrow viewports.

## 2. Verify the presentation contract

- [x] 2.1 Inspect the rendered row structure and CSS selectors to confirm the fixed rules apply to every leaderboard category and year without changing data or ranking behavior.
- [x] 2.2 Check the final source diff for unintended changes outside the leaderboard presentation styles.

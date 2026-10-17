## 1. Filter eligible years in the putting renderer

- [x] 1.1 Add a renderer predicate that qualifies a year only when its rounds contain an explicit `complete: true` entry.
- [x] 1.2 Apply the predicate before creating year accordions while preserving global metrics and existing eligible-year rendering.
- [x] 1.3 Confirm incomplete-only years, legacy rounds without completeness metadata, and players with no qualifying years produce no year accordions or placeholders.

## 2. Verify the UI-only change

- [x] 2.1 Check JavaScript syntax and inspect representative generated payloads for complete-only, mixed, and incomplete-only year cases without changing the export files.
- [x] 2.2 Confirm the existing exporter and full application test/compile checks remain unaffected by the renderer-only change.

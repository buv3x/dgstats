## Context

The basket statistics exporter writes `docs/data/statistics.json`, but the current implementation uses a relative `Path`. A relative path is resolved from the JVM process working directory, which can vary depending on whether the app is started from Maven, an IDE, a packaged jar, or another launcher.

For this local project workflow, the export must land in the repository checkout so the generated file is immediately available to `docs/index.html` and GitHub Pages publishing.

## Goals / Non-Goals

**Goals:**

- Resolve the statistics export file under the project directory root.
- Continue writing to `docs/data/statistics.json` relative to that root.
- Display the absolute resolved export path after export.
- Keep the export behavior deterministic even when the JVM working directory is not the project directory.

**Non-Goals:**

- Do not change snapshot JSON content or schema.
- Do not change static page behavior.
- Do not introduce a configurable export destination unless needed later.
- Do not add GitHub Pages deployment automation.

## Decisions

1. Resolve the project root from the application classpath location during local development.

   The running Spring Boot app can locate its compiled application resources. During Maven/IDE local runs, those resources live under `target/classes`, so the repository root is two directories above that location. The export service should resolve and normalize that root, then append `docs/data/statistics.json`.

   Alternative considered: keep relying on `Path.of("docs", "data", "statistics.json")`. That is what caused files to appear outside the checkout when the JVM working directory differed.

2. Return and display the absolute output path.

   The export result should include the normalized absolute path to the generated JSON file. This makes it immediately obvious where the file was written and avoids ambiguous UI messages like `docs\data\statistics.json`.

   Alternative considered: display only the project-relative path. That is cleaner but does not help diagnose launch-directory problems.

3. Keep the path fixed rather than user-configurable.

   The requested workflow has one canonical static site location: `docs/index.html` loading `docs/data/statistics.json`. Keeping the export path fixed avoids unnecessary UI and configuration surface.

   Alternative considered: add an application property for export path. That can be introduced later if packaged-jar export outside the repository becomes a real use case.

## Risks / Trade-offs

- Packaged jar layouts differ from Maven/IDE classpath layouts -> Keep this scoped to the local project workflow and fail visibly if the root cannot be resolved.
- Classpath-derived path logic is less obvious than a relative path -> Encapsulate it in the export service and display the resolved absolute path.
- Fixed project-root export does not support arbitrary output directories -> This matches the current GitHub Pages-ready `docs/` workflow.

## Migration Plan

No database or data migration is required. Existing misplaced files, if any, can be ignored or deleted manually. Rollback is returning to the previous relative path behavior.

## Open Questions

- None blocking.

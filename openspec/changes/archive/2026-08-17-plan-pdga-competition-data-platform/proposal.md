## Why

PDGA competition data is valuable for analysis and visualization, but the project direction should be clarified before implementation begins. A high-level plan is needed to define the intended stages, boundaries, risks, and open questions without committing this change to importer, database, export, or UI work.

## What Changes

- Create a high-level planning artifact for the PDGA competition data project.
- Assess the two intended project stages: local PDGA data import/storage and static GitHub Pages visualization.
- Capture project boundaries, assumptions, risks, and open questions.
- Explicitly avoid planning implementation work as part of this change.
- Defer importer code, database schema changes, snapshot export, visualization pages, and deployment setup to later implementation changes.

## Capabilities

### New Capabilities
- `pdga-data-import`: Covers the planning section for future local PDGA data import and database storage.
- `pdga-data-snapshot`: Covers the planning section for future static snapshot data files.
- `pdga-static-visualization`: Covers the planning section for future GitHub Pages visualization.

### Modified Capabilities

None.

## Impact

- Affected artifacts are limited to OpenSpec planning documents.
- No application code, database schema, data export, static site, or deployment behavior is changed by this planning change.
- Later implementation changes can use the high-level plan as input when defining concrete requirements and tasks.

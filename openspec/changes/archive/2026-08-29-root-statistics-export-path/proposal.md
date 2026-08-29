## Why

The basket statistics export currently uses a relative path, so the generated snapshot can be written under the Java process working directory instead of the project checkout. The export needs a deterministic project-root location so local browser exports create `docs/data/statistics.json` in the repository.

## What Changes

- Resolve the basket statistics export output path from the project directory root.
- Keep the exported file location as `docs/data/statistics.json` under that root.
- Show the resolved absolute export path in the local export result so misplaced output is visible immediately.
- Preserve the existing snapshot format, filtering, diagnostics, and static page behavior.

## Capabilities

### New Capabilities

- `project-root-statistics-export-path`: Deterministic project-root path resolution for the generated basket statistics snapshot.

### Modified Capabilities

- None.

## Impact

- Updates the basket statistics export service path resolution.
- Updates the export result displayed by the local admin page.
- Does not change the JSON schema, static UI, database schema, import behavior, or basket mapping behavior.

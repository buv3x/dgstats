## Context

This artifact is the high-level plan for a future PDGA competition data project. It does not authorize or plan implementation work in this change.

The broader project idea has two stages:

1. Import competition data from PDGA web services into a local database for repeatable analysis.
2. Publish static visualizations through GitHub Pages using exported snapshot files rather than a live database.

The repository already contains early application structure, PDGA API references, sample payloads, JPA/Liquibase dependencies, and an initial PostgreSQL-oriented Liquibase schema. Those details are useful context, but this change stops at planning.

## High-Level Assessment

The proposed two-stage direction is sound because it separates concerns cleanly:

- Stage 1 builds a local data acquisition and persistence foundation.
- Stage 2 publishes a static visualization backed by exported snapshot files.

That separation avoids requiring a deployed backend or database for GitHub Pages while still allowing the local database model to support richer import and analysis needs. The main planning risk is data scope: the schema and exported files should be driven by the questions the visualization and analysis need to answer, not only by the first PDGA responses that are easiest to import.

## Goals / Non-Goals

**Goals:**

- Produce a high-level plan for the future PDGA competition data project.
- Clarify the intended two-stage structure.
- Preserve the distinction between local database ingestion and static public visualization.
- Identify open questions that should be resolved before implementation planning begins.

**Non-Goals:**

- No implementation work in this change.
- No implementation task plan in this change.
- No system behavior requirements intended for direct implementation in this change.
- No externally deployed backend service.
- No live database access from GitHub Pages.
- No final commitment yet to specific UI charts, export file format, scheduling mechanism, or complete PDGA data model.
- No automated production import scheduling unless introduced by a later change.

## High-Level Plan

### Step 1: Local PDGA Data Import And Storage

The future first implementation stage should focus on getting selected competition data from PDGA web services and storing it in a local database. Execution is expected to be manual and local only. The database schema can follow the existing Liquibase direction, but it may need to evolve once the desired analysis questions and required PDGA fields are clearer.

This stage should answer:

- Which competitions are in scope?
- Which PDGA endpoints and fields are needed?
- Whether raw responses, normalized records, or both should be stored?
- What import status and failure visibility is useful for manual operation?

### Step 2: Static Snapshot And Visualization

The future second implementation stage should export selected database contents into static files committed or published with the repository. The visualization should be plain browser-delivered HTML, JavaScript, and CSS suitable for GitHub Pages.

This stage should answer:

- Which data format is best for browser loading: JSON, CSV, pre-aggregated files, or a combination?
- How large the snapshot can be while remaining usable from GitHub Pages?
- Which visualizations are valuable enough to build first?
- How the UI communicates snapshot freshness and data scope?

## Planning Decisions

1. Keep imports local and manually executed.

   Rationale: this matches the project direction and avoids operational work around hosting, credentials, monitoring, and scheduled jobs during the early stage.

2. Treat the database as the authoritative local working store.

   Rationale: local analysis, deduplication, repeatable exports, and schema evolution are easier when PDGA data is persisted before visualization.

3. Treat static snapshot files as the public UI contract.

   Rationale: GitHub Pages can reliably serve HTML, JavaScript, CSS, and static data files, but cannot query the local database. The snapshot files should therefore be the stable interface between the local import process and the published visualization.

4. Let analysis questions drive data breadth.

   Rationale: PDGA responses may contain more fields than the project needs. The schema and exports should prioritize fields needed for selected analyses and visualizations, while allowing later expansion if new questions require more data.

5. Keep PDGA web services as an external integration boundary.

   Rationale: PDGA endpoint shape, availability, and access expectations are outside this project and should be clarified before implementation.

## Risks / Trade-offs

- PDGA web service behavior changes or undocumented limits -> Capture endpoint assumptions before implementation.
- Initial schema may be too narrow for later analysis -> Resolve target analysis questions before expanding tables.
- Static snapshot files can become large or awkward for browser use -> Define export scope before choosing a file format.
- Manual import keeps operations simple but can make updates inconsistent -> Plan for a documented refresh workflow in a later change.
- Published visualizations may imply freshness that static data cannot guarantee -> Plan for snapshot metadata and UI freshness indicators.
- Raw PDGA data may include fields that should not be republished blindly -> Decide what data is appropriate to store and expose before exporting public snapshots.

## Open Questions

- Which PDGA competitions should be imported initially: a fixed known list, date/season range, event tier, geography, divisions, or another selection rule?
- Which analyses should the first visualization answer?
- What is the minimum data needed for those analyses: event metadata, divisions, rounds, players, hole scores, ratings, layouts, course data, or other fields?
- Should the local database store raw PDGA responses, normalized records, or both?
- What level of historical reproducibility is needed when PDGA data changes after an event?
- Which static export format is preferred for the UI: JSON, CSV, multiple files by entity, pre-aggregated files, or a combination?
- How large can the exported dataset reasonably be for GitHub Pages and browser-side loading?
- Should exported snapshot files be committed to the main repository, generated into a separate pages branch, or stored under a dedicated docs/public folder?
- What metadata should identify a snapshot: import time, source endpoint list, competition list, schema/export version, or source data timestamp?
- Are there PDGA usage, attribution, or redistribution constraints that affect stored or published data?
- What is the expected audience of the visualization: personal exploration, public portfolio, disc golf community, or analysis notebook companion?
- Should the project prioritize correctness and completeness of the import first, or deliver a thin end-to-end import-to-page slice first?

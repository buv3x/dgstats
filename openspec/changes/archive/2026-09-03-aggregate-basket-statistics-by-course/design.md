## Context

The current basket statistics export writes one human-readable JSON snapshot to `docs/data/statistics.json`. That snapshot contains a `competitions` list and all exported score samples. The static page loads the whole snapshot on startup, populates a competition selector, then filters samples by selected competition and rating bounds before grouping by basket and basket variation.

The manual basket model already has the needed course relationship:

```text
basket_course -> basket -> basket_variation -> basket_variation_round_division -> round_division -> round -> competition
```

This change makes the manual basket course the primary published aggregation boundary. The static page will not expose a competition filter; selected course statistics aggregate across every exported competition that has mapped, rated samples for that basket course.

## Goals / Non-Goals

**Goals:**

- Export a lightweight manifest at `docs/data/statistics.json` listing basket courses with available statistics.
- Export course-scoped score sample files under `docs/data/courses/`.
- Let the static page choose a basket course as the main selector.
- Aggregate all selected course samples across competitions, constrained only by rating bounds.
- Preserve enough competition identity in course files for provenance and future debugging.
- Keep player-identifying data out of all exported statistics files.
- Report diagnostics that reflect course-scoped export output.

**Non-Goals:**

- No competition filter in the static page.
- No change to PDGA import behavior.
- No change to basket course administration behavior.
- No database schema change.
- No pre-aggregation of statistics in exported JSON; the static page continues to calculate table values from score samples.

## Decisions

1. Use `docs/data/statistics.json` as a manifest, not the full sample snapshot.

   The existing static page already loads this relative path, and the project-root path spec already points export output under `docs/data`. Keeping the filename as the manifest creates a stable initial entrypoint while avoiding one global sample payload.

   Alternative considered: rename the manifest to `manifest.json` and remove `statistics.json`. That is cleaner semantically, but it creates a larger published contract break and requires every existing reference to move.

2. Write one course sample file per basket course.

   Course files should live under `docs/data/courses/{basketCourseId}.json` and contain the selected course descriptor, optional competition descriptors for provenance, and the course's score samples. This bounds file size by the selector the user actually chooses.

   Alternative considered: write one file per competition and have the course selector load many files. That keeps files small but makes course selection require multiple network requests and puts more orchestration in static JavaScript.

3. Keep competition identity in course-scoped samples, but do not expose a competition filter.

   The user decision is course-only selection. Keeping `competitionId` in samples and a `competitions` descriptor list preserves traceability without adding UI complexity or changing the aggregation rule.

   Alternative considered: remove competition identity entirely. That minimizes JSON, but it makes exports harder to audit and closes off future static UI improvements.

4. Export only courses with mapped, rated samples.

   This mirrors the current behavior that hides empty competitions and avoids presenting course options that cannot render statistics.

   Alternative considered: list all basket courses from `basket_course`. That would make the selector reflect administration data, but it would produce empty pages for courses without eligible statistics.

5. Continue client-side aggregation.

   The static page already computes Count, Average, and score bucket percentages from score samples. Keeping this model minimizes backend complexity and preserves rating filter semantics without exporting precomputed bucket combinations.

   Alternative considered: export pre-aggregated statistics by rating bucket. That would shrink files but would constrain future rating filter behavior or require many precomputed ranges.

## Risks / Trade-offs

- Existing published `docs/data/statistics.json` consumers break because the file becomes a manifest instead of a full snapshot -> Treat this as a breaking contract change and update the static page in the same implementation.
- Course files can become large for heavily played courses -> Splitting by course prevents unrelated courses from increasing initial page load; later work can add yearly or competition sharding if one course becomes too large.
- Stale course files may remain when a later export no longer includes a course -> Either clean `docs/data/courses` before writing or overwrite known generated files and ensure the manifest is authoritative.
- Course selection triggers asynchronous file loading -> The static page should show loading, missing-data, and empty-result states separately so users can distinguish missing export files from valid empty filters.
- `BasketStatisticsExportService.resolveProjectRoot()` is already specialized for the current project layout -> Keep the existing root resolution behavior unless implementation discovers it cannot address the new nested output paths.

## Migration Plan

1. Update export query/projection to include basket course identity through `basket.basketCourse`.
2. Change export service records from one snapshot with all samples to a manifest plus course snapshots.
3. Write `docs/data/statistics.json` and one file per included course under `docs/data/courses/`.
4. Update the export admin result to show manifest path, course file count, included courses, and diagnostics.
5. Update the static page to load the manifest, populate the course selector, fetch the selected course file, and aggregate course samples without a competition filter.
6. Regenerate `docs/data` with the new export.

Rollback is to restore the previous single-snapshot export and static page contract, then regenerate `docs/data/statistics.json`.

## Open Questions

- Should the export delete stale files under `docs/data/courses` before writing the current course files, or should it leave cleanup manual and rely on the manifest as the only source of truth?

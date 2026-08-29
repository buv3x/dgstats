## 1. Export Path Resolution

- [x] 1.1 Add project-root path resolution to the basket statistics export service.
- [x] 1.2 Resolve the output file as `<project-root>/docs/data/statistics.json` before creating directories or writing JSON.
- [x] 1.3 Return the normalized absolute output path in the export result.

## 2. Export Admin Feedback

- [x] 2.1 Ensure the export admin page displays the absolute resolved output path after export.
- [x] 2.2 Preserve existing export diagnostics and snapshot JSON structure.

## 3. Verification

- [x] 3.1 Compile the Spring application successfully.
- [x] 3.2 Verify the OpenSpec change status shows all required artifacts complete.

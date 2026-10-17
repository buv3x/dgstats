## 1. Filter displayable putting rounds

- [x] 1.1 Add a UI display-list filter that omits incomplete rounds when both C1x and C2 total counts are zero.
- [x] 1.2 Preserve complete rounds, legacy rounds without completeness metadata, and incomplete rounds with recorded attempts.
- [x] 1.3 Create the round-details toggle and container only when the filtered display list is non-empty.

## 2. Verify presentation behavior

- [x] 2.1 Confirm displayed incomplete rounds retain the existing italic and strikethrough styling.
- [x] 2.2 Confirm overall totals remain visible when all incomplete rounds are filtered out.
- [x] 2.3 Compile the application and inspect the final change scope for export/data preservation.

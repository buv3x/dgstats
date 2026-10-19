## 1. Extend putting highlight export data

- [x] 1.1 Update the most-C2+ and best-C1x highlight records to carry ordered descriptors for every matching complete round, preserving round identity and display labels.
- [x] 1.2 Update highlight aggregation to collect matching round descriptors using the same comparison and tie-break rules as the existing maximum/best-value calculations.
- [x] 1.3 Preserve legacy-safe serialization and existing visible values when no matching descriptor list is available.

## 2. Update putting statistics presentation

- [x] 2.1 Change overall and yearly putting totals so C1x and C2 render on separate lines without changing metric formatting or labels.
- [x] 2.2 Reduce round-detail typography to 14px while preserving the existing competition/round format, bold labels, incomplete styling, and expand/collapse behavior.
- [x] 2.3 Render the complete Most C2+ displayed value as an accessible tooltip trigger whenever matching round descriptors exist, including a single match.
- [x] 2.4 Render the complete Best C1x displayed value as an accessible tooltip trigger whenever matching round descriptors exist, including a single match.
- [x] 2.5 Format tooltip content as a readable list of matching competition and abbreviated round labels and retain the existing tooltip delegation and keyboard behavior.

## 3. Verify the change

- [x] 3.1 Review generated export JSON and static-page code paths for overall and yearly highlights, legacy data handling, and separate aggregate rows.
- [x] 3.2 Compile the affected Java sources and confirm the generated DTO/serialization changes compile without modifying unrelated functionality.

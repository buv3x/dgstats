## 1. Remove redundant snapshot metadata

- [x] 1.1 Remove the `snapshot-meta` header element from `docs/index.html` and remove the unused metadata rendering path from `docs/app.js`.
- [x] 1.2 Confirm no visible `Exported at` text or empty metadata layout remains in the static page assets.

## 2. Change Personal stats ordering

- [x] 2.1 Update `comparePersonalRows` in `docs/app.js` to compare exported decimal personal ratings descending before course, basket, and variation tie-breakers.
- [x] 2.2 Verify `personalFilteredRows` applies the rating-first comparator after course and minimum-count filtering, while row display continues to use the existing rounded rating.

## 3. Verify the static assets

- [x] 3.1 Check the modified JavaScript for syntax errors and confirm all references to removed DOM elements are gone.
- [x] 3.2 Confirm the change leaves manifest/export data, rating calculations, Description content, and non-personal statistics ordering unchanged.

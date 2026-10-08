## 1. Separate effective filtering from visible input normalization

- [x] 1.1 Add a non-mutating helper in `docs/app.js` that derives the effective Personal Stats minimum count, using `2` for empty, invalid, non-finite, or below-minimum values and flooring valid values.
- [x] 1.2 Update Personal Stats filtering to use the non-mutating effective-value helper so rendering does not repopulate an input that is temporarily empty.
- [x] 1.3 Keep commit, initialization, and reset paths using visible-value normalization so empty or invalid committed values display as `2`.

## 2. Verify the behavior

- [x] 2.1 Review the input and change event flow to confirm a cleared field remains editable during input and is normalized on commit.
- [x] 2.2 Verify existing minimum-count, course-filter, sorting, and empty-result behavior remains unchanged through static code inspection and JavaScript syntax validation.

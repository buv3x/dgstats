## 1. Make Course stats the default view

- [x] 1.1 Update the initial navigation classes and section visibility in `docs/index.html` so Course stats is active and Description is hidden.
- [x] 1.2 Update `initializePage()` in `docs/app.js` to call `setActiveView("course")` while preserving existing course-data initialization.
- [x] 1.3 Confirm selecting Description still displays the embedded Description and selecting the other views remains unchanged.

## 2. Refresh embedded Description content

- [x] 2.1 Replace the current Description article with the content from `description/description.txt`.
- [x] 2.2 Render the new topics as regular headings and paragraphs, preserving the three manifest-backed placeholder elements in General Information.
- [x] 2.3 Render the numbered Future Improvements and Suggestions entries as an ordered list and preserve the remaining source content and order.

## 3. Verification

- [x] 3.1 Inspect the initial HTML and JavaScript state to confirm Course stats is the default and Description remains last in navigation.
- [x] 3.2 Check that all source headings and placeholder elements are present in the embedded HTML and that no runtime Description fetch is introduced.
- [x] 3.3 Run JavaScript syntax and project compilation checks without changing unrelated work.

## 1. Description rendering

- [x] 1.1 Update the Description parser in `docs/app.js` to group content under each top-level heading into a `details` section with a `summary` label.
- [x] 1.2 Make the General Information section initially open while leaving all other sections collapsed and independent.
- [x] 1.3 Preserve paragraph flushing, placeholder replacement, safe inline formatting, and loading/error state behavior.

## 2. Accordion presentation and accessibility

- [x] 2.1 Update `docs/styles.css` to style the generated disclosure sections consistently with the existing Description card, typography, spacing, and borders.
- [x] 2.2 Ensure each disclosure control retains native keyboard and expanded/collapsed semantics without introducing a dependency or sibling-state controller.

## 3. Verification

- [x] 3.1 Inspect the generated static page behavior and confirm the six source sections appear in order, General Information is open by default, and multiple sections can remain open.
- [x] 3.2 Confirm description loading, unavailable-asset messaging, placeholders, and inline formatting remain intact.

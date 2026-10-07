## 1. Defer Personal stats player handling

- [x] 1.1 Add a cancellable zero-delay scheduler in `docs/app.js` for `handlePersonalPlayerInput`.
- [x] 1.2 Update the Personal stats `input` and `change` listeners to use the scheduler, preserving the existing player lookup, reset, messaging, and request-id behavior.
- [x] 1.3 Confirm the scheduler coalesces the normal datalist event sequence and cannot issue duplicate requests for one committed selection.

## 2. Verify the static page behavior

- [x] 2.1 Check the modified JavaScript for syntax errors and verify no unrelated static-page handlers or data assets changed.
- [x] 2.2 Review the acceptance scenarios for mouse selection, keyboard selection, exact typing, clearing, partial input, and rapid player changes across Chrome, Edge, and Firefox.

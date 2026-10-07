## Context

Personal stats uses a native text input backed by a dynamically populated `<datalist>`. `docs/app.js` handles both `input` and `change`, but Chromium-based browsers can expose the committed datalist value at a different point in the event sequence than Firefox. As a result, the handler can read the previous value and leave the previous player's results visible until another interaction occurs.

The fix must remain small, preserve the current static-page architecture, and avoid replacing the native picker or changing exported data.

## Goals / Non-Goals

**Goals:**

- Process the player input after the browser has committed a datalist selection.
- Coalesce closely related `input` and `change` events into one state update and one statistics request.
- Preserve existing exact-label validation, empty/partial-input messages, request cancellation guards, and rendering behavior.
- Keep the implementation dependency-free and compatible with the static GitHub Pages-style export.

**Non-Goals:**

- Do not introduce a custom combobox or third-party UI library.
- Do not change player lookup JSON, personal statistics JSON, backend APIs, or export generation.
- Do not alter the personal statistics filters or table ordering.

## Decisions

- Add a short zero-delay deferred scheduler around `handlePersonalPlayerInput`, and use it for both `input` and `change` listeners. Reading the value in the deferred callback gives Chromium time to commit the native datalist choice while remaining effectively immediate to the user.
- Cancel an already scheduled callback before scheduling a new one. This prevents the normal `input` + `change` event pair from clearing/loading the player twice or issuing duplicate fetches.
- Keep `handlePersonalPlayerInput` as the single state-transition function. The scheduler only controls timing; lookup, request sequencing, and messages remain centralized in the existing function.
- Validate with browser-level interaction checks in Chrome, Edge, and Firefox: mouse selection, keyboard selection, exact typing, clearing, partial text, and rapid changes between players.

Alternatives considered:

- Listening only to `change` was rejected because text-input `change` can wait for blur and reproduces the reported delayed behavior.
- Listening only to `input` was rejected because browser datalist implementations do not expose selection timing identically.
- Replacing `<datalist>` with a custom ARIA combobox was rejected for this low-risk fix because it increases accessibility, keyboard, styling, and maintenance scope.

## Risks / Trade-offs

- [Risk] A deferred callback could process a value after a very rapid sequence of edits → Mitigation: cancel the previous scheduled callback and retain the existing request-id guard for already-started fetches.
- [Risk] The native datalist remains browser-dependent in appearance and interaction details → Mitigation: this change targets the observed stale-value timing issue only; a custom combobox can be proposed separately if broader native-control inconsistencies remain.
- [Risk] A player may be typed exactly rather than selected from the list → Mitigation: preserve the current exact exported-label lookup behavior.

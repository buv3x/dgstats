## Context

The static Personal Stats page uses a numeric minimum-count input with a default and minimum of `2`. The input's `input` event re-renders the table, and the filtering path currently normalizes the DOM value on every render. Clearing the field therefore immediately writes `2` back into the field, preventing normal replacement editing.

The page needs to distinguish between the value being edited and the effective value used for filtering. The effective value must remain safe and deterministic while the visible input must be allowed to be temporarily empty.

## Goals / Non-Goals

**Goals:**

- Preserve an empty field during active editing.
- Use `2` as the effective filter value whenever the field is empty, invalid, fractional, or below the minimum.
- Normalize the visible field to `2` when the input change is committed.
- Keep existing course filtering, sorting, and default behavior unchanged.

**Non-Goals:**

- Changing the minimum allowed count from `2`.
- Changing exported statistics data or server-side APIs.
- Adding a new UI control or persistence mechanism.

## Decisions

Use separate responsibilities for reading the effective filter value and normalizing the visible input:

- The render/filter path will parse the current input and fall back to `2` without writing to the input element. This allows the `input` event to render using the default while the field remains empty.
- The `change` path and initialization/reset paths will continue to normalize the visible value to `2`, so an empty or invalid value is corrected after editing is committed.
- Values at least `2` will continue to be floored to an integer before filtering; values below `2`, empty, invalid, or non-finite values will use `2`.

The alternative of removing filtering during an empty transient state was rejected because it would make the table behavior depend on an invalid/incomplete threshold rather than the established default.

## Risks / Trade-offs

- [Users may expect the visible field to show `2` immediately after clearing] → Keep the existing `change` normalization so the default is restored when editing ends, while preserving the empty state only during active input.
- [The filtering and normalization paths could diverge] → Centralize parsing rules in a shared effective-value helper and cover empty, invalid, below-minimum, and valid values in tests or verification.

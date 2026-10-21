## Why

The Scoring Distribution description needs clearer grammar and wording so users can understand the score buckets, rating-window calculation, SPRW relationship, and chart-line visibility rules.

## What Changes

- Replace the existing Scoring Distribution description with the approved, grammar-corrected paragraph.
- Clarify that the Hole stats calculation uses the same “triangular weight” method used by SPRW.
- Preserve the existing statistical meaning, score groupings, filtering behavior, and default line-visibility rules.

## Capabilities

### New Capabilities

None.

### Modified Capabilities

- `static-basket-statistics-page`: Update the Description view's Scoring Distribution explanatory text.

## Impact

- Affects the static statistics page description content, likely in the Description view markup or its associated content source.
- No API, data-export, calculation, or chart-behavior changes are intended.

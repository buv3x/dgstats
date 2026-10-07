## Context

The static statistics page currently renders compact chart labels and table headers without contextual explanations. The SPR/VAR chart already has interactive data-point tooltips, but its axis/heading metric labels do not explain the abbreviations. Course stats and Personal stats table headers are rendered partly in HTML and partly through JavaScript, while Basket stats uses a selector label and chart heading in the static markup.

The change is presentation-only. It must preserve the existing chart data, calculations, table values, filtering, and export contracts.

## Goals / Non-Goals

**Goals:**

- Provide reusable custom tooltips for explanatory UI labels and headers.
- Make tooltip triggers visibly identifiable with a dotted underline.
- Show tooltips on pointer hover and keyboard focus.
- Give SPR and VAR separate tooltip triggers in the SPR/VAR chart heading.
- Keep all requested wording centralized enough to avoid inconsistent copies.
- Update the requested Basket stats and Personal stats terminology.

**Non-Goals:**

- Change existing chart point/arrow tooltips.
- Change chart calculations, table values, exports, or API contracts.
- Reformat the broader description content.
- Add tooltips to unrelated controls or navigation tabs.

## Decisions

### Use a reusable custom tooltip trigger

Add a shared tooltip-trigger presentation pattern for labels and table headers. Each trigger exposes its explanation through an explicit tooltip-content value and is rendered as a focusable inline element where the surrounding element is not already interactive. CSS provides the dotted underline and custom tooltip surface for hover/focus states; JavaScript may be used for positioning or focus/escape behavior if needed by the existing browser support target.

This is preferred over the native `title` attribute because the request requires a consistent dotted-underline treatment and custom visual presentation. Existing chart tooltips remain a separate chart interaction.

### Keep metric triggers separate

The `SPR / VAR` heading will contain two independent tooltip triggers so users can inspect either metric without showing an ambiguous combined definition. The Basket stats `SPRW` heading will have one trigger.

### Attach tooltips to semantic label text

Tooltips will wrap the visible text of the affected headings and table headers rather than changing the displayed values. This keeps column alignment and dynamic row rendering intact. The Personal stats `Variation` header will be changed to `Layout` while the row data remains unchanged.

### Use corrected, exact copy

The UI will use `Measurement` and `recorded results`. Score bucket headers `3`, `4`, `5`, `6`, and `7` each receive the corresponding exact-throw-count explanation; `1-2` and `8+` retain range-specific wording.

## Risks / Trade-offs

- [Tooltip overlap near viewport edges] → Position the tooltip within its containing region and allow wrapping for long table-header explanations.
- [Keyboard users cannot discover hover-only content] → Make non-interactive tooltip triggers focusable and show the same custom tooltip on focus.
- [Mobile devices do not have hover] → Support focus/tap-compatible activation or retain the explanatory text in accessible labeling so the information remains discoverable without a pointer hover.
- [Duplicated static and dynamic markup becomes inconsistent] → Use one shared class/attribute convention and centralize tooltip strings in the UI layer where practical.
- [Long tooltip copy makes narrow tables difficult to use] → Keep the tooltip surface unconstrained by table cell width, allow wrapping, and avoid changing table layout.

## 1. Competition Mapping Indicator

- [x] 1.1 Add repository support for retrieving competition ids that have at least one persisted basket variation round-division mapping.
- [x] 1.2 Extend the mapping editor competition option model with a mapped flag derived from the repository result.
- [x] 1.3 Render mapped competition options with a `* ` prefix while preserving existing name and date label content.

## 2. Same-Layout Editor Model and Save

- [x] 2.1 Add same-layout request handling to the mapping editor GET and POST controller paths.
- [x] 2.2 Carry same-layout mode in the editor model so the template can preserve checkbox state across competition and basket course changes.
- [x] 2.3 Extend save logic to expand first displayed division submissions to corresponding basket ordinals in the remaining divisions for each round when same-layout mode is enabled.
- [x] 2.4 Preserve existing per-cell validation against selected competition slots and selected basket course variation ids for both normal and expanded same-layout submissions.

## 3. Mapping Editor Template Behavior

- [x] 3.1 Add the `Same layout for all divisions` checkbox near the existing competition and basket course controls.
- [x] 3.2 In same-layout mode, render only the first displayed division per round as editable and render the remaining divisions as display-only copied values.
- [x] 3.3 Add browser-side mirroring so display-only division values update when the first displayed division changes before save.
- [x] 3.4 Ensure same-layout state is submitted on save and preserved through control form reloads.

## 4. Verification

- [x] 4.1 Compile the project and resolve any compile errors from model, controller, repository, or template changes.
- [x] 4.2 Inspect the rendered template logic against the OpenSpec scenarios for mapped competition labels, same-layout source columns, display-only copied columns, and same-layout save submission.

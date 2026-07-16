# Forms
> Demonstrate form interactivity — validation feedback, auto-growing fields, styled selects — with HTML and CSS only.

## Boundary
- `view-validation-demo` — constraint validation with interaction-aware feedback
- `view-field-sizing-demo` — a textarea that grows with its content
- `view-custom-select-demo` — a fully styled native select

## Requirements
### R1: Validation
- R1.1 — While a field is untouched, the BC shall show no validation feedback. _(why: invalid-on-pristine trains users to ignore red)_
- R1.2 — When the user commits an invalid value, the BC shall mark the field invalid.
- R1.3 — When the user commits a valid value, the BC shall mark the field valid.
- R1.4 — If a required field is empty on submission, then the BC shall block the submission.

### R2: Field sizing
- R2.1 — When the content exceeds one line, the BC shall grow the textarea to fit the content.
- R2.2 — The BC shall cap the textarea's growth at a maximum height.

### R3: Custom select
- R3.1 — The BC shall style the select's trigger and option list while keeping the native select element.
- R3.2 — The BC shall render non-text content inside options.

## Out of scope
- Submitting data anywhere — forms never leave the page.
- Scripted validation of any kind.

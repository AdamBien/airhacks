# Checks — forms

- [R1.1] /forms/validation.html on load: email input border-color equals the neutral `--border` value and no error text is visible
- [R1.2] /forms/validation.html: type "nope" into Email, press Tab — "Please enter a valid email address." becomes visible and the border switches to the alert color
- [R1.3] /forms/validation.html: type "dev@sun.com" into Email, press Tab — border switches to the ok color and the label gains a check mark
- [R1.4] /forms/validation.html: leave Handle empty, click Submit — the URL does not change (browser blocks submission)
- [R2.1] /forms/field-sizing.html: typing three lines into the textarea increases its rect height
- [R2.2] /forms/field-sizing.html: typing twenty lines plateaus the textarea height at its max-block-size (12rem)
- [R3.1] /forms/custom-select.html: the control is a native `select` element and its computed `appearance` is `base-select`
- [R3.2] /forms/custom-select.html: options contain `span.swatch` child elements

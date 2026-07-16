# address

Captures postal addresses through a validated input form and stores them in the application state.

## Design Decisions

- Form input accumulates field by field in a temporal draft object; the address list stays untouched until the draft is committed.
- Saving stamps the draft with `Date.now()` as entity id, appends it to the list, and resets the draft — clearing the form for the next entry.
- Validation is native HTML constraint validation (`required`, `reportValidity()`); no custom error display.
- `autocomplete` tokens (`name`, `street-address`, `postal-code`, `address-level2`, `country-name`) enable browser autofill.

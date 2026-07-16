# addresses

Captures postal addresses through a validated input form and stores them in the application state.

## Design Decisions

- Form input accumulates field by field in a temporal draft object; the address list stays untouched until the draft is committed.
- Saving stamps the draft with `Date.now()` as entity id, appends it to the list, and resets the draft — clearing the form for the next entry.
- Validation is native HTML constraint validation (`required`, `reportValidity()`); no custom error display.
- `autocomplete` tokens (`name`, `street-address`, `postal-code`, `address-level2`, `country-name`) enable browser autofill.
- The saved list stays in insertion order; sort criteria (`sort: {by, ascending}`) live in the state and the table derives the sorted view — clicking a column header toggles the direction.
- Inline cell editing is state-driven: a cell click marks `edit: {id, field}`, the table renders exactly that cell as an input. Enter or leaving the field commits, Escape cancels; typing itself never dispatches.
- The table paginates client-side: the zero-based `page` lives in the state (clamped by the reducer to the existing range), `PAGE_SIZE` is an entity constant — pagination applies after sorting.
- The control layer splits by direction: `CRUDControl` owns the write side (dispatchers wrapping actions), `QueryControl` the read side (pure sorting/paging derivation, no store access). The boundary renders what QueryControl returns and never computes.

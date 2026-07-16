# Structure
> Demonstrate structural reactivity — parent-state styling, container-adaptive layout, exclusive disclosure — with HTML and CSS only.

## Boundary
- `view-has-demo` — parent and sibling styling reacting to child state
- `view-container-queries-demo` — a component adapting to its container's width
- `view-accordion-demo` — an exclusive accordion with animated disclosure

## Requirements
### R1: Parent-state styling
- R1.1 — While a card contains a checked toggle, the BC shall restyle that card.
- R1.2 — While no card is selected, the BC shall show a hint prompting selection.

### R2: Container queries
- R2.1 — While the component's container is narrow, the BC shall stack the component's layout.
- R2.2 — While the component's container is wide, the BC shall lay the component out horizontally.
- R2.3 — The BC shall let the user resize the demo container. _(why: makes the container query observable on one screen)_

### R3: Exclusive accordion
- R3.1 — When the user opens an accordion item, the BC shall close the previously open item.
- R3.2 — When an accordion item opens or closes, the BC shall animate its content's disclosure.

## Out of scope
- Persisting selection or disclosure state across reloads.

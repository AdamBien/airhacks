# Motion
> Demonstrate CSS-driven motion — element continuity across pages, enter/exit animation, scroll-driven effects, carousels — with HTML and CSS only.

## Boundary
- `view-transitions-demo` — element continuity across page navigation
- `view-starting-style-demo` — enter and exit animation for elements toggling from hidden
- `view-scroll-driven-demo` — animations bound to scroll position
- `view-carousel-demo` — a carousel with CSS-generated buttons and markers

## Requirements
### R1: View transitions
- R1.1 — When the user navigates between the demo's pages, the BC shall carry a named element continuously across the transition.

### R2: Starting style
- R2.1 — When a hidden element is shown, the BC shall animate its entry from its declared starting style.
- R2.2 — When a shown element is dismissed, the BC shall animate its exit before it leaves the layout.

### R3: Scroll-driven animations
- R3.1 — While the user scrolls the page, the BC shall advance a progress indicator proportional to the scroll position.
- R3.2 — When a section enters the viewport, the BC shall animate it into view.

### R4: Carousel
- R4.1 — The BC shall page through the carousel's items with CSS-generated previous and next buttons.
- R4.2 — The BC shall mark the active carousel item with CSS-generated scroll markers.

## Out of scope
- Autoplay — the carousel moves only on user input.
- Scripted animation triggers.

# Overlays
> Demonstrate top-layer UI — popovers, modal dialogs, declarative invoker commands, and anchored tooltips — with HTML and CSS only.

## Boundary
- `view-popover-demo` — a menu, tooltip, and toast opened via the Popover API
- `view-dialog-demo` — a modal dialog with backdrop and focus containment
- `view-invoker-commands-demo` — buttons driving dialogs and popovers via declarative commands
- `view-anchor-positioning-demo` — tooltips positioned relative to their anchor in pure CSS

## Requirements
### R1: Popover
- R1.1 — When the user activates a popover trigger, the BC shall show the popover in the top layer.
- R1.2 — When the user presses Escape or clicks outside an open popover, the BC shall dismiss it.

### R2: Dialog
- R2.1 — When the user activates the open control, the BC shall show a modal dialog over a dimmed backdrop.
- R2.2 — While the modal dialog is open, the BC shall keep keyboard focus inside it.
- R2.3 — When the user presses Escape, the BC shall close the dialog.

### R3: Invoker commands
- R3.1 — When the user activates a button carrying a declarative command, the BC shall execute that command on the button's declared target.

### R4: Anchor positioning
- R4.1 — The BC shall position each tooltip relative to its anchor element with CSS anchor positioning.
- R4.2 — If the preferred position would overflow the viewport, then the BC shall flip the tooltip to a declared fallback position.

## Out of scope
- Hover-triggered interest invokers.
- Any scripted open, close, or positioning logic.

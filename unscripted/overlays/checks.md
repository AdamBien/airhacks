# Checks — overlays

- [R1.1] /overlays/popover.html at 1280px: menu links (Profile, Settings, Sign out) absent from snapshot; after clicking "Open menu" they are present
- [R1.2] /overlays/popover.html with the menu open: pressing Escape removes the menu links from the snapshot
- [R2.1] /overlays/dialog.html: after clicking "Open dialog", snapshot shows a modal dialog containing "Delete workspace?" and the computed `::backdrop` background is not transparent
- [R2.2] /overlays/dialog.html with the dialog open: repeated Tab keeps `document.activeElement` inside the dialog
- [R2.3] /overlays/dialog.html with the dialog open: pressing Escape removes the dialog content from the snapshot
- [R3.1] /overlays/invoker-commands.html: clicking the toggle-popover button shows the popover text; clicking show-modal opens the dialog; clicking close closes it — no page reload
- [R4.1] /overlays/anchor-positioning.html at 1280px: focusing "Hover or focus me" shows the tooltip with its rect adjacent to (above) the button's rect
- [R4.2] /overlays/anchor-positioning.html at 375px: focusing "Edge case →" shows the tooltip on the opposite inline side (tooltip rect left of the button rect)

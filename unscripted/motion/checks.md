# Checks — motion

- [R1.1] /motion/view-transitions.html and /motion/view-transitions-detail.html: the card element resolves computed `view-transition-name: vt-card` on both pages, and the first page's demo link navigates to the second
- [R2.1] /motion/starting-style.html: clicking "Toggle toast" makes the toast visible; the stylesheet declares a `@starting-style` block for `.toast:popover-open`
- [R2.2] /motion/starting-style.html: clicking "Toggle toast" again removes the toast; the `.toast` transition includes `display` with `allow-discrete`
- [R3.1] /motion/scroll-driven.html: computed `scale` of `.progress` is near 0 at scroll top and increases after scrolling to the bottom
- [R3.2] /motion/scroll-driven.html: a below-fold `.reveal` section has opacity < 1 while outside the viewport and opacity 1 after scrolling it into view
- [R4.1] /motion/carousel.html: the carousel exposes previous/next scroll buttons (snapshot shows buttons "Previous slide" / "Next slide"); clicking "Next slide" increases the carousel's scroll offset
- [R4.2] /motion/carousel.html: a marker group with 5 markers renders; at scroll offset 0 the first marker is `:target-current`, after clicking "Next slide" a later marker becomes current

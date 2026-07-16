# Checks — structure

- [R1.1] /structure/has.html: after clicking the "Pro" plan, its card's computed border-color switches to the accent (differs from the unselected cards)
- [R1.2] /structure/has.html on load: the hint "Nothing selected yet" is visible; after selecting any plan it is hidden
- [R2.1] /structure/container-queries.html at 375px: `.profile` computes `flex-direction: column`
- [R2.2] /structure/container-queries.html at 1280px: `.profile` computes `flex-direction: row`
- [R2.3] /structure/container-queries.html: `.resizable` computes `resize: horizontal`
- [R3.1] /structure/accordion.html: open item one, then item two — exactly one `details[open]` remains and it is item two
- [R3.2] /structure/accordion.html: the stylesheet transitions `::details-content` block-size with `interpolate-size: allow-keywords`, and opening an item reveals its answer text

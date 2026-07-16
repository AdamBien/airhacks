# Checks — system invariants

- [S1] every page: network requests include no `.js` files and the document contains no `script` element and no `on*` attribute; console shows no errors
- [S2] every page: a stylesheet declares `@view-transition { navigation: auto }` inside `@media (prefers-reduced-motion: no-preference)`
- [S3] every page: a stylesheet neutralizes `animation` and `transition` inside `@media (prefers-reduced-motion: reduce)`
- [S4] / at 1280px: `background-color` of `body` differs between `emulate colorScheme: dark` and `colorScheme: light`
- [S5] every feature page: snapshot contains text starting with "Baseline:"
- [S6] every feature page: snapshot contains a link "All demos" targeting the catalog

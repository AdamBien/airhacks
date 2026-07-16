# HTML and CSS Instead of JavaScript
> A static showcase where every page proves one HTML or CSS feature that replaces SPA JavaScript.

## Vision
- Show that the interactive feel of a single-page application no longer requires JavaScript.

## Components
- `home` links to every feature page of `overlays`, `forms`, `motion`, and `structure`.
- Every feature BC links back to `home`'s catalog.
- Every feature BC consumes the design tokens owned by `home`; no feature BC ships its own token set.
- Feature BCs never link each other.

## System invariants
- S1 — The system shall serve every page without JavaScript — no script elements, no inline handlers, no script files. _(why: the site's thesis; a single script tag anywhere falsifies it)_
- S2 — While the user has no reduced-motion preference, when the user navigates between pages, the system shall animate the transition with cross-document view transitions.
- S3 — While the user prefers reduced motion, the system shall neutralize animations and transitions.
- S4 — While the user prefers a light color scheme, the system shall render the light theme; while dark, the dark theme.
- S5 — The system shall state the demonstrated feature's Baseline status on every feature page. _(why: visitors must know what they can ship today)_
- S6 — The system shall provide on every feature page a link back to the catalog.

## Ubiquitous language
- Feature page — one page demonstrating exactly one HTML or CSS feature, stating what JavaScript it replaces and its Baseline status.
- Catalog — `home`'s index listing every feature page, grouped by business component.
- Baseline status — the webstatus.dev availability tier of a feature: widely, newly, or limited.

## Stack
- web-static + web-latest (Baseline gate replaced by a support-floor report) · site root = repo root · each BC's pages live in `<bc>/`, its spec in `<bc>/package-info.md`, its checks in `<bc>/checks.md`

# i18n

Cross-cutting label internationalization: resolves the UI language once at startup and exposes the matching message bundle.

## Design Decisions

- Locale bundles are plain JSON modules loaded with import attributes (`with { type: "json" }`) — no loader, no build step. Adding a language means adding one JSON file and one import line in the control.
- The language is resolved once at module load from `navigator.language`; unsupported languages fall back to English. A runtime language switch requires a reload.
- Boundary components import `messages` and interpolate — no key-lookup indirection, and `messages` is typed as the English bundle, so a key missing from a translation surfaces in the IDE.
- The locale bundles are pure data and live in the entity layer; the control carries the logic — resolution, selection, and fallback.

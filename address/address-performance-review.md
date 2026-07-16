# Performance Review: addresses app (http://localhost:3000)

Date: 2026-07-16 · Reviewed with chrome-devtools MCP against a plain `zws --single` instance (no `--live`).

**Lab caveat:** all numbers are lab data from one Chrome on one machine (macOS). No field/CrUX data exists for this app; nothing here is a claim about real users.

## Lab Conditions

- Chrome via DevTools MCP, viewport 375×667, **4× CPU throttle, Slow 4G** (primary profile)
- Secondary data point: 1280×800, unthrottled
- 3 throttled load traces; variance across runs < 2% on LCP, CLS identical in all runs — numbers are stable
- 1 interaction trace (focus + typing into the add-address form) for INP
- Heap probe: 6 route round trips (/ ↔ /add) + 12 theme toggles
- Lighthouse corroboration was not possible: this MCP version's `lighthouse_audit` excludes the performance category. The traces are the primary evidence throughout.

## Core Web Vitals (median of 3 throttled runs)

| Page | LCP | CLS | INP (interaction lab) | Verdict |
|------|-----|-----|------------------------|---------|
| /    | 1371 ms | **0.43** | 114 ms | LCP good · **CLS poor** · INP good |
| / (unthrottled desktop, single run) | 139 ms | 0.02 | – | reference only |

TTFB ≈ 1 ms (localhost — meaningless). No long tasks were flagged in any trace, even at 4× CPU with the WebGL backdrop running.

## Findings

| Priority | Issue | Evidence | Location |
|----------|-------|----------|----------|
| High | Layout shift of 0.43 when the SPA views replace the static placeholder | single shift at ~3578 ms, score 0.4318, identical in all 3 throttled runs; 0.02 unthrottled | `index.html` `.view` placeholder / first lit-html render |
| Medium | 4-level ES-module import chain delays interactivity | last module (`QueryControl.js`) starts at 2965 ms; levels at ~590 / 1180 / 1798 / 2965 ms, ≈600 ms Slow-4G round trip each | `app.js` import graph |
| Medium | Two render-blocking stylesheets | `tokens.css` + `style.css` block first paint; insight estimates FCP/LCP savings of ~1148 ms | `index.html` head |
| Low | Missing charset declaration | CharacterSet insight flagged in every trace | `index.html` |
| Low | Both locale files fetched | `en.json` + `de.json` (~2 KB combined) loaded regardless of active locale | `i18n/control/I18nControl.js` |

## Detailed Analysis

### 1. CLS 0.43 — late view render replaces placeholder (Priority: High)

**Evidence:** One layout-shift cluster (score 0.4318) at ~3578 ms in every throttled run — well after LCP (1371 ms) and exactly when the module chain finishes and the boundaries (`b-addresses`, `b-preview`) first render. The DevTools insight reports "no potential root causes" (no images/fonts involved); unthrottled, where modules arrive almost instantly, CLS drops to 0.02.

**Problem:** On a slow connection, users see the static page (headline, nav, `.view` tagline) for ~3.5 s, then the whole grid reflows when the form and preview mount — the classic mid-read jump. This is the only thing keeping the app out of "good" across the board.

**Recommendation:** Reserve the space before the views arrive — give the `.view` grid area a stable `min-block-size` that matches the mounted form, or render a skeleton with the final dimensions in the static HTML. Fixing belongs to the `web-components` stack; re-verify with its own loop afterwards.

### 2. Module waterfall: 4 sequential round trips (Priority: Medium)

**Evidence:** Total transfer is only ~70 KB over 22 requests, but the import graph is 4 levels deep: `app.js` (starts 590 ms) → `router.js`/`store.js`/`Addresses.js` (1180 ms) → `reduction.js`/`lit-html.js`/`BElement.js`/`Preview.js`/`List.js` (~1798 ms) → `QueryControl.js` (2965 ms). On Slow 4G each level costs ≈600 ms of latency; bytes are irrelevant.

**Problem:** The app is code-light but latency-deep — interactivity (and the CLS fix above notwithstanding, the visible content swap) lands ~2 s later than the payload justifies.

**Recommendation:** Add `<link rel="modulepreload" href="...">` hints in `index.html` for the deep dependencies (`lit-html.js`, `reduction.js`, `BElement.js`, the boundary/control/entity modules). This flattens the chain to ~2 round trips while staying fully buildless — no bundler required.

### 3. Render-blocking CSS: two files where one would do (Priority: Medium)

**Evidence:** `tokens.css` (2.2 KB) and `style.css` (10 KB) are both render-blocking; download completes ~1.28 s; the RenderBlocking insight estimates ~1148 ms FCP/LCP savings if moved out of the critical path. LCP is 99% render delay (1370 of 1371 ms).

**Problem:** Two VeryHigh-priority requests must round-trip before anything paints. LCP is still "good" (1371 ms), so this is an optimization, not an emergency.

**Recommendation:** The cheapest honest win is merging tokens+style into one stylesheet request, or inlining both (12 KB total) into `index.html` for a zero-request critical path. Do not switch to `@import` — that serializes the two requests and makes it worse.

### 4. Missing charset declaration (Priority: Low)

**Evidence:** CharacterSet insight in every trace: no `<meta charset>` in the first 1024 bytes and no charset in the `Content-Type` header.

**Recommendation:** Add `<meta charset="utf-8">` as the first element of `<head>`. Also a correctness issue (encoding sniffing), not just a performance one.

### 5. Both locales fetched (Priority: Low)

**Evidence:** `en.json` (979 B) and `de.json` (1 KB) both load on every start.

**Recommendation:** Fetch only the active locale. At this size it is cosmetic; noted for completeness, not urgency.

### Non-findings (verified clean)

- **INP / main thread:** 114 ms INP for form interaction at 4× CPU; no long tasks in any trace. The redux-style store + lit-html re-render path is comfortably responsive.
- **WebGL backdrop:** contributes no main-thread long tasks; GPU cost is continuous by design but bounded (DPR cap 1.25, dark-theme only, loop fully cancelled in light theme and static under `prefers-reduced-motion`).
- **Memory:** heap after 6 route round trips + 12 theme toggles: −236 KB (GC noise around a ~2 MB heap) — no leak signal, no stacked animation loops.
- **Page weight:** ~70 KB total, no images, no webfonts. Nothing to compress away.
- **Serving artifacts:** `no-cache` headers and HTTP/1.1 come from the zws dev server; production hosting (HTTP/2, caching) changes the waterfall shape — the module-chain depth finding still stands, since chained discovery defeats parallelism regardless of protocol.

## Summary

- **Fast where it counts:** LCP 1371 ms (good) and INP 114 ms (good) on a throttled mid-range mobile profile; ~70 KB total, no leaks, clean main thread.
- **High:** CLS 0.43 (poor) — the SPA's late view mount shifts the whole layout at ~3.6 s on slow networks; reserve the view area's space.
- **Medium:** 4-deep module import chain (last module at ~3 s) — add `modulepreload` hints; merge or inline the two render-blocking stylesheets (~1.1 s estimated paint savings).
- **Low:** add `<meta charset="utf-8">`; load only the active locale.

# address

Address management application: create, list, edit, and delete postal addresses in the browser.

Built as a single-page application with native [Web Components](https://developer.mozilla.org/en-US/docs/Web/API/Web_components), [lit-html](https://lit.dev/docs/libraries/standalone-templates/) templating, and Redux-style unidirectional state management — no build system, no framework. The architecture follows the Boundary Control Entity (BCE) pattern and is derived from the [bce.design](bce.design/) quickstarter.

## Prerequisites

A static web server with SPA fallback to `index.html`, e.g. [zws](https://github.com/adamBien/zws) (requires Java).

## Run

There is nothing to build. Serve the application sources:

```bash
cd app/src
zws.sh --single
```

Open `http://localhost:4000` in a browser.

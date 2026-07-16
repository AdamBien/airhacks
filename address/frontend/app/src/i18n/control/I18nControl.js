/**
 * Cross-cutting label internationalization. Locale bundles are plain
 * JSON modules loaded with import attributes — no loader, no build step,
 * adding a language means adding one JSON file and one import line.
 * The bundle is chosen once at startup from the browser language;
 * unsupported languages fall back to English.
 * @see {@link https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/import/with|MDN: import attributes}
 */
import en from "../entity/en.json" with { type: "json" };
import de from "../entity/de.json" with { type: "json" };

const bundles = { en, de };

const requested = (navigator.language ?? "en").split("-")[0];

/** @type {string} the resolved language — the requested one, or "en" when unsupported */
export const language = requested in bundles ? requested : "en";

/** @type {typeof en} the active message bundle */
export const messages = bundles[language];

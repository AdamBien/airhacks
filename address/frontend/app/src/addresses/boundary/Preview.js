import BElement from "../../BElement.js";
import { html } from "lit-html";
import { messages } from "../../i18n/control/I18nControl.js";

/**
 * Live preview of the address draft, rendered as a postal address block.
 * Reflects every keystroke — the draft flows from the form through the
 * store back into this component.
 */
class Preview extends BElement {

    /**
     * @param {{address: import('../entity/AddressesReducer.js').AddressState}} state
     * @returns {Partial<import('../entity/AddressesReducer.js').Address>} the draft under input
     */
    extractState({ addresses: { draft } }) {
        return draft;
    }

    view() {
        const { name, street, postalCode, city, country } = this.state;
        const lines = [
            name,
            street,
            [postalCode, city].filter(Boolean).join(' '),
            country
        ].filter(Boolean);
        return html`
        <address>
            ${lines.length
                ? lines.map(line => html`<div>${line}</div>`)
                : html`<p class="hint">${messages.previewHint}</p>`}
        </address>
        `;
    }
}
customElements.define('b-addresses-preview', Preview);

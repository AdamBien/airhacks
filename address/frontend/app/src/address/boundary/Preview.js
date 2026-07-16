import BElement from "../../BElement.js";
import { html } from "lit-html";

/**
 * Live preview of the address draft, rendered as a postal address block.
 * Reflects every keystroke — the draft flows from the form through the
 * store back into this component.
 */
class Preview extends BElement {

    /**
     * @param {{address: import('../entity/AddressReducer.js').AddressState}} state
     * @returns {Partial<import('../entity/AddressReducer.js').Address>} the draft under input
     */
    extractState({ address: { draft } }) {
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
            ${lines.map(line => html`<div>${line}</div>`)}
        </address>
        `;
    }
}
customElements.define('b-address-preview', Preview);

import BElement from "../../BElement.js";
import { html } from "lit-html";
import { quotePrice } from "../control/Renting.js";

/**
 * Boundary op `quote-price`: the total for the chosen type and days, shown before renting.
 */
class Quote extends BElement {

    extractState({ rental: { request: { type, days } } }) {
        return { type, days };
    }

    view() {
        const { type, days } = this.state;
        return html`
            <label>total price
                <output name="price">€${quotePrice(type, days)}</output>
            </label>
        `;
    }
}
customElements.define('b-rental-quote', Quote);

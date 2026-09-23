import BElement from "../../BElement.js";
import { html } from "lit-html";
import { returnBike } from "../control/Renting.js";

/**
 * Boundary op `list-rentals`: every rental, open and returned, newest first.
 * Boundary op `return-bike`: the return button of an open rental; a returned
 * rental offers none.
 */
class Rentals extends BElement {

    extractState({ rental: { rentals } }) {
        return rentals;
    }

    /**
     * @param {number} rentalId
     */
    returnBike(rentalId) {
        returnBike(rentalId);
    }

    view() {
        if (this.state.length === 0) {
            return html`<p>no rentals yet</p>`;
        }
        return html`
            <h3>my rentals</h3>
            <ol aria-label="my rentals">
                ${this.state.map(({ id, bikeId, type, days, price, status }) => html`
                    <li>
                        <span>${bikeId} · ${type} · ${days} ${days === 1 ? "day" : "days"} · €${price} · ${status}</span>
                        ${status === "open"
                            ? html`<button @click=${_ => this.returnBike(id)}>return ${bikeId}</button>`
                            : ""}
                    </li>
                `)}
            </ol>
        `;
    }
}
customElements.define('b-rental-list', Rentals);

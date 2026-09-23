import BElement from "../../BElement.js";
import { html } from "lit-html";
import { availability } from "../control/Renting.js";

/**
 * Boundary op `show-availability`: how many bikes of each type can be rented right now.
 */
class Availability extends BElement {

    extractState({ rental: { fleet } }) {
        return availability(fleet);
    }

    view() {
        return html`
            <h3>available right now</h3>
            <ul>
                ${this.state.map(({ type, available }) => html`
                    <li>${type}: ${available > 0 ? `${available} available` : "sold out"}</li>
                `)}
            </ul>
        `;
    }
}
customElements.define('b-rental-availability', Availability);

import BElement from "../../BElement.js";
import { html } from "lit-html";
import { requestUpdated, startRental, BIKE_TYPES, MIN_DAYS, MAX_DAYS } from "../control/Renting.js";
import './Quote.js';

/**
 * Boundary op `start-rental`: hand an available bike of the chosen type to a
 * customer for the chosen days at the quoted price. Native constraint
 * validation rejects a missing name or email and days outside 1 to 14; the
 * reducer rejects a sold-out type and reports it in the notice.
 */
class Start extends BElement {

    extractState({ rental: { request, notice } }) {
        return { request, notice };
    }

    /**
     * @param {SubmitEvent} event
     */
    submit(event) {
        event.preventDefault();
        const form = /** @type {HTMLFormElement} */ (event.target);
        if (!form.reportValidity()) return;
        startRental();
    }

    view() {
        const { request: { type, days, name, email }, notice } = this.state;
        const update = ({ target: { name, value } }) => requestUpdated(name, value);
        return html`
            <h3>rent now</h3>
            <form @submit=${event => this.submit(event)}>
                <label>type
                    <select name="type" .value=${type} @change=${update}>
                        ${BIKE_TYPES.map(option => html`
                            <option value=${option} ?selected=${option === type}>${option}</option>
                        `)}
                    </select>
                </label>
                <label>days
                    <input name="days" type="number" required min=${MIN_DAYS} max=${MAX_DAYS} step="1"
                           .value=${String(days)} @input=${update}>
                </label>
                <label>name
                    <input name="name" type="text" required autocomplete="name"
                           .value=${name} @input=${update}>
                </label>
                <label>email
                    <input name="email" type="email" required autocomplete="email"
                           .value=${email} @input=${update}>
                </label>
                <b-rental-quote></b-rental-quote>
                <button type="submit">start rental</button>
                ${notice ? html`<p role="alert">${notice}</p>` : ""}
            </form>
        `;
    }
}
customElements.define('b-rental-start', Start);

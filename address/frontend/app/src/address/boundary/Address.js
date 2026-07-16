import BElement from "../../BElement.js";
import { html } from "lit-html";
import { addressUpdated, saveAddress } from "../control/CRUDControl.js";
import './Preview.js';

/**
 * Postal address input form — the address module's primary entry point.
 * Binds each field to the draft in the address state slice; native
 * constraint validation gates the save.
 */
class Address extends BElement {

    /**
     * @param {{address: import('../entity/AddressReducer.js').AddressState}} state
     * @returns {Partial<import('../entity/AddressReducer.js').Address>} the draft under input
     */
    extractState({ address: { draft } }) {
        return draft;
    }

    view() {
        return html`
        <b-address-preview></b-address-preview>
        <form>
            <label>Name:
                <input required name="name" autocomplete="name" placeholder="name" .value="${this.state.name ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <label>Street:
                <input required name="street" autocomplete="street-address" placeholder="street and number" .value="${this.state.street ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <label>Postal Code:
                <input required name="postalCode" autocomplete="postal-code" placeholder="postal code" .value="${this.state.postalCode ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <label>City:
                <input required name="city" autocomplete="address-level2" placeholder="city" .value="${this.state.city ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <label>Country:
                <input required name="country" autocomplete="country-name" placeholder="country" .value="${this.state.country ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <button @click="${e => this.onSave(e)}">save address</button>
        </form>
        `;
    }

    /**
     * @param {{target: {name: string, value: string}}} event input event
     */
    onUserInput({ target: { name, value } }) {
        addressUpdated(name, value);
    }

    /**
     * Validates the form with native constraint validation and commits
     * the draft on success.
     * @param {Event & {target: {form: HTMLFormElement}}} event click event
     */
    onSave(event) {
        const { target: { form } } = event;
        event.preventDefault();
        form.reportValidity();
        if (form.checkValidity()) {
            saveAddress();
            form.reset();
        }
    }
}

customElements.define('b-address', Address);

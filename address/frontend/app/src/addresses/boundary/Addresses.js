import BElement from "../../BElement.js";
import { html } from "lit-html";
import { addressUpdated, saveAddress } from "../control/CRUDControl.js";
import { messages } from "../../i18n/control/I18nControl.js";
import './Preview.js';
import './List.js';

/**
 * Postal address input form — the address module's primary entry point.
 * Binds each field to the draft in the address state slice; native
 * constraint validation gates the save.
 */
class Addresses extends BElement {

    /**
     * @param {{address: import('../entity/AddressesReducer.js').AddressState}} state
     * @returns {Partial<import('../entity/AddressesReducer.js').Address>} the draft under input
     */
    extractState({ addresses: { draft } }) {
        return draft;
    }

    view() {
        return html`
        <b-addresses-preview></b-addresses-preview>
        <form>
            <label>${messages.labels.name}:
                <input required name="name" autocomplete="name" placeholder="${messages.placeholders.name}" .value="${this.state.name ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <label>${messages.labels.street}:
                <input required name="street" autocomplete="street-address" placeholder="${messages.placeholders.street}" .value="${this.state.street ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <label>${messages.labels.postalCode}:
                <input required name="postalCode" autocomplete="postal-code" placeholder="${messages.placeholders.postalCode}" .value="${this.state.postalCode ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <label>${messages.labels.city}:
                <input required name="city" autocomplete="address-level2" placeholder="${messages.placeholders.city}" .value="${this.state.city ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <label>${messages.labels.country}:
                <input required name="country" autocomplete="country-name" placeholder="${messages.placeholders.country}" .value="${this.state.country ?? ''}" @input=${e => this.onUserInput(e)}>
            </label>
            <button @click="${e => this.onSave(e)}">${messages.saveAddress}</button>
        </form>
        <b-addresses-list></b-addresses-list>
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
     * the draft on success. A failed attempt shakes the invalid fields.
     * @param {Event & {target: {form: HTMLFormElement}}} event click event
     */
    onSave(event) {
        const { target: { form } } = event;
        event.preventDefault();
        form.reportValidity();
        if (form.checkValidity()) {
            saveAddress();
            form.reset();
        } else {
            this.shakeInvalid(form);
        }
    }

    /**
     * Replays the shake animation on every invalid field. Like focus
     * handling, a browser-only concern — no state involved.
     * @param {HTMLFormElement} form
     */
    shakeInvalid(form) {
        form.querySelectorAll('input:invalid').forEach(input => {
            input.classList.add('shake');
            input.addEventListener('animationend', _ => input.classList.remove('shake'), { once: true });
        });
    }
}

customElements.define('b-addresses', Addresses);

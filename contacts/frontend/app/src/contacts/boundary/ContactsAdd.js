import BElement from "../../BElement.js";
import { html } from "lit-html";
import { contactUpdated, editContact, saveContact } from "../control/ContactsControl.js";

class ContactsAdd extends BElement {

    /**
     * The router passes the :id route parameter as an attribute. Loading (or
     * resetting) the temporal cache happens before the initial render
     * triggered by BElement's connectedCallback.
     */
    connectedCallback() {
        editContact(this.getAttribute('id'));
        super.connectedCallback();
    }

    extractState({ contacts: { contact } }) {
        return contact;
    }

    view() {
        return html`
        <form>
            <label>First name
                <input name="firstName" placeholder="first name"
                    .value="${this.state.firstName ?? ''}" @input="${event => this.onUserInput(event)}">
            </label>
            <label>Last name
                <input required name="lastName" placeholder="last name"
                    .value="${this.state.lastName ?? ''}" @input="${event => this.onUserInput(event)}">
            </label>
            <label>Email
                <input type="email" name="email" placeholder="duke@java.net"
                    .value="${this.state.email ?? ''}" @input="${event => this.onUserInput(event)}">
            </label>
            <label>Phone
                <input type="tel" name="phone" placeholder="+1 555 0100"
                    .value="${this.state.phone ?? ''}" @input="${event => this.onUserInput(event)}">
            </label>
            <label>Type
                <select name="type" .value="${this.state.type ?? 'private'}"
                    @input="${event => this.onUserInput(event)}">
                    <option value="private">private</option>
                    <option value="business">business</option>
                </select>
            </label>
            <button @click="${event => this.saveContact(event)}">${this.state.id ? "save contact" : "new contact"}</button>
        </form>
        `;
    }

    /**
     * @param {{target: {name: string, value: string}}} event
     */
    onUserInput({ target: { name, value } }) {
        contactUpdated(name, value);
    }

    /**
     * Native constraint validation gates the save: a missing last name or a
     * malformed email blocks the submission and surfaces the invalid field.
     * @param {Event & {target: {form: HTMLFormElement}}} event
     */
    saveContact(event) {
        const { target: { form } } = event;
        event.preventDefault();
        form.reportValidity();
        if (form.checkValidity()) {
            saveContact();
        }
    }
}
customElements.define('b-contacts-add', ContactsAdd);

import BElement from "../../BElement.js";
import { html } from "lit-html";
import { deleteContact, loadContacts, searchTermChanged, sortSelected } from "../control/ContactsControl.js";

/** @typedef {import("../entity/ContactsReducer.js").Contact} Contact */

class Contacts extends BElement {

    connectedCallback() {
        loadContacts();
        super.connectedCallback();
    }

    /**
     * Selects the visible slice: the list filtered by the search term
     * (first name, last name, email — case-insensitive) and ordered by the
     * selected sort field and direction.
     * @param {{contacts: import("../entity/ContactsReducer.js").ContactsState}} state
     * @returns {{list: Contact[], searchTerm: string}}
     */
    extractState({ contacts: { list, searchTerm, sort } }) {
        const term = searchTerm.toLowerCase();
        const matching = list.filter(({ firstName, lastName, email }) =>
            [firstName, lastName, email].some(field => (field ?? "").toLowerCase().includes(term)));
        const ordered = matching.toSorted((left, right) =>
            (left[sort.field] ?? "").localeCompare(right[sort.field] ?? ""));
        if (!sort.ascending) {
            ordered.reverse();
        }
        return { list: ordered, searchTerm };
    }

    view() {
        return html`
        <search>
            <label>search
                <input type="search" name="search" placeholder="name or email"
                    .value="${this.state.searchTerm}"
                    @input="${({ target: { value } }) => searchTermChanged(value)}">
            </label>
        </search>
        <table>
            <thead>
                <tr>
                    <th><button @click="${_ => sortSelected('lastName')}">last name</button></th>
                    <th><button @click="${_ => sortSelected('firstName')}">first name</button></th>
                    <th><button @click="${_ => sortSelected('email')}">email</button></th>
                    <th>phone</th>
                    <th><button @click="${_ => sortSelected('type')}">type</button></th>
                    <th>actions</th>
                </tr>
            </thead>
            <tbody>
                ${this.state.list.map(contact => html`
                    <tr>
                        <td>${contact.lastName}</td>
                        <td>${contact.firstName}</td>
                        <td>${contact.email}</td>
                        <td>${contact.phone}</td>
                        <td>${contact.type}</td>
                        <td>
                            <a href="/edit/${contact.id}">edit</a>
                            <button @click="${_ => deleteContact(contact.id)}">delete</button>
                        </td>
                    </tr>
                `)}
            </tbody>
        </table>
        `;
    }
}
customElements.define('b-contacts', Contacts);

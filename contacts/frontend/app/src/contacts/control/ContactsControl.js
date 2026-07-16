import { createAction } from "@reduxjs/toolkit";
import store from "../../store.js";
import { contactsServiceUri } from "../../app.config.js";

export const contactsLoadedAction = createAction("contactsLoadedAction");
export const contactUpdatedAction = createAction("contactUpdatedAction");
export const editContactAction = createAction("editContactAction");
export const contactSavedAction = createAction("contactSavedAction");
export const contactDeletedAction = createAction("contactDeletedAction");
export const searchTermChangedAction = createAction("searchTermChangedAction");
export const sortSelectedAction = createAction("sortSelectedAction");

/**
 * Replaces the list with all contacts provided by the contacts service.
 * @returns {Promise<void>}
 */
export const loadContacts = async _ => {
    try {
        const response = await fetch(contactsServiceUri);
        if (!response.ok) {
            console.error("loading contacts failed:", response.status);
            return;
        }
        store.dispatch(contactsLoadedAction(await response.json()));
    } catch (error) {
        console.error("loading contacts failed:", error);
    }
}

/**
 * @param {string} name form field name
 * @param {string} value user input
 */
export const contactUpdated = (name, value) => {
    store.dispatch(contactUpdatedAction({ name, value }));
}

/**
 * Loads the contact with the given id from the contacts service into the
 * temporal cache for editing. A missing id resets the cache to a fresh
 * contact preselecting the `private` type — the type is mandatory, so every
 * submission carries a valid one without an extra click.
 * @param {string|null} id contact id from the route parameter
 * @returns {Promise<void>}
 */
export const editContact = async id => {
    if (!id) {
        store.dispatch(editContactAction({ type: "private" }));
        return;
    }
    try {
        const response = await fetch(`${contactsServiceUri}/${id}`);
        const contact = response.ok ? await response.json() : {};
        store.dispatch(editContactAction(contact));
    } catch (error) {
        console.error("loading contact for edit failed:", error);
        store.dispatch(editContactAction({}));
    }
}

/**
 * Sends the cached contact to the contacts service — an update for a cached
 * id, a creation otherwise — and mirrors the confirmed result into the list.
 * @returns {Promise<void>}
 */
export const saveContact = async _ => {
    const { contacts: { contact } } = store.getState();
    const update = Boolean(contact.id);
    const uri = update ? `${contactsServiceUri}/${contact.id}` : contactsServiceUri;
    try {
        const response = await fetch(uri, {
            method: update ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(contact)
        });
        if (!response.ok) {
            console.error("saving contact failed:", response.status);
            return;
        }
        store.dispatch(contactSavedAction(await response.json()));
    } catch (error) {
        console.error("saving contact failed:", error);
    }
}

/**
 * @param {string} id contact id
 * @returns {Promise<void>}
 */
export const deleteContact = async id => {
    try {
        const response = await fetch(`${contactsServiceUri}/${id}`, { method: "DELETE" });
        if (!response.ok) {
            console.error("deleting contact failed:", response.status);
            return;
        }
        store.dispatch(contactDeletedAction(id));
    } catch (error) {
        console.error("deleting contact failed:", error);
    }
}

/**
 * @param {string} term search input, empty string clears the filter
 */
export const searchTermChanged = term => {
    store.dispatch(searchTermChangedAction(term));
}

/**
 * Orders the listing by the given field; selecting the current field again
 * reverses the order.
 * @param {string} field a contact field name
 */
export const sortSelected = field => {
    store.dispatch(sortSelectedAction(field));
}

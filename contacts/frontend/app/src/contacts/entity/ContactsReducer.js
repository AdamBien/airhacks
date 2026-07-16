import { createReducer } from "@reduxjs/toolkit";
import {
    contactDeletedAction,
    contactSavedAction,
    contactUpdatedAction,
    contactsLoadedAction,
    editContactAction,
    searchTermChangedAction,
    sortSelectedAction
} from "../control/ContactsControl.js";

/**
 * @typedef {object} Contact
 * @property {string} id service-assigned identity
 * @property {string} firstName
 * @property {string} lastName mandatory
 * @property {string} email
 * @property {string} phone
 * @property {"business"|"private"} type mandatory, closed value set
 */

/**
 * @typedef {object} ContactsState
 * @property {Contact[]} list contacts as provided by the contacts service
 * @property {Partial<Contact>} contact temporal cache for the add/edit form
 * @property {string} searchTerm
 * @property {{field: string, ascending: boolean}} sort
 */

/** @type {ContactsState} */
const initialState = {
    list: [],
    contact: {},
    searchTerm: "",
    sort: { field: "lastName", ascending: true }
}

/**
 * Maintains the contacts slice. The service is the source of truth: the list
 * is replaced on every load, and saves/deletes only mirror already-confirmed
 * service responses. The temporal cache holds form input until saved.
 */
export const contacts = createReducer(initialState, (builder) => {
    builder.addCase(contactsLoadedAction, (state, { payload }) => {
        state.list = payload;
    }).addCase(contactUpdatedAction, (state, { payload: { name, value } }) => {
        state.contact[name] = value;
    }).addCase(editContactAction, (state, { payload }) => {
        state.contact = payload;
    }).addCase(contactSavedAction, (state, { payload }) => {
        const exists = state.list.some(({ id }) => id === payload.id);
        state.list = exists
            ? state.list.map(contact => contact.id === payload.id ? payload : contact)
            : state.list.concat(payload);
        state.contact = {};
    }).addCase(contactDeletedAction, (state, { payload }) => {
        state.list = state.list.filter(({ id }) => id !== payload);
    }).addCase(searchTermChangedAction, (state, { payload }) => {
        state.searchTerm = payload;
    }).addCase(sortSelectedAction, (state, { payload }) => {
        state.sort = state.sort.field === payload
            ? { field: payload, ascending: !state.sort.ascending }
            : { field: payload, ascending: true };
    });
});

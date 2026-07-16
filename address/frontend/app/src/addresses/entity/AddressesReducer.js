import { createReducer } from "@reduxjs/toolkit"
import {
    addressUpdatedAction,
    addressesSortedAction,
    cellEditedAction,
    cellUpdatedAction,
    editCancelledAction,
    pageSelectedAction,
    saveAddressAction
} from "../control/CRUDControl.js";

/** @type {number} addresses shown per table page */
export const PAGE_SIZE = 5;

/**
 * @typedef {Object} Address
 * @property {number} id assigned on save
 * @property {string} name full name of the addressee
 * @property {string} street street and house number
 * @property {string} postalCode postal / ZIP code
 * @property {string} city city or locality
 * @property {string} country country name
 */

/**
 * @typedef {Object} Sort
 * @property {?string} by Address property to sort the list by, null for insertion order
 * @property {boolean} ascending sort direction
 */

/**
 * @typedef {Object} Edit
 * @property {?number} id address whose cell is under edit, null for none
 * @property {?string} field Address property under edit
 */

/**
 * @typedef {Object} AddressState
 * @property {Address[]} list saved addresses
 * @property {Partial<Address>} draft temporal cache for form input
 * @property {Sort} sort list sort criteria
 * @property {Edit} edit cell under inline edit
 * @property {number} page current table page, zero-based
 */

/** @type {AddressState} */
const initialState = {
    list: [],
    draft: {},
    sort: { by: null, ascending: true },
    edit: { id: null, field: null },
    page: 0
}

/**
 * Redux reducer managing address state transitions.
 *
 * Form input flows field by field into the draft — a temporal cache that
 * exists independently of the list. Saving stamps the draft with an id,
 * appends it to the list, and resets the draft, which clears the form.
 */
export const addresses = createReducer(initialState, (builder) => {
    builder.addCase(addressUpdatedAction, (state, { payload: { name, value } }) => {
        state.draft[name] = value;
    }).addCase(addressesSortedAction, (state, { payload }) => {
        state.sort = {
            by: payload,
            ascending: state.sort?.by === payload ? !state.sort.ascending : true
        };
    }).addCase(cellEditedAction, (state, { payload: { id, field } }) => {
        state.edit = { id, field };
    }).addCase(cellUpdatedAction, (state, { payload: { id, field, value } }) => {
        state.list = state.list.map(address =>
            address.id === id ? { ...address, [field]: value } : address);
        state.edit = { id: null, field: null };
    }).addCase(editCancelledAction, (state) => {
        state.edit = { id: null, field: null };
    }).addCase(pageSelectedAction, (state, { payload }) => {
        const lastPage = Math.max(0, Math.ceil(state.list.length / PAGE_SIZE) - 1);
        state.page = Math.min(Math.max(0, payload), lastPage);
    }).addCase(saveAddressAction, (state, { payload }) => {
        state.draft.id = payload;
        state.list = state.list.concat(state.draft);
        state.draft = {};
    });
})

import { createReducer } from "@reduxjs/toolkit"
import { addressUpdatedAction, saveAddressAction } from "../control/CRUDControl.js";

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
 * @typedef {Object} AddressState
 * @property {Address[]} list saved addresses
 * @property {Partial<Address>} draft temporal cache for form input
 */

/** @type {AddressState} */
const initialState = {
    list: [],
    draft: {}
}

/**
 * Redux reducer managing address state transitions.
 *
 * Form input flows field by field into the draft — a temporal cache that
 * exists independently of the list. Saving stamps the draft with an id,
 * appends it to the list, and resets the draft, which clears the form.
 */
export const address = createReducer(initialState, (builder) => {
    builder.addCase(addressUpdatedAction, (state, { payload: { name, value } }) => {
        state.draft[name] = value;
    }).addCase(saveAddressAction, (state, { payload }) => {
        state.draft.id = payload;
        state.list = state.list.concat(state.draft);
        state.draft = {};
    });
})

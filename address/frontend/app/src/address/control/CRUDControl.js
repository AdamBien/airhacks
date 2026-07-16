import { createAction } from "@reduxjs/toolkit";
import store from "../../store.js";

export const addressUpdatedAction = createAction("addressUpdatedAction");

/**
 * Writes a single form field into the address draft.
 * @param {string} name field name, matches an Address property
 * @param {string} value user-entered value
 */
export const addressUpdated = (name, value) => {
    store.dispatch(addressUpdatedAction({ name, value }));
}

export const saveAddressAction = createAction("saveAddressAction");

/**
 * Commits the draft to the address list. The dispatch timestamp
 * becomes the entity id.
 */
export const saveAddress = _ => {
    store.dispatch(saveAddressAction(Date.now()));
}

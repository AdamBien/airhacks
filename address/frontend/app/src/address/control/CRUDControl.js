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

export const addressesSortedAction = createAction("addressesSortedAction");

/**
 * Sorts the address list by the given column. Sorting the same
 * column again reverses the direction.
 * @param {string} by an Address property name
 */
export const addressesSorted = (by) => {
    store.dispatch(addressesSortedAction(by));
}

export const cellEditedAction = createAction("cellEditedAction");

/**
 * Marks a single cell of a saved address as under edit.
 * @param {number} id the address id
 * @param {string} field an Address property name
 */
export const editCell = (id, field) => {
    store.dispatch(cellEditedAction({ id, field }));
}

export const cellUpdatedAction = createAction("cellUpdatedAction");

/**
 * Writes the edited value into the saved address and ends the edit.
 * @param {number} id the address id
 * @param {string} field an Address property name
 * @param {string} value the new value
 */
export const updateCell = (id, field, value) => {
    store.dispatch(cellUpdatedAction({ id, field, value }));
}

export const editCancelledAction = createAction("editCancelledAction");

/**
 * Ends the cell edit without changing the address.
 */
export const cancelEdit = _ => {
    store.dispatch(editCancelledAction());
}

export const saveAddressAction = createAction("saveAddressAction");

/**
 * Commits the draft to the address list. The dispatch timestamp
 * becomes the entity id.
 */
export const saveAddress = _ => {
    store.dispatch(saveAddressAction(Date.now()));
}

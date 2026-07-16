/**
 * Redux store configuration with automatic localStorage persistence.
 * The entire application state is persisted to localStorage on every update,
 * enabling state recovery across browser sessions. The contacts service stays
 * the source of truth — the persisted list is only a display cache, replaced
 * on every load.
 * @see {@link https://developer.mozilla.org/en-US/docs/Web/API/Window/localStorage}
 */
import { configureStore } from "@reduxjs/toolkit";
import { load } from "./localstorage/control/StorageControl.js";
import { contacts } from "./contacts/entity/ContactsReducer.js"

const reducer = {
    contacts
}
const preloadedState = load();
const config = preloadedState ? { reducer, preloadedState } : {reducer};
const store = configureStore(config);
export default store;

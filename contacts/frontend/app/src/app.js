/**
 * Application entry point that initializes routing and state persistence.
 * To deactivate localStorage persistence, comment out or remove the store.subscribe() block.
 */
import { initRouter } from "./router.js";
import './contacts/boundary/Contacts.js';
import './contacts/boundary/ContactsAdd.js';
import store from "./store.js";
import { save } from "./localstorage/control/StorageControl.js";

/**
 * To deactivate localStorage persistence, comment out or remove the store.subscribe() block below.
 */
store.subscribe(_ => {
    const state = store.getState();
    save(state);
})
initRouter(document.querySelector('.view'), [
    { path: '/',         component: 'b-contacts' },
    { path: '/add',      component: 'b-contacts-add' },
    { path: '/edit/:id', component: 'b-contacts-add' }
]);
console.log("router initialized");

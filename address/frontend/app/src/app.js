/**
 * Application entry point that initializes routing and state persistence.
 * To deactivate localStorage persistence, comment out or remove the store.subscribe() block.
 */
import { initRouter } from "./router.js";
import './address/boundary/Address.js';
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
    { path: '/',    component: 'b-address' },
    { path: '/add', component: 'b-address' }
]);
console.log("router initialized");

/**
 * Application entry point that initializes routing and state persistence.
 * To deactivate localStorage persistence, comment out or remove the store.subscribe() block.
 */
import { initRouter } from "./router.js";
import './address/boundary/Address.js';
import store from "./store.js";
import { save } from "./localstorage/control/StorageControl.js";
import { language, messages } from "./i18n/control/I18nControl.js";

/**
 * The static shell (title, heading, navigation) is plain HTML —
 * localized once at startup, before the router renders the first view.
 */
document.documentElement.lang = language;
document.title = messages.title;
document.querySelector('header h1').textContent = messages.title;
document.querySelector('nav a[href="/add"]').textContent = messages.addAddress;

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

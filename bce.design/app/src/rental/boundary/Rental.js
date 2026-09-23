import BElement from "../../BElement.js";
import { html } from "lit-html";
import './Availability.js';
import './Start.js';

/**
 * The module's routing entry point: availability next to the start form.
 */
class Rental extends BElement {

    view() {
        return html`
            <b-rental-availability></b-rental-availability>
            <b-rental-start></b-rental-start>
        `;
    }
}
customElements.define('b-rental', Rental);

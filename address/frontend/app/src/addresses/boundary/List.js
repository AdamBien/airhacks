import BElement from "../../BElement.js";
import { html } from "lit-html";
import { addressesSorted, cancelEdit, editCell, pageSelected, updateCell } from "../control/CRUDControl.js";
import { clampedPage, pageCount, pageOf, sorted } from "../control/QueryControl.js";
import { messages } from "../../i18n/control/I18nControl.js";

/** @type {Array<{key: string, label: string}>} table columns, one per Address property */
const COLUMNS = ['name', 'street', 'postalCode', 'city', 'country']
    .map(key => ({ key, label: messages.labels[key] }));

/**
 * All saved addresses as a table, updated on every store change.
 * Column headers sort the rows; sorting the same column again
 * reverses the direction. A cell click turns the cell into an input —
 * Enter or leaving the field commits, Escape cancels.
 */
class List extends BElement {

    /**
     * Selects the table's state slices; the display derivation
     * (sorting, paging) is delegated to QueryControl.
     * @param {{address: import('../entity/AddressesReducer.js').AddressState}} state
     * @returns {import('../entity/AddressesReducer.js').Address[]} the addresses, sorted for display
     */
    extractState({ addresses: { list, sort, edit, page } }) {
        this.sort = sort ?? { by: null, ascending: true };
        this.edit = edit ?? { id: null, field: null };
        this.page = page ?? 0;
        return sorted(list, this.sort);
    }

    view() {
        if (!this.state.length) return html``;
        const pages = pageCount(this.state);
        const page = clampedPage(this.state, this.page);
        const rows = pageOf(this.state, page);
        return html`
        <table>
            <caption>${messages.savedAddresses}</caption>
            <thead>
                <tr>
                    ${COLUMNS.map(column => this.headerView(column))}
                </tr>
            </thead>
            <tbody>
                ${rows.map(address => html`
                <tr>
                    ${COLUMNS.map(({ key }) => this.cellView(address, key))}
                </tr>
                `)}
            </tbody>
        </table>
        ${this.paginationView(page, pages)}
        `;
    }

    /**
     * Previous/next pager with the current position — rendered only
     * when the list spans more than one page.
     * @param {number} page the clamped, zero-based current page
     * @param {number} pageCount total number of pages
     * @returns {any} the pager, or nothing for a single page
     */
    paginationView(page, pageCount) {
        if (pageCount <= 1) return html``;
        return html`
        <nav class="pagination" aria-label="${messages.pagination}">
            <button aria-label="${messages.previousPage}" ?disabled="${page === 0}"
                @click="${_ => pageSelected(page - 1)}">‹</button>
            <span>${page + 1} / ${pageCount}</span>
            <button aria-label="${messages.nextPage}" ?disabled="${page === pageCount - 1}"
                @click="${_ => pageSelected(page + 1)}">›</button>
        </nav>
        `;
    }

    /**
     * Renders one cell: as an input while under edit, otherwise as a
     * click target starting the edit. Typing never dispatches — only
     * committing (Enter/blur via change) or cancelling (Escape) does.
     * @param {import('../entity/AddressesReducer.js').Address} address the row's address
     * @param {string} key the column's Address property
     * @returns {any} the table cell
     */
    cellView(address, key) {
        if (this.edit.id === address.id && this.edit.field === key) {
            return html`
            <td>
                <input name="${key}" .value="${address[key] ?? ''}"
                    @change="${({ target: { value } }) => updateCell(address.id, key, value)}"
                    @blur="${_ => cancelEdit()}"
                    @keydown="${e => this.onEditKey(e)}">
            </td>
            `;
        }
        return html`
        <td>
            <button class="cell" title="${messages.clickToEdit}" @click="${_ => editCell(address.id, key)}">${address[key]}</button>
        </td>
        `;
    }

    /**
     * Enter commits by leaving the field (change fires before blur),
     * Escape cancels the edit.
     * @param {KeyboardEvent & {target: HTMLInputElement}} event
     */
    onEditKey(event) {
        if (event.key === 'Enter') event.target.blur();
        if (event.key === 'Escape') cancelEdit();
    }

    /**
     * Focuses the freshly rendered edit input — rendering happens in
     * BElement's store subscription, focus is a browser-only concern.
     */
    triggerViewUpdate() {
        super.triggerViewUpdate();
        const input = /** @type {?HTMLInputElement} */ (this.querySelector('tbody input'));
        if (input && document.activeElement !== input) {
            input.focus();
            input.select();
        }
    }

    /**
     * @param {{key: string, label: string}} column
     * @returns {any} a sortable header cell with the current sort state
     */
    headerView({ key, label }) {
        const active = this.sort.by === key;
        const direction = this.sort.ascending ? 'ascending' : 'descending';
        return html`
        <th scope="col" aria-sort="${active ? direction : 'none'}">
            <button @click="${_ => addressesSorted(key)}">
                ${label} <span class="sort-indicator">${active ? (this.sort.ascending ? '▲' : '▼') : ''}</span>
            </button>
        </th>
        `;
    }
}
customElements.define('b-addresses-list', List);

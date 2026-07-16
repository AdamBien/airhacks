import BElement from "../../BElement.js";
import { html, nothing } from "lit-html";
import { toggleRow, editCell, commitCell, cancelCell, addNode, editName, commitName } from "../control/TreetableControl.js";

/**
 * @typedef {import("../entity/TreetableReducer.js").Row} Row
 * @typedef {import("../entity/TreetableReducer.js").Column} Column
 */

/**
 * Boundary op `show-tree` — renders the tree as a treegrid table (R1).
 * Collapsed subtrees are not rendered at all, so hiding needs no CSS.
 */
class Treetable extends BElement {

    /**
     * @param {Object} reduxState
     * @returns {import("../entity/TreetableReducer.js").TreetableState}
     */
    extractState({ treetable }) {
        return treetable;
    }

    view() {
        const { columns } = this.state;
        return html`
        <p class="actions">
            <button class="add-node" @click="${_ => addNode()}">add node</button>
        </p>
        <table role="treegrid" aria-label="treetable">
            <thead>
                <tr role="row">
                    <th role="columnheader" scope="col">name</th>
                    ${columns.map(column => html`<th role="columnheader" scope="col">${column.label}</th>`)}
                </tr>
            </thead>
            <tbody>
                ${this.visibleRows(this.state.rows).map(({ row, level }) => this.renderRow(row, level))}
            </tbody>
        </table>
        `;
    }

    /**
     * Flattens the tree into the rows to render: a row is visible when all
     * its ancestors are expanded (R2.1, R2.2).
     * @param {Row[]} rows
     * @param {number} level - 1-based depth, becomes aria-level (R1.7)
     * @returns {{row: Row, level: number}[]}
     */
    visibleRows(rows, level = 1) {
        return rows.flatMap(row => [
            { row, level },
            ...(this.state.expanded[row.id] ? this.visibleRows(row.children, level + 1) : [])
        ]);
    }

    /**
     * One tree row: hierarchy cell first (R1.2), value cells aligned to the
     * declared columns (R1.1, R1.4).
     * @param {Row} row
     * @param {number} level
     */
    renderRow(row, level) {
        const expandable = row.children.length > 0;
        const expanded = !!this.state.expanded[row.id];
        return html`
        <tr role="row" aria-level="${level}" aria-expanded="${expandable ? expanded : nothing}">
            <th role="rowheader" scope="row" style="--level: ${level}">
                ${expandable
                    ? html`<button class="toggle" aria-label="${expanded ? 'collapse' : 'expand'} ${row.label}"
                        @click="${_ => toggleRow(row.id)}">${expanded ? '▾' : '▸'}</button>`
                    : nothing}
                ${this.renderName(row)}
            </th>
            ${this.state.columns.map(column => this.renderCell(row, column))}
        </tr>
        `;
    }

    /**
     * The row's name — displayed text, or a text input while it is being
     * renamed (R5.1); an activatable name replaces the retired R3.5.
     * @param {Row} row
     */
    renderName(row) {
        const { editing } = this.state;
        if (editing && editing.rowId === row.id && editing.columnKey === null) {
            return html`
            <input class="name-edit" type="text" aria-label="rename ${row.label}"
                .value="${row.label}"
                @keydown="${event => this.onNameKey(event, row)}"
                @blur="${event => this.onNameBlur(event, row)}">
            `;
        }
        return html`
        <span class="name" tabindex="0"
            @click="${_ => this.activateName(row)}"
            @keydown="${event => { if (event.key === 'Enter') this.activateName(row); }}"
        >${row.label}</span>
        <button class="add-child" aria-label="add child to ${row.label}"
            @click="${_ => addNode(row.id)}">+</button>
        `;
    }

    /**
     * A value cell — displayed text, or a text input while it is being
     * edited (R3.1). The hierarchy cell never goes through here (R3.5).
     * @param {Row} row
     * @param {Column} column
     */
    renderCell(row, column) {
        const { editing } = this.state;
        if (editing && editing.rowId === row.id && editing.columnKey === column.key) {
            return html`
            <td role="gridcell" class="editing">
                <input type="text" aria-label="edit ${column.label} of ${row.label}"
                    .value="${row.values[column.key] ?? ''}"
                    @keydown="${event => this.onEditKey(event, row, column)}"
                    @blur="${event => this.onEditBlur(event, row, column)}">
            </td>
            `;
        }
        return html`
        <td role="gridcell" tabindex="0"
            @click="${_ => this.activate(row, column)}"
            @keydown="${event => { if (event.key === 'Enter') this.activate(row, column); }}"
        >${row.values[column.key] ?? ''}</td>
        `;
    }

    /**
     * Opens the cell for editing; the dispatch renders synchronously, so the
     * input exists and can take focus right after (R3.1).
     * @param {Row} row
     * @param {Column} column
     */
    activate(row, column) {
        editCell(row.id, column.key);
        /** @type {?HTMLInputElement} */ (this.querySelector('td.editing input'))?.focus();
    }

    /**
     * Enter commits, Escape cancels (R3.2, R3.3).
     * @param {KeyboardEvent} event
     * @param {Row} row
     * @param {Column} column
     */
    onEditKey(event, row, column) {
        const input = /** @type {HTMLInputElement} */ (event.target);
        if (event.key === 'Enter') commitCell(row.id, column.key, input.value);
        if (event.key === 'Escape') cancelCell();
    }

    /**
     * Focus leaving the input commits (R3.2). Commit and cancel re-render
     * synchronously and clear the editing state before the input's blur
     * fires, so this stays a no-op after Enter or Escape.
     * @param {FocusEvent} event
     * @param {Row} row
     * @param {Column} column
     */
    onEditBlur(event, row, column) {
        const input = /** @type {HTMLInputElement} */ (event.target);
        if (this.state.editing) commitCell(row.id, column.key, input.value);
    }

    /**
     * Opens the row's name for renaming; the dispatch renders synchronously,
     * so the input exists and can take focus right after (R5.1).
     * @param {Row} row
     */
    activateName(row) {
        editName(row.id);
        /** @type {?HTMLInputElement} */ (this.querySelector('th input.name-edit'))?.focus();
    }

    /**
     * Enter commits, Escape cancels (R5.2, R5.3).
     * @param {KeyboardEvent} event
     * @param {Row} row
     */
    onNameKey(event, row) {
        const input = /** @type {HTMLInputElement} */ (event.target);
        if (event.key === 'Enter') commitName(row.id, input.value);
        if (event.key === 'Escape') cancelCell();
    }

    /**
     * Focus leaving the input commits (R5.2) — same trailing-blur no-op
     * guard as the value cells.
     * @param {FocusEvent} event
     * @param {Row} row
     */
    onNameBlur(event, row) {
        const input = /** @type {HTMLInputElement} */ (event.target);
        const { editing } = this.state;
        if (editing && editing.columnKey === null) commitName(row.id, input.value);
    }
}
customElements.define('b-treetable', Treetable);

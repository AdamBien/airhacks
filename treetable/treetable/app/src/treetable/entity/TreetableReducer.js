import { createReducer } from "@reduxjs/toolkit";
import { toggleRowAction, editCellAction, commitCellAction, cancelCellAction, addNodeAction, editNameAction, commitNameAction } from "../control/TreetableControl.js";

/**
 * @typedef {Object} Column
 * @property {string} key - identifies the value inside {@link Row} values
 * @property {string} label - column header text
 */

/**
 * @typedef {Object} Row
 * @property {string} id - stable row identity
 * @property {string} label - hierarchy column text
 * @property {Object<string, string>} values - cell values keyed by {@link Column} key
 * @property {Row[]} children - nested rows
 */

/**
 * @typedef {Object} TreetableState
 * @property {Column[]} columns - value columns in declared order (R1.4)
 * @property {Row[]} rows - root rows of the tree
 * @property {Object<string, boolean>} expanded - expansion state per row id; empty on first run (R1.5)
 * @property {?{rowId: string, columnKey: ?string}} editing - the cell currently edited in place (columnKey null means the row's name), or null
 */

/**
 * Bundled sample tree (R1.6) — seeds the state when nothing is persisted yet.
 * @type {TreetableState}
 */
const initialState = {
    columns: [
        { key: "owner", label: "owner" },
        { key: "status", label: "status" },
        { key: "effort", label: "effort" }
    ],
    rows: [
        {
            id: "p1", label: "bce.design", values: { owner: "duke", status: "active", effort: "13" }, children: [
                {
                    id: "t1", label: "routing", values: { owner: "duke", status: "done", effort: "3" }, children: [
                        { id: "s1", label: "URLPattern matching", values: { owner: "duke", status: "done", effort: "1" }, children: [] },
                        { id: "s2", label: "Navigation API interception", values: { owner: "duke", status: "done", effort: "2" }, children: [] }
                    ]
                },
                { id: "t2", label: "state management", values: { owner: "joe", status: "active", effort: "5" }, children: [] }
            ]
        },
        {
            id: "p2", label: "treetable", values: { owner: "adam", status: "active", effort: "8" }, children: [
                { id: "t3", label: "inline editing", values: { owner: "adam", status: "planned", effort: "5" }, children: [] }
            ]
        }
    ],
    expanded: {},
    editing: null
};

/**
 * Depth-first lookup of a row anywhere in the tree.
 * @param {Row[]} rows
 * @param {string} id
 * @returns {?Row}
 */
const findRow = (rows, id) => {
    for (const row of rows) {
        if (row.id === id) return row;
        const found = findRow(row.children, id);
        if (found) return found;
    }
    return null;
}

/**
 * Treetable state transitions.
 *
 * Expansion is a per-row-id flag map: collapsing a row leaves its descendants'
 * flags untouched, so re-expanding restores their prior state (R2.3). The
 * whole state is persisted by the application-wide localStorage subscription,
 * which carries expansion (R2.4) and committed edits (R3.4) across reloads.
 */
export const treetable = createReducer(initialState, (builder) => {
    builder.addCase(toggleRowAction, (state, { payload }) => {
        state.expanded[payload] = !state.expanded[payload];
    }).addCase(editCellAction, (state, { payload: { rowId, columnKey } }) => {
        state.editing = { rowId, columnKey };
    }).addCase(commitCellAction, (state, { payload: { rowId, columnKey, value } }) => {
        const row = findRow(state.rows, rowId);
        if (row) row.values[columnKey] = value;
        state.editing = null;
    }).addCase(cancelCellAction, (state) => {
        state.editing = null;
    }).addCase(editNameAction, (state, { payload }) => {
        state.editing = { rowId: payload, columnKey: null };
    }).addCase(commitNameAction, (state, { payload: { rowId, value } }) => {
        const row = findRow(state.rows, rowId);
        if (row && value.trim()) row.label = value;
        state.editing = null;
    }).addCase(addNodeAction, (state, { payload: { parentId, id } }) => {
        const node = { id, label: "new node", values: {}, children: [] };
        if (!parentId) {
            state.rows.push(node);
            return;
        }
        const parent = findRow(state.rows, parentId);
        if (!parent) return;
        parent.children.push(node);
        state.expanded[parentId] = true;
    });
});

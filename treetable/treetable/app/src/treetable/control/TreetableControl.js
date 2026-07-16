import { createAction } from "@reduxjs/toolkit";
import store from "../../store.js";

export const toggleRowAction = createAction("toggleRowAction");

/**
 * Boundary op `toggle-row` — expand or collapse a row's subtree (R2).
 * @param {string} rowId
 */
export const toggleRow = (rowId) => {
    store.dispatch(toggleRowAction(rowId));
}

export const editCellAction = createAction("editCellAction");

/**
 * Boundary op `edit-cell` — open a value cell for in-place editing (R3.1).
 * @param {string} rowId
 * @param {string} columnKey
 */
export const editCell = (rowId, columnKey) => {
    store.dispatch(editCellAction({ rowId, columnKey }));
}

export const commitCellAction = createAction("commitCellAction");

/**
 * Commits the open edit: stores the value and leaves edit mode (R3.2).
 * @param {string} rowId
 * @param {string} columnKey
 * @param {string} value
 */
export const commitCell = (rowId, columnKey, value) => {
    store.dispatch(commitCellAction({ rowId, columnKey, value }));
}

export const editNameAction = createAction("editNameAction");

/**
 * Boundary op `rename-node` — open a row's name for in-place editing (R5.1).
 * @param {string} rowId
 */
export const editName = (rowId) => {
    store.dispatch(editNameAction(rowId));
}

export const commitNameAction = createAction("commitNameAction");

/**
 * Commits the open rename: stores the name and leaves edit mode (R5.2);
 * an empty name keeps the previous one (R5.4).
 * @param {string} rowId
 * @param {string} value
 */
export const commitName = (rowId, value) => {
    store.dispatch(commitNameAction({ rowId, value }));
}

export const addNodeAction = createAction("addNodeAction");

/**
 * Boundary op `add-node` — append a new row, as a root (no parentId) or as
 * a child of an existing row (R4).
 * @param {?string} [parentId] - parent row id, or null/omitted for a root row
 */
export const addNode = (parentId = null) => {
    store.dispatch(addNodeAction({ parentId, id: `n${Date.now()}` }));
}

export const cancelCellAction = createAction("cancelCellAction");

/**
 * Discards the open edit, keeping the previous value (R3.3).
 */
export const cancelCell = _ => {
    store.dispatch(cancelCellAction());
}

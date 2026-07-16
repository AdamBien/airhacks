/**
 * Read-side queries over the address list: pure derivation of what the
 * table displays — sorting and pagination. No store access, no dispatch;
 * the boundary passes state in and renders what comes back. Counterpart
 * of the dispatchers in CRUDControl.js, which own the write side.
 */
import { PAGE_SIZE } from "../entity/AddressesReducer.js";

/** @typedef {import('../entity/AddressesReducer.js').Address} Address */

/**
 * @param {Address[]} list saved addresses in insertion order
 * @param {import('../entity/AddressesReducer.js').Sort} sort the sort criteria
 * @returns {Address[]} a sorted copy — the insertion order for by = null
 */
export const sorted = (list, { by, ascending }) => {
    if (!by) return list;
    const result = list.toSorted((a, b) =>
        String(a[by] ?? '').localeCompare(String(b[by] ?? ''), undefined, { numeric: true }));
    return ascending ? result : result.reverse();
}

/**
 * @param {Address[]} list saved addresses
 * @returns {number} the number of table pages, at least 1
 */
export const pageCount = (list) => Math.max(1, Math.ceil(list.length / PAGE_SIZE));

/**
 * A persisted page can outlive a shrinking list — clamping on read
 * keeps any stale page displayable.
 * @param {Address[]} list saved addresses
 * @param {number} page the requested zero-based page
 * @returns {number} the page, clamped to the existing range
 */
export const clampedPage = (list, page) => Math.max(0, Math.min(page, pageCount(list) - 1));

/**
 * @param {Address[]} list saved addresses
 * @param {number} page the zero-based page
 * @returns {Address[]} the page's addresses
 */
export const pageOf = (list, page) => list.slice(page * PAGE_SIZE, (page + 1) * PAGE_SIZE);

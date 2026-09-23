import { createReducer } from "@reduxjs/toolkit";
import { requestUpdatedAction, startRentalAction, returnBikeAction } from "../control/Renting.js";
import { seedFleet, availableBikeOf } from "./Fleet.js";
import { BIKE_TYPES, isValidDays, priceOf } from "./Pricing.js";

/**
 * @typedef {Object} Customer
 * @property {string} name
 * @property {string} email
 */

/**
 * @typedef {Object} Rental one bike in a customer's hands for a chosen number of days
 * @property {number} id
 * @property {string} bikeId
 * @property {string} type
 * @property {number} days
 * @property {number} price total in euros, fixed at start
 * @property {Customer} customer
 * @property {"open"|"returned"} status
 * @property {string} startedAt ISO timestamp
 */

/**
 * @typedef {Object} RentalRequest temporal cache of the start form
 * @property {string} type
 * @property {number} days
 * @property {string} name
 * @property {string} email
 */

/**
 * @typedef {Object} RentalState
 * @property {import("./Fleet.js").Bike[]} fleet
 * @property {Rental[]} rentals newest first
 * @property {RentalRequest} request
 * @property {string} notice why the last start was rejected, empty otherwise
 */

const emptyRequest = () => ({ type: "bike", days: 1, name: "", email: "" });

/** @type {RentalState} */
const initialState = {
    fleet: seedFleet(),
    rentals: [],
    request: emptyRequest(),
    notice: ""
};

const present = value => typeof value === "string" && value.trim() !== "";

/**
 * @param {RentalRequest} request
 * @returns {string} the reason the request cannot be started, empty when it can
 */
const rejectionOf = ({ type, days, name, email }) => {
    if (!BIKE_TYPES.includes(type)) return `unknown bike type: ${type}`;
    if (!isValidDays(days)) return "days must be between 1 and 14";
    if (!present(name) || !present(email)) return "name and email are required";
    return "";
};

/**
 * Owns the fleet, the rentals and the start form cache. Starting a rental
 * validates the cached request, allocates the first available bike of the
 * requested type and prepends the rental; a rejected start leaves the fleet
 * untouched and records the reason in `notice`. Returning a bike closes the
 * rental and frees its bike; a rental that is not open is left as it is.
 */
export const rental = createReducer(initialState, (builder) => {
    builder.addCase(requestUpdatedAction, (state, { payload: { name, value } }) => {
        state.request[name] = name === "days" ? Number(value) : value;
        state.notice = "";
    }).addCase(startRentalAction, (state, { payload: { id, startedAt } }) => {
        const { request } = state;
        const rejection = rejectionOf(request);
        if (rejection) {
            state.notice = rejection;
            return;
        }
        const bike = availableBikeOf(state.fleet, request.type);
        if (!bike) {
            state.notice = `no ${request.type} available`;
            return;
        }
        bike.status = "allocated";
        state.rentals.unshift({
            id,
            bikeId: bike.id,
            type: bike.type,
            days: request.days,
            price: priceOf(bike.type, request.days),
            customer: { name: request.name.trim(), email: request.email.trim() },
            status: "open",
            startedAt
        });
        state.request = emptyRequest();
        state.notice = "";
    }).addCase(returnBikeAction, (state, { payload }) => {
        const open = state.rentals.find(({ id, status }) => id === payload && status === "open");
        if (!open) return;
        open.status = "returned";
        const bike = state.fleet.find(({ id }) => id === open.bikeId);
        if (bike) bike.status = "available";
    });
});

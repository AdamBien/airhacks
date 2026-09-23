import { createAction } from "@reduxjs/toolkit";
import store from "../../store.js";
import { priceOf, BIKE_TYPES, MIN_DAYS, MAX_DAYS } from "../entity/Pricing.js";
import { countAvailable } from "../entity/Fleet.js";

export { BIKE_TYPES, MIN_DAYS, MAX_DAYS };

export const requestUpdatedAction = createAction("requestUpdatedAction");
/**
 * Caches one field of the start form.
 * @param {string} name field name: type, days, name or email
 * @param {string} value the entered value
 */
export const requestUpdated = (name, value) => {
    store.dispatch(requestUpdatedAction({ name, value }));
};

export const startRentalAction = createAction("startRentalAction");
/**
 * Starts a rental from the cached request; the reducer decides whether it is accepted.
 */
export const startRental = _ => {
    store.dispatch(startRentalAction({ id: Date.now(), startedAt: new Date().toISOString() }));
};

export const returnBikeAction = createAction("returnBikeAction");
/**
 * @param {number} rentalId the rental whose bike comes back
 */
export const returnBike = rentalId => {
    store.dispatch(returnBikeAction(rentalId));
};

/**
 * Quote for a type and a number of days before renting.
 * @param {string} type
 * @param {number} days
 * @returns {number} total price in euros
 */
export const quotePrice = (type, days) => priceOf(type, days);

/**
 * @param {import("../entity/Fleet.js").Bike[]} fleet
 * @returns {{type: string, available: number}[]} available bikes per type
 */
export const availability = fleet =>
    BIKE_TYPES.map(type => ({ type, available: countAvailable(fleet, type) }));

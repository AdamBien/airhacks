/**
 * Daily rates per bike type in whole euros (spec R2.1).
 * @type {Readonly<Record<string, number>>}
 */
export const DAILY_RATES = Object.freeze({
    "bike": 10,
    "e-bike": 25
});

/** @type {ReadonlyArray<string>} the bike types customers can rent */
export const BIKE_TYPES = Object.freeze(Object.keys(DAILY_RATES));

/** @type {number} */
export const MIN_DAYS = 1;
/** @type {number} */
export const MAX_DAYS = 14;

/**
 * Total price for renting a type for a number of days: days times the daily rate.
 * @param {string} type bike type
 * @param {number} days rental length in days
 * @returns {number} total price in euros
 */
export const priceOf = (type, days) => DAILY_RATES[type] * days;

/**
 * @param {number} days
 * @returns {boolean} whether the length is a whole number of days within the allowed range
 */
export const isValidDays = days => Number.isInteger(days) && days >= MIN_DAYS && days <= MAX_DAYS;

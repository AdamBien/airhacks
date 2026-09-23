/**
 * @typedef {Object} Bike one physical bicycle of the fleet
 * @property {string} id
 * @property {string} type "bike" or "e-bike"
 * @property {"available"|"allocated"} status allocated while in an open rental
 */

/**
 * The seed fleet the app starts with: 5 bikes and 3 e-bikes, all available (spec R1.1).
 * @returns {Bike[]}
 */
export const seedFleet = () => [
    ...["B1", "B2", "B3", "B4", "B5"].map(id => ({ id, type: "bike", status: "available" })),
    ...["E1", "E2", "E3"].map(id => ({ id, type: "e-bike", status: "available" }))
];

/**
 * @param {Bike[]} fleet
 * @param {string} type
 * @returns {Bike|undefined} the first available bike of the type, if any
 */
export const availableBikeOf = (fleet, type) =>
    fleet.find(bike => bike.type === type && bike.status === "available");

/**
 * @param {Bike[]} fleet
 * @param {string} type
 * @returns {number} the bikes of the type that are not in an open rental
 */
export const countAvailable = (fleet, type) =>
    fleet.filter(bike => bike.type === type && bike.status === "available").length;

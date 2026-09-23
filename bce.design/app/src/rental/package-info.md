# Rental
> Rent an available bike or e-bike from the Amsterdam fleet to a customer for a chosen number of days and take it back on return.

## Boundary
- `show-availability` — show how many bikes of each type can be rented right now
- `quote-price` — show the total price for a chosen type and number of days before renting
- `start-rental` — hand an available bike of the chosen type to a customer for the chosen days at the quoted price
- `return-bike` — take the bike back and close the rental
- `list-rentals` — show every rental, open and returned

## Requirements
### R1: Show availability
- R1.1 — The BC shall start with a fleet of 5 bikes and 3 e-bikes, all available. _(why: a standalone app needs a fleet to rent from, and a small one makes "sold out" reachable)_
- R1.2 — The BC shall show, per type, the number of bikes that are not in an open rental.
- R1.3 — While every bike of a type is in an open rental, the BC shall show that type as sold out.

### R2: Quote a price
- R2.1 — The BC shall charge 10 euros per day for a bike and 25 euros per day for an e-bike.
- R2.2 — When a type and a number of days are chosen, the BC shall show the total price, days times the daily rate, before the rental is confirmed.

### R3: Start a rental
- R3.1 — When a customer with name and email confirms a type and a number of days between 1 and 14 while a bike of that type is available, the BC shall allocate one bike of that type and open a rental at the quoted price.
- R3.2 — If no bike of the chosen type is available, then the BC shall reject the start and leave the fleet unchanged.
- R3.3 — If the name or the email is missing, then the BC shall reject the start.
- R3.4 — If the number of days is below 1 or above 14, then the BC shall reject the start.

### R4: Return a bike
- R4.1 — While a rental is open, when its bike is returned, the BC shall close the rental and make the bike available again.
- R4.2 — If a returned rental is returned again, then the BC shall reject the return.

### R5: List rentals
- R5.1 — The BC shall list every rental, open and returned, with its bike, type, days, price and state, newest first.
- R5.2 — When the app is reopened, the BC shall show the rentals and the availability as they were left. _(why: a customer must find their open rental again after closing the browser)_

## Entities
- Bike, Rental

## Decisions
- D1 — One `rental` BC owns availability, pricing and the rental lifecycle. _(why: standalone, customer-only app with a single journey; rejected: mirroring the backend's `fleet` / `reservation` / `rental` carving)_
- D2 — The app is standalone; state lives in the browser store and localStorage. _(why: no backend dependency for the frontend quickstarter; rejected: calling the abr Quarkus backend's REST API)_
- D3 — Days are chosen and priced when the rental starts; return only closes it. _(why: a browser session cannot wait for real days to pass; rejected: pricing started days at return)_

## Out of scope
- Advance reservations for a pickup day.
- Fleet management: registering, withdrawing, or restoring bikes.
- Choosing a specific bike by identifier.
- Collecting payment, deposits, refunds, and assessing damage on return.

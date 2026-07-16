# Contacts
> Own the contacts UI: a searchable, sortable listing with create, edit, and delete flows backed by the contacts service.

## Boundary
- `list-contacts` — display all contacts as a searchable, sortable listing → R1, R2, R3
- `add-contact` — capture and submit a new contact → R4, R7
- `edit-contact` — change a contact's fields → R5, R7
- `delete-contact` — remove a contact → R6

## Requirements
### R1: List contacts
- R1.1 — When the listing is opened, the BC shall display all contacts provided by the contacts service. _(why: the service is the source of truth — inception decision)_
- R1.2 — The BC shall display each contact's last name, first name, email, phone, and type.

### R2: Search
- R2.1 — While a search term is entered, the BC shall display only contacts whose first name, last name, or email contains the term, ignoring case.
- R2.2 — When the search term is cleared, the BC shall display all contacts.

### R3: Sort
- R3.1 — When a listing field is selected for sorting, the BC shall order the displayed contacts by that field.
- R3.2 — When the already-selected field is selected again, the BC shall reverse the order.

### R4: Add a contact
- R4.1 — When a contact carrying a last name is submitted, the BC shall send it to the contacts service and display it in the listing.
- R4.2 — If the last name is missing, then the BC shall block the submission and indicate the missing field. _(why: last name is the only mandatory field — inception decision)_
- R4.3 — If the email address is malformed, then the BC shall block the submission and indicate the invalid field.

### R5: Edit a contact
- R5.1 — When a contact is opened for editing, the BC shall prefill the form with its stored fields.
- R5.2 — When an edited contact with valid fields is submitted, the BC shall send the change to the contacts service and display the updated fields in the listing.

### R6: Delete a contact
- R6.1 — When a contact's deletion is triggered, the BC shall remove it via the contacts service and from the listing.

### R7: Contact type
- R7.1 — The BC shall offer exactly the types `business` and `private` for a contact, preselected to `private` for a new contact. _(why: type is mandatory — a preselected fixed choice needs no extra click)_
- R7.2 — When a contact is opened for editing, the BC shall preselect its stored type.
- R7.3 — When a contact is saved, the BC shall send the selected type to the contacts service.

## Entities
- Contact

## Out of scope
- offline edits and background sync — the service is the source of truth
- tags, groups, favorites
- pagination

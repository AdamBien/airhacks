/// # Contacts
/// > Own the contact lifecycle: validate, store, and serve contacts from an in-memory store.
///
/// ## Boundary
/// - `list-contacts` — return all stored contacts → R2
/// - `get-contact` — return one stored contact by id → R2
/// - `create-contact` — validate, store, and return a new contact with an assigned id → R1, R5
/// - `update-contact` — validate and replace the fields of a stored contact → R3, R5
/// - `delete-contact` — remove a stored contact → R4
///
/// ## Requirements
/// ### R1: Create a contact
/// - R1.1 — When a contact carrying a last name is submitted, the BC shall store it and assign it a unique id.
/// - R1.2 — When a contact is created, the BC shall return the stored contact including its assigned id. _(why: the client needs the id for follow-up edits and deletes)_
/// - R1.3 — If a submitted contact carries no last name, then the BC shall reject the creation. _(why: last name is the only mandatory field — inception decision)_
/// - R1.4 — If a submitted contact carries a malformed email address, then the BC shall reject the creation.
///
/// ### R2: Read contacts
/// - R2.1 — The BC shall provide all stored contacts.
/// - R2.2 — When a contact is requested by id, the BC shall return it.
/// - R2.3 — If a requested id is unknown, then the BC shall report that the contact does not exist.
///
/// ### R3: Update a contact
/// - R3.1 — When a stored contact is updated with valid fields, the BC shall replace its stored fields.
/// - R3.2 — If an update targets an unknown id, then the BC shall report that the contact does not exist.
/// - R3.3 — If an update carries no last name or a malformed email address, then the BC shall reject it.
///
/// ### R4: Delete a contact
/// - R4.1 — When a stored contact is deleted, the BC shall remove it.
/// - R4.2 — If a deletion targets an unknown id, then the BC shall report that the contact does not exist.
///
/// ### R5: Contact type
/// - R5.1 — When a submitted contact carries the type `business` or `private`, the BC shall store the type.
/// - R5.2 — If a submitted contact carries no type, then the BC shall reject it. _(why: type is mandatory — inception decision)_
/// - R5.3 — If a submitted contact carries a type other than `business` or `private`, then the BC shall reject it. _(why: closed value set — no free-text categories)_
///
/// ## Entities
/// - Contact, ContactType
///
/// ## Out of scope
/// - persistence across restarts _(why: in-memory store — inception decision)_
/// - search, filtering, sorting — client-side concern of the frontend `contacts` BC
/// - pagination, authentication, duplicate detection
package airhacks.contacts.contacts;

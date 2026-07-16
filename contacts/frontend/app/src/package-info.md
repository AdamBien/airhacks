# contacts
> Manage personal contacts end to end: browse, find, create, edit, and delete them from the browser, backed by the contacts service.

## Vision
- Every contact one keystroke away — a contacts book that feels local, on a standards stack that outlives frameworks.

## Components
- frontend `contacts` calls backend `contacts` (JSON/HTTP) for all reads and mutations; the service is the source of truth — never the reverse.

## Ubiquitous language
- Contact — a person entry: first and last name, email, phone, and a mandatory type (`business` or `private`); identified by a service-assigned id. Owned by backend `contacts`.

## Stack
- web-components (lit-html, reduction.js, Navigation API routing) · source root `app/src`
- backend tier: microprofile-server · base package `airhacks.contacts` (see `backend/service/src/main/java/airhacks/contacts/package-info.java`)

/// # Hubtime
/// > Turn a meeting instant in the user's time zone into a table of local times across the major business hubs.
///
/// ## Boundary
/// - `show-hub-times` — render the hub-times table for an optional meeting date-time
///
/// ## Requirements
/// ### R1: Show hub times
/// - R1.1 — When invoked without a date-time, the BC shall render the table for the current moment.
/// - R1.2 — When invoked with a valid date-time, the BC shall interpret it in the system default time zone and render the table for that instant.
/// - R1.3 — The BC shall list, for each major business hub (San Francisco, New York, London, Frankfurt, Vienna, Dubai, Mumbai, Singapore, Tokyo, Sydney), the hub's local date and time for the requested instant.
/// - R1.4 — The BC shall mark each hub whose local time falls outside business hours (09:00–18:00 local). _(why: the point of the table is spotting slots inconvenient for attendees in other zones)_
///
/// ### R2: Reject invalid input
/// - R2.1 — If the date-time argument is unparseable, then the BC shall report an error naming the expected format and signal failure.
///
/// ## Entities
/// - Hub
///
/// ## Out of scope
/// - Persisting meetings or attendees
/// - User-supplied or configurable time zones and business hours
/// - Overriding the source time zone (always the system default)
package airhacks.meetingschedule.hubtime;

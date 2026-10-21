/// # Yield Planning
/// > Plan the mine's coal yield per day, in whole tonnes and with a stone size (the maximum coal lump size), and lock each plan once its day starts.
///
/// ## Boundary
/// - `plan-day` — set the planned tonnage and stone size for a day that has no plan yet
/// - `revise-plan` — change the planned tonnage, the stone size, or both, of an already planned day
/// - `delete-plan` — remove a day's plan
/// - `get-plan` — read the plan of one day
/// - `list-plans` — read the plans within a date range
///
/// ## Requirements
/// ### R1: Plan a day
/// - R1.1 — When a date, a planned tonnage, and a stone size are submitted for a day without a plan, the BC shall store them as that day's plan.
/// - R1.2 — When the planned tonnage is zero, the BC shall accept it. _(why: planned non-production days, e.g. maintenance)_
/// - R1.3 — If a plan already exists for the submitted date, then the BC shall reject the submission. _(why: changes must be explicit revisions)_
/// - R1.4 — If the date is missing, the planned tonnage is missing, negative, or not a whole number, or the stone size is missing or not a whole number of millimetres above zero, then the BC shall reject the submission, naming every invalid part.
///
/// ### R2: Revise a plan
/// - R2.1 — When a planned tonnage is submitted for a day that has a plan, the BC shall replace that day's planned tonnage.
/// - R2.2 — If the day has no plan, then the BC shall reject the revision as not found.
/// - R2.4 — When a stone size is submitted for a day that has a plan, the BC shall replace that day's stone size.
/// - R2.5 — When a revision omits the planned tonnage or the stone size, the BC shall keep the omitted value unchanged.
/// - R2.6 — If a revision submits neither a planned tonnage nor a stone size, then the BC shall reject the revision, naming both.
/// - R2.7 — If a submitted planned tonnage is negative or not a whole number, or a submitted stone size is not a whole number of millimetres above zero, then the BC shall reject the revision, naming every invalid part.
///
/// ### R3: Delete a plan
/// - R3.1 — When deletion of a day's plan is requested, the BC shall remove that plan.
/// - R3.2 — If the day has no plan, then the BC shall reject the deletion as not found.
///
/// ### R4: Get a plan
/// - R4.1 — When the plan of a day is requested, the BC shall return its date, planned tonnage, and stone size, whether or not the day has started.
/// - R4.2 — If the day has no plan, then the BC shall report that no plan exists.
///
/// ### R5: List plans
/// - R5.1 — When plans are requested for a from–to date range, the BC shall return the plans dated within that range, both ends inclusive, ordered by date ascending.
/// - R5.2 — The BC shall omit days without a plan from a listed range.
/// - R5.3 — If the from date is after the to date, then the BC shall reject the request.
/// - R5.4 — If the from date or the to date is missing, then the BC shall reject the request, naming every missing part.
///
/// ### R6: Lock started days
/// - R6.1 — The BC shall treat a day as started from midnight in the mine's time zone. _(why: the plan is set against the mine's local working day)_
/// - R6.2 — While a day has started, the BC shall reject planning it.
/// - R6.3 — While a day has started, the BC shall reject revising its plan.
/// - R6.4 — While a day has started, the BC shall reject deleting its plan. _(why: a plan in effect or in the past is a record, not a draft)_
///
/// ## Entities
/// - YieldPlan
///
/// ## Out of scope
/// - Recording actual production and comparing plan vs. actual
/// - Generating plans from capacity, crews, or equipment
/// - Planning per shift or per section/seam/pit
/// - More than one mine
/// - Keeping plans across a service restart
/// - Limiting how far ahead a day can be planned
package airhacks.qmp.yieldplanning;

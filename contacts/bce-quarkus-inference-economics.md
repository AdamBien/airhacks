# Discussion: Quarkus + MicroProfile, BCE, and LLM Inference Economics

*Saved from a Claude Code session, 2026-07-16, in `contacts/backend`.*

## 1. What is this project?

The backend of a full-stack contacts management app — a Quarkus MicroProfile service (Java 25) exposing contact CRUD via JAX-RS, backed by an in-memory store. Two Maven modules: `service` (the app) and `service-st` (standalone system tests via MicroProfile REST Client). Spec-driven via `/sbce`: the capability spec lives in the BC's `package-info.java` and is the boundary contract. The frontend is a standards-based SPA (custom elements, lit-html, Navigation API) talking to this service over JSON/HTTP. Both tiers follow BCE with a matching `contacts` business component.

## 2. Why Quarkus + MicroProfile is a good idea

- **The API surface is a standard, not a framework.** Code depends on JAX-RS, CDI, JSON-P — specifications with multiple implementations. Quarkus is the runtime; almost nothing imports `io.quarkus.*`. Business code outlives the runtime choice: swap the pom, not the code.
- **Quarkus makes the standards fast and pleasant.** Build-time CDI wiring (fast startup, low memory, DI errors at compile time), live reload (`quarkus:dev`), optional native compilation — without proprietary APIs. Developer experience and portability don't fight each other.
- **Minimal dependency graph.** MicroProfile-only pom = fewer transitive dependencies, fewer CVE fire drills, fewer breaking upgrades. Specs evolve slowly and deliberately.
- **Fits BCE naturally.** Boundaries are plain JAX-RS resources, controls are CDI beans, entities are records. No framework base classes; unit-test controls as plain objects, system-test the boundary — exactly the `service` / `service-st` split.
- **Trade-off:** smaller ecosystem/hiring pool than Spring; a full server runtime may be the wrong shape for very short-lived serverless functions.

## 3. Why it matters to an enterprise company

- **Application lifespan exceeds framework lifespan.** Enterprise apps live 10–20 years; frameworks reinvent themselves every few years. Spec-based code minimizes forced rewrites — pure cost with zero business value.
- **Vendor neutrality is negotiating power.** MicroProfile/Jakarta EE runs on Quarkus, Open Liberty, WildFly, Payara, Helidon. If licensing changes, migrate the runtime, not the portfolio.
- **Auditability and security posture.** Minimal spec-based dependency graph = less attack surface, less CVE-response burden, multiplied across hundreds of applications.
- **Predictable skills.** JAX-RS/CDI knowledge from 2015 still applies. Same standard APIs across all apps lowers onboarding cost and key-person risk.
- **Operational density.** Fast startup, low memory → more instances per node, faster scaling/recovery. At hundreds of services, memory is a line item.
- **Pattern:** enterprises optimize for cost of *change* and cost of *risk* over decades, not first-release speed. Standards are boring exactly where enterprises need boring.

## 4. BCE architecture

Boundary-Control-Entity goes back to Ivar Jacobson's use-case-driven OOSE. Modern application (bce.design): the system is cut into **business components** (vertical slices named after business capabilities, e.g. `contacts`), each internally layered:

- **boundary** — public face: interface to the outside world (JAX-RS resources), the only thing other components may call.
- **control** — internal implementation detail: process logic, orchestration, data access. Optional; never visible outside the BC.
- **entity** — domain objects, state, persistence-mapped types.

Flow: boundary → control → entity, never backwards. Cross-component calls go boundary-to-boundary (or via events). Benefits: screaming architecture (packages named `contacts`, not `services`), 1:1 mapping from use case to code location, natural test strategy (unit-test controls/entities, system-test the boundary). SBCE adds the spec dimension: one capability spec ≡ one BC, spec as boundary contract in `package-info.java`.

## 5. Does BCE scale to huge projects?

Yes — the unit of growth is horizontal, not vertical. In layered architectures every use case fattens every layer package; complexity grows inside shared containers. In BCE, BC #150 has the same internal shape as BC #3 — cognitive load per component is constant; only the count grows. Same property that makes microservices scale organizationally, without the distribution tax until needed.

What carries the load at scale:

- **Boundary-to-boundary rule** keeps the N² relationship graph coarse and auditable — must be enforced mechanically (ArchUnit, module systems) beyond ~50 developers.
- **The carving, not the pattern.** BCE structures the inside of a component; where to cut is strategic DDD work — that's where huge projects succeed or fail.
- **Team ownership maps cleanly.** One team owns a set of BCs; the BC is simultaneously the unit of code, spec, ownership, and (later, if needed) deployment.

Limits: at some size build times / release coordination push BCs into separately deployed services — BCE makes that promotion cheap because the boundary already is the contract. Shared concerns (auth, money types, audit) need deliberate shared-kernel BCs.

## 6. Inference cost savings — the estimate

**Measured in this repo:**

- Entire backend business logic: 10.3 KB Java (~2,600 tokens)
- The `contacts` BC (spec + boundary + control + entity): ~8.4 KB (~2,100 tokens)
- The two READMEs sufficient to explain the whole full-stack system: 3.8 KB (~950 tokens)

Full system comprehension for an LLM agent: under 1,000 tokens. Complete implementation of a BC: ~2,000.

**Cost model:** agentic task cost ≈ turns × avg context per turn × input price + output. Architecture affects both factors:

- *Context per turn:* BCE — one BC (~2k tokens) plus co-located spec (compressed intent; no reverse-engineering purpose from code). Layered — the use case spans controllers/services/repositories/DTOs/mappers + config: 10–20 files, 30–60k tokens.
- *Turns:* screaming architecture → discovery is one glob (`**/contacts/**`). Layer packages → iterative grep-read rounds; each turn re-sends the conversation. Misunderstood intent adds retry loops — the most expensive failure mode.

**Per-task estimate** (Claude Opus 4.8: $5 input / $25 output per MTok, uncached):

| | BCE/SBCE | Conventional layered |
|---|---|---|
| Turns × avg context | ~12 × 15k ≈ 180k input tokens | ~35 × 45k ≈ 1.6M input tokens |
| Cost per task | ≈ **$1.10** | ≈ **$8.30** |

Prompt caching (reads ~0.1× input price) compresses both proportionally — the ratio of **~5–8× per task** holds. On Fable 5 pricing ($10/$50) the absolute gap doubles.

**At scale:** a team running 50 agent-assisted tasks/day: ~$55/day (BCE) vs ~$400/day (layered) — roughly **$85k/year difference per team**, before counting developer time saved by fewer agent retries. Across 100 teams: a seven-figure line item.

**Caveat:** the layered-project figures (35 turns / 45k context) are informed guesses; the BCE side is measured. The defensible claim is the mechanism: constant-size BCs, co-located specs, and business-named packages make the context an agent needs small and findable in one shot.

**Conclusion:** SBCE is prompt engineering applied to the repository layout — the same properties that make BCE good for human cognition make it cheap for machine cognition.

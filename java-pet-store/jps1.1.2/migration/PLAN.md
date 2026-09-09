# Migration Plan — Java Pet Store 1.1.2 → BCE / MicroProfile

Produced by `/migration-advisor` on 2026-09-09. This is a recommendation with its
evidence shown, not a verdict — edit before executing step one. Every step below
is invoked explicitly by a human and produces a reviewable artifact.

## Findings

| Signal | Value | Source | Caveats |
|---|---|---|---|
| Platform | J2EE 1.2/1.3 BluePrints: EJB 1.1 (`SessionBean`/`EntityBean` via wildcard imports + `*_ejb.xml` descriptors), JSP/servlet front-controller MVC, `javax.*` throughout, Ant build | repo probe (`src/**/build.xml`, `*_ejb.xml`, imports) | — |
| Size | 233 Java files, ~18.6k LOC, 88 JSPs; 3 EARs (`petstore`, `petstoreadmin`, `mailerapp`) | `find`/`wc` | — |
| Shape | 7 components: customer (48 files), personalization (20), shoppingcart (19), signon (18), inventory (14), mail (7), util (1); plus `petstore` web tier (`control/ejb`, `control/event`, `control/web/handlers`, taglibs) and admin | `src/components/`, package listing | Sun's "components" are a head start on BC boundaries, but they are technical J2EE components, not verified business components — `/bc-carver` still decides |
| Overengineering density | 48/233 files (~21%) named `*Manager`/`*Impl`/`*Util`/`*Facade`/`*Helper`/`*Handler`; event/handler indirection in the web tier | filename grep | High — the concept pipeline pays off here; the domain vocabulary is buried under J2EE-era pattern names |
| Tests | **Zero** test sources in the legacy tree | `find -ipath '*test*'` | A behavior bar must be built before any restructuring; "1:1 lift" of untested code is high-risk |
| Git history | 8 commits, all 2026, teaching-workspace commits ("Workspace prepared", "initial catalog BC creation") | `git log` | **Original Sun history absent.** Churn, hotspot, temporal-coupling, and bus-factor signals are unavailable; this plan is more heuristic as a result |
| Running instance | None. `:8000` refused; `:8080` answers but `/petstore` → 404 (unrelated server) | `curl` | J2EE RI 1.3 will not start on a modern JDK — revival needs a period-correct JDK (1.3/1.4) in a container |
| ~~Migration already in flight~~ | Sibling `aldi-eshop` project exists in the workspace | workspace listing, git log | **Ignored per directive (2026-09-09): aldi-eshop is not part of this migration.** All BCs, catalog included, are carved fresh from the Pet Store vocabulary |

## Interview answers (2026-09-09)

- **Continuity:** legacy need not keep running (it doesn't run anyway) → no strangler/rehost needed.
- **Goal:** full BCE rearchitect, converging to specs (`/sbce` + `microprofile-server`).
- **Experts:** a live domain expert is available → `/concept-clarifier` can run interactively.
- **Behavior capture:** **revive the legacy app first** and record real behavior, rather than relying on static reading or docs alone.

## Migration target

`/Users/abien/work/workspaces/airhacks/workspace/modern-jps` (set 2026-09-09) —
a Quarkus/MicroProfile BCE template: `service` (base package `airhacks.qmp`,
sample `greetings` BC to be replaced) + `service-st` (system tests via
MicroProfile REST Client). sbce-ready: one capability spec ≡ one BC, spec lives
in the BC's `package-info.java`. Each carved BC from `CARVING.md` becomes a
package `airhacks.qmp.<bc>` with boundary/control/entity, its requirements
tested in `service-st`. Dependency policy: MicroProfile/Java SE only — ask
before adding any dependency (AGENTS.md).

## Recommended path: **Rearchitect** (concept-first, revival before convergence)

Reasoning: no continuity constraint rules out rehost-as-goal; the stated goal
(full BCE) rules out replatform-as-terminal. The 21% pattern-name
density and buried domain vocabulary are exactly the profile where the concept
pipeline (extract → clarify → carve) beats direct rewriting, and it needs no runtime —
so it leads. The zero-test finding plus the "revive first" decision still require a
containerized resurrection and behavior recording, but as the gate before spec
convergence (step 6), not before the vocabulary work — that recording remains the
only non-heuristic behavior bar available, since git history and telemetry are gone.

## Sequence

Each step links to its own skill for detail; decision points needing a human call are flagged ⚑.

Resequenced 2026-09-09: start with concept extraction (static, no runtime needed);
revival and characterization move behind the carve, before spec convergence — they
gate step 6, not the vocabulary work.

1. **`/concept-extractor`** — ✅ **done** (`migration/concepts.md`): mined the domain
   vocabulary out of the `Manager/Handler/Impl` noise, seeded by the events in
   `control/event`. Artifact: 10 candidate concepts + noise catalog + carver notes.

2. **`/concept-clarifier`** — ✅ **done** (2026-09-09, expert session; decisions folded
   into `migration/concepts.md`): sign-on → external IdP; order lifecycle
   `pending → shipped` (no approval); inventory standalone with reservation at order
   creation; personalization own BC; cart persisted per customer; back-office view +
   modern export kept; express order kept; notification = async adapter.
   One question deferred to step 5: UPDATE/DELETE_ORDER reachability.
   Canonical terms + decisions materialized as `migration/GLOSSARY.md` (ground truth);
   `concepts.md` keeps the evidence trails.

3. **`/bc-carver`** — ✅ **done** (2026-09-09, `migration/CARVING.md`): seven BCs —
   catalog, customers, cart, orders, inventory, fulfillment, personalization — with
   acyclic uses-graph, package→BC diff, mandatory unmapped list (signon → IdP,
   MVC plumbing, mailer → notification adapter), and open questions Q1–Q3.
   ⚑ Human call still pending: confirm the cut before /sbce consumes it.

4. **Revive the legacy app in a container** *(per interview — not a skill, a one-off task)*
   Period-correct JDK (1.3/1.4) + J2EE RI 1.3 (or Ant `build.sh` against it) in Docker;
   deploy `petstore.ear` + Cloudscape data. Artifact: a `Dockerfile`/compose file and a
   reachable `http://localhost:<port>/petstore`.
   ⚑ Time-box this. If revival exceeds the box, fall back to static capture + expert
   interview (the interview chose revival, but it is a means, not the goal).
   May run in parallel with steps 2–3 — nothing before step 5 depends on it.

5. **`/characterization-tests record`** — exercise the revived instance and record behavior
   *per carved BC slice*: browse/search catalog, cart lifecycle, signon, checkout/order,
   mailer side effects. Concentrate on the Order/Checkout and Cart slices (largest;
   no churn data exists to target hotspots). Artifact: recorded behavior corpus,
   the green bar for step 6.

6. **`/sbce new <bc>` → `/sbce apply`** per carved BC, executed **in `modern-jps`**
   (base package renamed `airhacks.qmp` → `airhacks.petstore`, 2026-09-09;
   catalog spec declared — awaiting `/sbce apply catalog`),
   composed with `microprofile-server` (+ `ears-tests` to turn spec requirements into
   system tests in `service-st`). Each BC: spec in `airhacks.qmp.<bc>/package-info.java`,
   boundary/control/entity package, requirement-traced tests. Converge until the test
   loop and the characterization corpus for its slice are green. Remove the template's
   `greetings` sample with the first real BC.
   ⚑ Human call: BC ordering. Suggested: catalog → shopping-cart → customer-account →
   order → inventory → fulfillment → personalization.

7. **Retire the legacy tree** once every recorded behavior maps to a green BC system test.
   The revived container remains as an oracle until then.

### Skippable

- **Rehost / replatform passes** — skipped: no continuity requirement, and the J2EE RI
  target makes a 1:1 lift cost more than the rearchitect it would precede.
- **Git-driven hotspot targeting** — impossible: original history absent.

## Distrust notes

- All churn/coupling/bus-factor reasoning is **unavailable**, not merely weak — do not
  let anyone re-add it from the teaching repo's 8 commits.
- Sun's component folders look like BCs but encode J2EE packaging, not domain
  boundaries. Treat them as hypotheses for step 3 (confirmed twice during
  extraction: Catalog inside `shoppingcart`, Order inside `customer`).

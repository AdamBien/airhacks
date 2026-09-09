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
| Migration already in flight | Sibling `../../aldi-eshop` with `service/` + `service-st/` (MicroProfile layout, system tests incl. `SystemInvariantsIT`); commit "initial catalog BC creation" | workspace listing, git log | The catalog BC is the template: new BCs should match its structure and test conventions |

## Interview answers (2026-09-09)

- **Continuity:** legacy need not keep running (it doesn't run anyway) → no strangler/rehost needed.
- **Goal:** full BCE rearchitect, converging to specs (`/sbce` + `microprofile-server`), continuing the aldi-eshop pattern.
- **Experts:** a live domain expert is available → `/concept-clarifier` can run interactively.
- **Behavior capture:** **revive the legacy app first** and record real behavior, rather than relying on static reading or docs alone.

## Recommended path: **Rearchitect** (with a revival prelude)

Reasoning: no continuity constraint rules out rehost-as-goal; the stated goal and
the in-flight catalog BC rule out replatform-as-terminal. The 21% pattern-name
density and buried domain vocabulary are exactly the profile where the concept
pipeline (extract → clarify → carve) beats direct rewriting. The zero-test finding
plus the "revive first" decision put a containerized resurrection and behavior
recording ahead of everything else — that is the only non-heuristic behavior bar
available, since git history and production telemetry are gone.

## Sequence

Each step links to its own skill for detail; decision points needing a human call are flagged ⚑.

1. **Revive the legacy app in a container** *(prelude, per interview — not a skill, a one-off task)*
   Period-correct JDK (1.3/1.4) + J2EE RI 1.3 (or Ant `build.sh` against it) in Docker;
   deploy `petstore.ear` + Cloudscape data. Artifact: a `Dockerfile`/compose file and a
   reachable `http://localhost:<port>/petstore`.
   ⚑ Time-box this. If revival exceeds the box, fall back to static capture + expert
   interview (the interview chose revival, but it is a means, not the goal).

2. **`/characterization-tests record`** — exercise the revived instance and record behavior:
   browse/search catalog, cart lifecycle, signon, checkout/order, mailer side effects.
   Concentrate on customer + shoppingcart (largest components; no churn data exists to
   target hotspots). Artifact: recorded behavior corpus, the green bar for all later steps.

3. **`/concept-extractor`** — mine the domain vocabulary out of the `Manager/Handler/Impl`
   noise (events in `control/event` are a good seed: they name business intents).
   Artifact: candidate concept list.

4. **`/concept-clarifier`** — live sessions with the domain expert to confirm/rename/merge
   concepts. Artifact: clarified vocabulary.

5. **`/bc-carver`** — carve business components. Sun's 7 technical components are input
   candidates, not answers; catalog is already carved (aldi-eshop) and serves as the
   size/shape template.
   ⚑ Human call: BC list and boundaries; whether `petstoreadmin` and `mailerapp` become
   BCs, fold into others, or are dropped for the workshop scope.

6. **`/sbce new <bc>` → `/sbce apply`** per carved BC, composed with `microprofile-server`
   (+ `ears-tests` to turn spec requirements into system tests in `<bc>-st`, mirroring
   `service-st`). Converge each BC until its system tests and the characterization
   corpus for its slice are green. Artifact per BC: spec + service + `-st` module.
   ⚑ Human call: BC ordering. Suggested: signon → catalog(done) → shoppingcart →
   customer → order/mail.

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
  boundaries. Treat them as hypotheses for step 5.

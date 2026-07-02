/**
 * Generic, application-wide liveness and readiness probes for container
 * orchestration. Holds only checks that have no business-component owner;
 * BC-specific health checks live in the respective BC's boundary package,
 * close to the code whose operational invariants they assert.
 */
package airhacks.qmp.health;

/**
 * MicroProfile REST client interfaces for system testing.
 * <p>
 * This package contains only test-independent client code. The client interfaces mirror
 * the service module's package structure to maintain clear correspondence with the resources
 * under test and simplify testing setup.
 * <p>
 * Client interfaces are named after their corresponding resources with the "Client" suffix.
 * <p>
 * All MicroProfile REST client interfaces in this source directory (src/main/java) are packaged
 * into a jar and can be reused as clients by other services.
 */
package airhacks.qmp.health;

/**
 * Root package of the {@code java-cli-app} — a zero-dependency Java 25 CLI
 * application built and packaged with zb.
 * <p>
 * {@link airhacks.App} is the boundary: it wires the feature business components
 * together and prints their output. Each direct sub-package is a business
 * component named after its domain responsibility, layered internally into
 * {@code boundary}, {@code control}, and {@code entity} as needed.
 *
 * @see airhacks.greeting
 */
package airhacks;

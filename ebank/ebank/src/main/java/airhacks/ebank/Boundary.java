package airhacks.ebank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Stereotype;
import jakarta.transaction.Transactional;

/**
 * Marks coarse-grained entry points that expose application functionality
 * and manage transaction boundaries. Fully optional.
 *
 * @see <a href="https://bce.design">BCE Pattern</a>
 */
@ApplicationScoped
@Transactional
@Stereotype
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Boundary {}

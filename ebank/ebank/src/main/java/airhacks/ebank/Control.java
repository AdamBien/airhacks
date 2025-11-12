package airhacks.ebank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.enterprise.inject.Stereotype;

/**
 * Marks components implementing procedural business logic and workflows.
 * Fully optional.
 *
 * @see <a href="https://bce.design">BCE Pattern</a>
 */
@Stereotype
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Control {}

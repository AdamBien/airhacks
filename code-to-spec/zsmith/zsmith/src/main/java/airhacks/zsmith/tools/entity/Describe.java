package airhacks.zsmith.tools.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(RECORD_COMPONENT)
public @interface Describe {

    String value();
}

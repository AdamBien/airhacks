package ro.tuica.distillery.catalog.control;

import java.time.Duration;
import jakarta.enterprise.context.ApplicationScoped;
import ro.tuica.distillery.catalog.entity.Tuica;

@ApplicationScoped
public class TuicaQualityManagement {

    static final System.Logger LOGGER = System.getLogger(TuicaQualityManagement.class.getName());

    public boolean taste(Tuica tuica) {
        try {
            Thread.sleep(Duration.ofSeconds(2));
        } catch (InterruptedException ex) {
            LOGGER.log(System.Logger.Level.ERROR, "Tasting interrupted", ex);
        }
        return true;
    }
}

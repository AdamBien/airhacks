package ro.tuica.distillery.business.catalog.control;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import ro.tuica.distillery.business.catalog.entity.Tuica;

/**
 *
 * @author airhacks.com
 */
public class TuicaQualityManagement {

    public boolean taste(Tuica tuica) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TuicaQualityManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

}

package ro.tuica.distillery.distillery.boundary;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.scheduler.Scheduled;
import ro.tuica.distillery.distillery.control.Distillator;

@ApplicationScoped
public class Burner {

    static final System.Logger LOGGER = System.getLogger(Burner.class.getName());

    @Inject
    Distillator distillator;

    @Scheduled(every = "5s")
    public void burn() {
        distillator.doSomethingExpensive();
        LOGGER.log(System.Logger.Level.INFO, "New Palinca!");
    }
}

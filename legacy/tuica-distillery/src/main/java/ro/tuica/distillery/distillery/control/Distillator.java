package ro.tuica.distillery.distillery.control;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Distillator {

    static final System.Logger LOGGER = System.getLogger(Distillator.class.getName());

    public void doSomethingExpensive() {
        LOGGER.log(System.Logger.Level.INFO, "Done!");
    }
}

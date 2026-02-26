package ro.tuica.distillery.catalog.boundary;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import ro.tuica.distillery.authenticator.entity.TuicaPrincipal;
import ro.tuica.distillery.catalog.control.TuicaQualityManagement;
import ro.tuica.distillery.catalog.entity.Tuica;

@ApplicationScoped
public class TuicaStore {

    static final System.Logger LOGGER = System.getLogger(TuicaStore.class.getName());

    @Inject
    TuicaQualityManagement tqm;

    @Inject
    EntityManager em;

    @Inject
    TuicaPrincipal tp;

    public void store(Tuica tuica) {
        LOGGER.log(System.Logger.Level.INFO, "Principal is: {0}", tp);
        LOGGER.log(System.Logger.Level.INFO, "stored: {0}", tuica);
        em.merge(tuica);
    }

    public List<Tuica> all() {
        return em.createNamedQuery("all", Tuica.class).getResultList();
    }

    public List<Tuica> strong() {
        return all().parallelStream()
                .filter(t -> t.strength() > 15)
                .toList();
    }
}

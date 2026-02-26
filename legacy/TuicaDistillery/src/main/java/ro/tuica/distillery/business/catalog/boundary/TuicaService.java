package ro.tuica.distillery.business.catalog.boundary;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ro.tuica.distillery.business.authenticator.entity.TuicaPrincipal;
import ro.tuica.distillery.business.catalog.control.TuicaQualityManagement;
import ro.tuica.distillery.business.catalog.entity.Strength;
import ro.tuica.distillery.business.catalog.entity.Tuica;
import ro.tuica.distillery.business.monitoring.boundary.PerformanceLogger;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Interceptors(PerformanceLogger.class)
public class TuicaService {

    @Inject
    TuicaQualityManagement tqm;

    @PersistenceContext
    EntityManager em;

    @Resource
    ManagedExecutorService mes;

    @Inject
    TuicaPrincipal tp;

    public void store(@Strength Tuica tuica) {
        System.out.println("Principal is: " + tp);
        System.out.println("stored: " + tuica);
        em.merge(tuica);
    }

    public List<Tuica> all() {
        return em.createNamedQuery("all", Tuica.class).getResultList();
    }

    public List<Tuica> strong() {
        return all().parallelStream().
                filter(t -> t.getStrength() > 15).
                collect(Collectors.toList());

    }
;
}

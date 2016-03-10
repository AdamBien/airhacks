package com.airhacks.launch.services;

import com.airhacks.launch.entities.Steak;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author airhacks.com
 */
@Stateless
@Interceptors(Audit.class)
public class SteakService {

    @Inject
    Grill grill;

    @PersistenceContext
    EntityManager em;

    public SteakService() {
        System.out.println("--- don't use constructors");
    }

    @PostConstruct
    public void initialize() {
        System.out.println("-- fully initialized " + grill.getClass().getName());
    }

    public List<Steak> steaks() {
        return this.em.createNamedQuery(Steak.findAll, Steak.class).
                getResultList();
    }

    public Steak save(Steak s) {
        return this.em.merge(s);
    }

}

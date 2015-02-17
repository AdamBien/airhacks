package com.airhacks.testing.workshops.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class WorkshopIT {

    private EntityManager em;
    private EntityTransaction tx;

    @Before
    public void initEM() {
        this.em = Persistence.createEntityManagerFactory("it").
                createEntityManager();
        this.tx = this.em.getTransaction();
    }

    @Test
    public void crud() {
        this.tx.begin();
        final String key = "testing";
        this.em.persist(new Workshop(key));
        this.tx.commit();

        Workshop found = this.em.find(Workshop.class, key);
        assertNotNull(found);
    }

}

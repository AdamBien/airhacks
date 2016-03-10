package com.airhacks.plainjpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.AttributeNode;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author adam-bien.com
 */
public class SpaceshipTest {

    EntityManager em;
    EntityTransaction et;

    @Before
    public void instantiate() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("demo");
        this.em = emf.createEntityManager();
        this.et = this.em.getTransaction();
        createSpaceship();
    }

    @Test
    public void namedEntityGraph() {

        EntityGraph<?> withEnginesGraph = this.em.getEntityGraph("withEngines");
        Assert.assertNotNull(withEnginesGraph);
        List<AttributeNode<?>> nodes = withEnginesGraph.getAttributeNodes();
        for (AttributeNode<?> node : nodes) {
            Assert.assertNotNull(node);
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph ", withEnginesGraph);
        Spaceship found = this.em.find(Spaceship.class, "nepomuk", properties);
        assertNotNull(found);
    }

    @Test
    public void dynamicEntityGraph() {
        EntityGraph dynamic = em.createEntityGraph(Spaceship.class);
        dynamic.addAttributeNodes("engines");

        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph ", dynamic);
        Spaceship found = this.em.find(Spaceship.class, "nepomuk", properties);
        assertNotNull(found);
    }

    private void createSpaceship() {
        this.et.begin();
        this.em.merge(new Spaceship("nepomuk", 42));
        this.et.commit();
    }
}

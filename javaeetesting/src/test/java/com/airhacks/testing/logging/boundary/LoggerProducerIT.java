package com.airhacks.testing.logging.boundary;

import javax.inject.Inject;
import static org.hamcrest.CoreMatchers.is;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author airhacks.com
 */
@RunWith(Arquillian.class)
public class LoggerProducerIT {

    @Inject
    LoggerInjectionVerifier cut;

    @Deployment
    public static JavaArchive create() {
        return ShrinkWrap.create(JavaArchive.class).
                addClasses(LoggerProducer.class,
                        LoggerInjectionVerifier.class).
                addAsManifestResource(EmptyAsset.INSTANCE,
                        "beans.xml");
    }

    @Test
    public void loggerProperlyConfigured() {
        String loggerName = this.cut.getClass().getName();
        assertThat(loggerName, is(cut.getLogger().getName()));
    }

}

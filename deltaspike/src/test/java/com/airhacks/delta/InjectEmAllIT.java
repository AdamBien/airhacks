package com.airhacks.delta;

import javax.inject.Inject;
import static junit.framework.Assert.assertNotNull;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author airhacks.com
 */
@RunWith(CdiTestRunner.class)
public class InjectEmAllIT {

    @Inject
    Boundary boundary;

    @Test
    public void injection() {
        assertNotNull(boundary);
        assertThat(boundary.greeting(), is("perfect day"));
    }
}

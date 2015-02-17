package com.airhacks.testing.workshops.boundary;

import com.airhacks.testing.workshops.control.Booking;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author airhacks.com
 */
public class WorkshopsCatalogTest {

    WorkshopsCatalog cut;

    @Before
    public void init() {
        this.cut = new WorkshopsCatalog();
        this.cut.booking = mock(Booking.class);
    }

    @Test(expected = IllegalStateException.class)
    public void boookingWithUnsufficientAttendees() {
        when(this.cut.booking.getRegistrationNumber()).thenReturn(2);
        this.cut.startWorkshop();
    }

}

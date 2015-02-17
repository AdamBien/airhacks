package com.airhacks.testing.workshops.boundary;

import com.airhacks.testing.workshops.control.Booking;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author airhacks.com
 */
@RunWith(MockitoJUnitRunner.class)
public class WorkshopsCatalogTest {

    @InjectMocks
    WorkshopsCatalog cut;

    @Mock
    Booking booking;

    @Test(expected = IllegalStateException.class)
    public void boookingWithUnsufficientAttendees() {
        when(this.booking.getRegistrationNumber()).thenReturn(2);
        this.cut.startWorkshop();
    }

}

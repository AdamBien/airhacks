package com.airhacks.testing.workshops.boundary;

import com.airhacks.testing.workshops.control.Booking;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class WorkshopsCatalog {

    @Inject
    Booking booking;

    public String all() {
        return "testing,architecture";
    }

    public void startWorkshop() {
        if (booking.getRegistrationNumber() < 3) {
            throw new IllegalStateException("Not sufficient registraitons");
        }
        booking.payTrainer();
    }

}

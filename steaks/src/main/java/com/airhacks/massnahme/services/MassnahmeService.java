package com.airhacks.massnahme.services;

import com.airhacks.launch.entities.Steak;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;

/**
 *
 * @author airhacks.com
 */
public class MassnahmeService {

    public void onCreatedSteak(@Observes(during = TransactionPhase.AFTER_SUCCESS) Steak steak) {
        System.out.println(" steak was created = " + steak);
    }

    public void onCreationFailure(@Observes(during = TransactionPhase.AFTER_FAILURE) Steak steak) {
        System.out.println(" steak was not created = " + steak);
    }

}

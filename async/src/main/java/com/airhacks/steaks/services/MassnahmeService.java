package com.airhacks.steaks.services;

import javax.ejb.Stateless;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class MassnahmeService {

    public void protocol(String steak) {
        System.out.println("this = " + steak);
    }

}

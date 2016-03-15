package com.airhacks.insurance.boundary;

import javax.ejb.Stateless;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class DamageService {

    public String getDamage(long id) {
        return id + " severe " + System.currentTimeMillis();
    }

}

package com.airhacks;

import javax.ejb.Stateless;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class BetriebService {

    public void save() {
        System.out.println("Enterprise save");
    }

}

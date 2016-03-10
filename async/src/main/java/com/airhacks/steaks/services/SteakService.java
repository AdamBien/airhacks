package com.airhacks.steaks.services;

import javax.ejb.Stateless;

/**
 *
 * @author airhacks.com
 */
@Stateless
public class SteakService {

    public String steak() {
        return "argentina ";
    }

    public String process(String input) {
        return "input " + input;
    }

    public void save(String input) {
        System.out.println("saved = " + input);
    }

}

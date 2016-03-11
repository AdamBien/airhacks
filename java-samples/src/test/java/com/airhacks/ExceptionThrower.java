package com.airhacks;

/**
 *
 * @author airhacks.com
 */
public class ExceptionThrower {

    public void unstable() {
        throw new IllegalStateException("keine Lust");
    }

    public void unable() throws MotivationException {
        throw new MotivationException();
    }

}

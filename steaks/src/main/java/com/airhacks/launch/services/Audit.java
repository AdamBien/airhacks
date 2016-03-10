package com.airhacks.launch.services;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author airhacks.com
 */
public class Audit {

    @AroundInvoke
    public Object log(InvocationContext ic) throws Exception {
        System.out.println("-- " + ic.getMethod());
        long start = System.currentTimeMillis();
        try {
            return ic.proceed();

        } catch (Exception ex) {

            throw ex;
        } finally {
            long duration = System.currentTimeMillis() - start;
            System.out.println("duration = " + duration);
        }
    }

}

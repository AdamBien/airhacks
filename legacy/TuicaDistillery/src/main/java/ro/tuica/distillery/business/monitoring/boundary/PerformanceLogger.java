package ro.tuica.distillery.business.monitoring.boundary;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 *
 * @author airhacks.com
 */
public class PerformanceLogger {

    @AroundInvoke
    public Object measure(InvocationContext ic) throws Exception {
        try {
            return ic.proceed();
        } finally {
            System.out.println("Nice method: " + ic.getMethod());
        }
    }

}

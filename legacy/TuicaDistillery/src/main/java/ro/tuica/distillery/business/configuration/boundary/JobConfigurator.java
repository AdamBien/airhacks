package ro.tuica.distillery.business.configuration.boundary;

import javax.ejb.ScheduleExpression;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 *
 * @author airhacks.com
 */
public class JobConfigurator {

    @Produces
    public ScheduleExpression exposes(InjectionPoint ip) {
        System.out.println("Hey job: " + ip.getMember().getDeclaringClass().getName());
        ScheduleExpression se = new ScheduleExpression();
        se.minute("*").second("*/5").hour("*");
        return se;

    }

}

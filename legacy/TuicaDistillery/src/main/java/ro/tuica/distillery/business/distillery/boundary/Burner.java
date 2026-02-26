package ro.tuica.distillery.business.distillery.boundary;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import ro.tuica.distillery.business.distillery.control.Distillator;

/**
 *
 * @author airhacks.com
 */
@Startup
@Singleton
public class Burner {

    @Resource
    TimerService ts;
    private Timer timer;

    @Inject
    ScheduleExpression se;

    @Inject
    Distillator d;

    @Resource
    ManagedExecutorService mes;

    @PostConstruct
    public void initialize() {
        this.timer = ts.createCalendarTimer(se);
    }

    @Timeout
    public void burn() {
        Runnable runnable = d::doSomethingExpensive;
        mes.execute(runnable);
        System.out.println("New Palinca !" + new Date());
    }

}

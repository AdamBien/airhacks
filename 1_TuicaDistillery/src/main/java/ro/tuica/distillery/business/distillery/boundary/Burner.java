package ro.tuica.distillery.business.distillery.boundary;

import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Schedule;
import jakarta.ejb.ScheduleExpression;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerService;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.inject.Inject;
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

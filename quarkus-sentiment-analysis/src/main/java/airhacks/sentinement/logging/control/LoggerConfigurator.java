package airhacks.sentinement.logging.control;

import java.lang.System.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class LoggerConfigurator {
    
    @Produces
    public Logger configure(InjectionPoint ip) {
        var ipName = ip.getMember().getDeclaringClass().getName();
        return System.getLogger(ipName);
    }
}

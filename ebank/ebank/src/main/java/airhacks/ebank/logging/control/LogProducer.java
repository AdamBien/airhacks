package airhacks.ebank.logging.control;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class LogProducer {


    @Produces
    public EBLog create(InjectionPoint injectionPoint){
        var clazz = injectionPoint.getMember().getDeclaringClass();
        return new EBLog(clazz);
    }
}

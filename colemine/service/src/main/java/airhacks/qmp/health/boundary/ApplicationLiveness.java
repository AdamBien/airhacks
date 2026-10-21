package airhacks.qmp.health.boundary;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
@ApplicationScoped
public class ApplicationLiveness implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("application-live");
    }
}

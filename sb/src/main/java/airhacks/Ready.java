package airhacks;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Readiness
public class Ready implements HealthCheck{

	@Override
	public HealthCheckResponse call() {
        return HealthCheckResponse.up("basic");
	}
}

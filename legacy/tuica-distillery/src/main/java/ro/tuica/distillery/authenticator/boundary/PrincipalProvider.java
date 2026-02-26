package ro.tuica.distillery.authenticator.boundary;

import java.security.Principal;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import ro.tuica.distillery.authenticator.entity.TuicaPrincipal;

public class PrincipalProvider {

    @Inject
    Principal principal;

    @Produces
    public TuicaPrincipal expose() {
        if (principal == null) {
            return new TuicaPrincipal("anonymous", "none");
        }
        return new TuicaPrincipal(principal.getName(), "everything");
    }
}

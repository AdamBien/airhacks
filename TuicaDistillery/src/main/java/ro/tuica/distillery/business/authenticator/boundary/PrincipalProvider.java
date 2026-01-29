package ro.tuica.distillery.business.authenticator.boundary;

import java.security.Principal;
import javax.enterprise.inject.Produces;
import ro.tuica.distillery.business.authenticator.entity.TuicaPrincipal;

/**
 *
 * @author airhacks.com
 */
public class PrincipalProvider {

    @Produces
    public TuicaPrincipal expose(Principal principal) {
        String name = principal.getName();
        return new TuicaPrincipal(name, "everything");
    }

}

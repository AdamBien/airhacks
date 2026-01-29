package ro.tuica.distillery.business.authenticator.entity;

/**
 *
 * @author airhacks.com
 */
public class TuicaPrincipal {

    private String name;
    private String rights;

    public TuicaPrincipal(String name, String rights) {
        this.name = name;
        this.rights = rights;
    }

    @Override
    public String toString() {
        return "TuicaPrincipal{" + "name=" + name + ", rights=" + rights + '}';
    }

}

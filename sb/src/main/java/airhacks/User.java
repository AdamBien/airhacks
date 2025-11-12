package airhacks;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class User {

    String name;

    @PostConstruct
    public void init() {
        System.out.println(this.getClass() + " init ");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

}

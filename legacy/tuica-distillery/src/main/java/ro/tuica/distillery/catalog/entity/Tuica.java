package ro.tuica.distillery.catalog.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = "all", query = "SELECT t FROM Tuica t")
public class Tuica {

    @Id
    @GeneratedValue
    long id;

    String name;
    int strength;

    public Tuica(String name, int strength) {
        this.name = name;
        this.strength = strength;
    }

    public Tuica() {
    }

    public String name() {
        return name;
    }

    public int strength() {
        return strength;
    }

    public boolean isValid() {
        return strength > 10;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("name", name)
                .add("strength", strength)
                .build();
    }

    public static Tuica fromJSON(JsonObject json) {
        return new Tuica(json.getString("name"), json.getInt("strength"));
    }

    @Override
    public String toString() {
        return "Tuica{" + "name=" + name + ", strength=" + strength + '}';
    }
}

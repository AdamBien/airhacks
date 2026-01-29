package ro.tuica.distillery.business.catalog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.Min;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author airhacks.com
 */
@Entity
@NamedQuery(name = "all", query = "SELECT t FROM Tuica t")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Tuica {

    @Id
    @GeneratedValue
    private long id;

    @XmlElement(name = "identifier")
    private String name;
    private int strength;

    public Tuica(String name, int strength) {
        this.name = name;
        this.strength = strength;
    }

    public Tuica() {
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    public boolean isValid() {
        return strength > 10;
    }

    @Override
    public String toString() {
        return "Tuica{" + "name=" + name + ", strength=" + strength + '}';
    }

}

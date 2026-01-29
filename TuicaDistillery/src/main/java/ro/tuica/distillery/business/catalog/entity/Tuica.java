package ro.tuica.distillery.business.catalog.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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

/*
 * $Id: Address.java,v 1.3.4.2 2001/03/15 00:47:22 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.util;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 * This class encapsulates address information. This
 * class is meant to be immutable so it has no
 * mutator methods.
 */

public class Address implements java.io.Serializable {

    private String streetName1;
    private String streetName2;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    public Address (String streetName1, String streetName2,String city, String state,String zipCode, String country){
                this.streetName1 = streetName1;
                this.streetName2 = streetName2;
                this.city = city;
                this.state = state;
                this.zipCode = zipCode;
                this.country = country;
    }

    public Address(){}

    public Object clone(){
        return new  Address (streetName1, streetName2, city, state, zipCode,  country);
    }

    // get methods for the instance variables

    public String getStreetName1() {
        return streetName1;
    }

    public String getStreetName2() {
                // do not return a null for presentation
                if (streetName2 == null) return "";
                else return streetName2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }


    public String getCountry(){
        return country;
    }

    public String toString(){
        return "[ Address 1=" + streetName1 + ", Address 2=" + streetName2 +
                ", City="  + city + ", State/Province=" + state + ", Postal Code=" + zipCode + ", country=" + country + "]";
    }

    public Element toXml(Document doc, String id) {
        Element root = doc.createElement("Address");
        if (id != null)
            root.setAttribute("Id", id);

        Element node = doc.createElement("StreetName1");
        node.appendChild(doc.createTextNode(streetName1));
        root.appendChild(node);

        node  = doc.createElement("StreetName2");
        node.appendChild(doc.createTextNode(streetName2));
        root.appendChild(node);

        node = doc.createElement("City");
        node.appendChild(doc.createTextNode(city));
        root.appendChild(node);

        node = doc.createElement("State");
        node.appendChild(doc.createTextNode(state));
        root.appendChild(node);

        node = doc.createElement("ZipCode");
        node.appendChild(doc.createTextNode(zipCode));
        root.appendChild(node);

        return root;
    }

}

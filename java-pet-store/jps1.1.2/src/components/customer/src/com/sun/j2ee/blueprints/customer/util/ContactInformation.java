/*
 * $Id: ContactInformation.java,v 1.3.4.2 2001/03/15 00:47:23 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.util;

import com.sun.j2ee.blueprints.customer.util.Address;
import java.io.Serializable;

/**
 * This class represents all the data needed to
 * identify an indvidual and contact that individual.
 * This class is meant to be immutable.
 */
public class ContactInformation implements Serializable{

    private String telephone;
    private String email;
    private Address address;
    private String familyName;
    private String givenName;

    /**
     * Default Constructor
     */
    public ContactInformation(String familyName,
                String givenName,
                String telephone,
                String email,
                Address address){

        this.givenName = givenName;
        this.familyName = familyName;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
    }

    /**
     * Class constructor with no arguments, used by the web tier.
     */
    public ContactInformation() {}

    public String getGivenName(){
        return givenName;
    }

    public String getFamilyName(){
        return familyName;
    }

    public String getEMail(){
        return email;
    }

    public Address getAddress(){
        return address;
    }
    public String getTelephone(){
        return telephone;
    }

    public String toString(){
        return "[familyName=" + familyName + ", givenName=" +
            givenName + ", telephone=" + telephone + ", email=" +
            email + ",  address=" + address+ "]";
    }

}

/*
 * $Id: SignOnModel.java,v 1.1.2.6 2001/03/05 13:07:08 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.model;


/**
 * This class represents the model data for the
 * sign on info. Note that this object is immutable
 * since it is intended to be read only.
 */
public class SignOnModel implements java.io.Serializable {

    public String userName;
    public String password;

    public SignOnModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return password;
    }
}

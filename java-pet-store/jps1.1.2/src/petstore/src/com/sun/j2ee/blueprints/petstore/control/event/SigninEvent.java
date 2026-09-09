/*
 * $Id: SigninEvent.java,v 1.1.2.3 2001/03/15 00:40:00 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.event;

/**
 * This event is sent from the web tier to the EJB Controller to
 * notify the EJB Controller that a user has logged into the
 * application.
 */
public class SigninEvent extends EStoreEventSupport {

    private String userName;
    private String password;

    public SigninEvent(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEventName() {
        return "java:comp/env/event/SigninEvent";
    }

    public String toString() {
        return "SigninEvent: userName=" + userName + ", password=" + password;
    }

}

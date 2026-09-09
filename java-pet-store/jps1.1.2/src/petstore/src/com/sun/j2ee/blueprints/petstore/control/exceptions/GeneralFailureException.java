/*
 * $Id: GeneralFailureException.java,v 1.1.2.2 2001/03/15 00:40:01 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.exceptions;

/**
 * This exception is the base class for all the web runtime exceptions.
 */
public class GeneralFailureException extends RuntimeException
    implements java.io.Serializable {

    private Throwable t;

    public GeneralFailureException(String s) {
        super(s);
    }

    public GeneralFailureException(String s, Throwable t) {
        super(s);
        this.t = t;
    }

    public String getThrowable() {
        return ("Received throwable with Message: "+ t.getMessage());
    }
}

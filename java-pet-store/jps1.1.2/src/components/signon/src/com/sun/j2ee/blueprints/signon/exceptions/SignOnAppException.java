/*
 * $Id: SignOnAppException.java,v 1.1.2.2 2001/03/01 11:01:23 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.exceptions;

/**
 * SignOnAppException is an exception that extends the
 * standard Exception. This is thrown by the the signon
 * component when there is some failure because of user error
 */
public class SignOnAppException extends Exception {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public SignOnAppException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public SignOnAppException () {
        super();
    }

}

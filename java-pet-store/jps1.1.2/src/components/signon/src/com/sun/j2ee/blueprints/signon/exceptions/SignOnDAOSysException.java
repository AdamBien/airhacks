/*
 * $Id: SignOnDAOSysException.java,v 1.1.2.2 2001/03/01 11:01:23 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.exceptions;

import java.lang.RuntimeException;

/**
 * SignOnDAOSysException is an exception that extends the standard
 * RunTimeException Exception. This is thrown by the DAOs of the signon
 * component when there is some irrecoverable error (like SQLException)
 */
public class SignOnDAOSysException extends RuntimeException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public SignOnDAOSysException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public SignOnDAOSysException () {
        super();
    }

}

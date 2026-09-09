/*
 * $Id: SignOnDAODupKeyException.java,v 1.1.2.2 2001/03/01 11:01:23 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.exceptions;

/**
 * SignOnDAODupKeyException is an exception that extends the
 * SignOnDAOAppException. This is thrown by the DAOs of the signon
 * component when a row is already found with a given primary key
 */
public class SignOnDAODupKeyException extends SignOnDAOAppException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public SignOnDAODupKeyException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public SignOnDAODupKeyException () {
        super();
    }

}

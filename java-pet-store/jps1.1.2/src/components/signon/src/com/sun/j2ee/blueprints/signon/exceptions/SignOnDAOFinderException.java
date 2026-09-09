/*
 * $Id: SignOnDAOFinderException.java,v 1.1.2.2 2001/03/01 11:01:23 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.exceptions;

/**
 * SignOnDAOFinderException is an exception that extends the
 * SignOnDAOAppException. This is thrown by the DAOs of the signon
 * component when there is no row corresponding to a primary key
 */
public class SignOnDAOFinderException extends SignOnDAOAppException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public SignOnDAOFinderException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public SignOnDAOFinderException () {
        super();
    }

}

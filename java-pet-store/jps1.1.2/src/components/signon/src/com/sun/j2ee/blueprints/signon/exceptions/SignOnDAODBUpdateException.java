/*
 * $Id: SignOnDAODBUpdateException.java,v 1.1.2.1 2001/03/01 11:18:25 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.exceptions;

/**
 * SignOnDAODBUpdateException is an exception that extends the
 * SignOnDAOAppException. This is thrown by the DAOs of the signon
 * component when there is an error while writing/updating databases
 */
public class SignOnDAODBUpdateException extends SignOnDAOAppException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public SignOnDAODBUpdateException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public SignOnDAODBUpdateException () {
        super();
    }

}

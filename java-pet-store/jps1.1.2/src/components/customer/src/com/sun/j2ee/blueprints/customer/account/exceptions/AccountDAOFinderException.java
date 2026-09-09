/*
 * $Id: AccountDAOFinderException.java,v 1.1.2.1 2001/03/02 12:18:13 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.account.exceptions;

/**
 * AccountDAOFinderException is an exception that extends the
 * AccountDAOAppException. This is thrown by the DAOs of the account
 * component when there is no row corresponding to a primary key
 */
public class AccountDAOFinderException extends AccountDAOAppException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public AccountDAOFinderException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public AccountDAOFinderException () {
        super();
    }

}

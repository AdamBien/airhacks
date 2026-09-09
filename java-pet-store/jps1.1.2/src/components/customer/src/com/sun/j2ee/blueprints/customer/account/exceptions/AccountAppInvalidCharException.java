/*
 * $Id: AccountAppInvalidCharException.java,v 1.1.2.1 2001/03/14 07:33:05 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.account.exceptions;

/**
 * AccountAppInvalidCharException is an exception that extends the
 * AccountAppException. This is thrown by the the account
 * component when there is some failure because of user error
 */
public class AccountAppInvalidCharException extends AccountAppException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public AccountAppInvalidCharException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public AccountAppInvalidCharException () {
        super();
    }

}

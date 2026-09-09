/*
 * $Id: CustomerAppException.java,v 1.1.2.1 2001/03/05 10:42:18 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.customer.exceptions;

/**
 * CustomerAppException is an exception that extends the
 * standard Exception. This is thrown by the the customer
 * component when there is some failure because of user error
 */
public class CustomerAppException extends Exception {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public CustomerAppException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public CustomerAppException () {
        super();
    }

}

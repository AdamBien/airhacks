/*
 * $Id: OrderDAOSysException.java,v 1.1.2.1 2001/03/02 12:18:18 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.exceptions;

import java.lang.RuntimeException;

/**
 * OrderDAOSysException is an exception that extends the standard
 * RunTimeException Exception. This is thrown by the DAOs of the account
 * component when there is some irrecoverable error (like SQLException)
 */
public class OrderDAOSysException extends RuntimeException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public OrderDAOSysException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public OrderDAOSysException () {
        super();
    }

}

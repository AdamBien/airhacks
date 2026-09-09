/*
 * $Id: OrderDAODupKeyException.java,v 1.1.2.1 2001/03/02 12:18:18 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.exceptions;

/**
 * OrderDAODupKeyException is an exception that extends the
 * OrderDAOAppException. This is thrown by the DAOs of the order
 * component when a row is already found with a given primary key
 */
public class OrderDAODupKeyException extends OrderDAOAppException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public OrderDAODupKeyException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public OrderDAODupKeyException () {
        super();
    }

}

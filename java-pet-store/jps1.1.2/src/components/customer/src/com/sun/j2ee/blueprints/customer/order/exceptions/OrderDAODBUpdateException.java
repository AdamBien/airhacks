/*
 * $Id: OrderDAODBUpdateException.java,v 1.1.2.1 2001/03/02 12:18:17 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.exceptions;

/**
 * OrderDAODBUpdateException is an exception that extends the
 * OrderDAOAppException. This is thrown by the DAOs of the order
 * component when there is an error while writing/updating databases
 */
public class OrderDAODBUpdateException extends OrderDAOAppException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public OrderDAODBUpdateException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public OrderDAODBUpdateException () {
        super();
    }

}

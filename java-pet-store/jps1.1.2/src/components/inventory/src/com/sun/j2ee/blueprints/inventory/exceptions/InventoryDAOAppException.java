/*
 * $Id: InventoryDAOAppException.java,v 1.1.2.2 2001/03/01 07:04:13 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.inventory.exceptions;

/**
 * InventoryDAOAppException is an exception that extends the
 * standard Exception. This is thrown by the DAOs of the inventory
 * component when there is some failure because of user error
 */
public class InventoryDAOAppException extends Exception {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public InventoryDAOAppException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public InventoryDAOAppException () {
        super();
    }

}


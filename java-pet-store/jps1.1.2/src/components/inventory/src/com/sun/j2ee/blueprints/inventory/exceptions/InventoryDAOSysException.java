/*
 * $Id: InventoryDAOSysException.java,v 1.1.2.2 2001/03/01 07:04:13 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.inventory.exceptions;

import java.lang.RuntimeException;

/**
 * InventoryDAOSysException is an exception that extends the
 * standard RuntimeException. This is thrown by the DAOs of the inventory
 * component when there is unrecoverable failure while accessing the Database
 * like SQLException
 */
public class InventoryDAOSysException extends RuntimeException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public InventoryDAOSysException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public InventoryDAOSysException () {
        super();
    }

}


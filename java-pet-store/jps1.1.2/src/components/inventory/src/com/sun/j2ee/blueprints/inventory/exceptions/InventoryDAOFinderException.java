/*
 * $Id: InventoryDAOFinderException.java,v 1.1.2.2 2001/03/01 07:04:13 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.inventory.exceptions;

/**
 * InventoryDAOFinderException is an exception that extends the
 * InventoryDAOApp Exception. This is thrown by the DAOs of the inventory
 * component when no row with a given primary key is found in the database
 */
public class InventoryDAOFinderException extends InventoryDAOAppException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public InventoryDAOFinderException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public InventoryDAOFinderException () {
        super();
    }

}


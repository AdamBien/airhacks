/*
 * $Id: CatalogDAOSysException.java,v 1.1.2.1 2001/03/01 12:33:03 vijaysr Exp $
 * Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2000 Sun Microsystems, Inc. Tous droits réservés.
 */


package com.sun.j2ee.blueprints.shoppingcart.catalog.exceptions;

/**
 * CatalogDAOSysException is an exception that extends the standard
 * RunTimeException Exception. This is thrown by the DAOs of the catalog
 * component when there is some irrecoverable error (like SQLException)
 */

public class CatalogDAOSysException extends RuntimeException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public CatalogDAOSysException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public CatalogDAOSysException () {
        super();
    }
}


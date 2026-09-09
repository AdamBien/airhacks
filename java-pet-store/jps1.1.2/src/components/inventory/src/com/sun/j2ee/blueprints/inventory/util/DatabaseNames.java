/*
 * $Id: DatabaseNames.java,v 1.3.4.1 2001/02/28 10:58:22 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.inventory.util;

/**
 * This interface stores the name of all the database tables.
 * The String constants in this class should be used by other
 * classes instead of hardcoding the name of a database table
 * into the source code.
 */
public interface DatabaseNames {

    public static final String INVENTORY_TABLE = "inventory";

}

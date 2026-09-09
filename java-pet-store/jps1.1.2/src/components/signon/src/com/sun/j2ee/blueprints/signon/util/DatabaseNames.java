/*
 * $Id: DatabaseNames.java,v 1.1.2.4 2001/03/14 07:33:09 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.util;

/**
 * This interface stores the name of all the database tables.
 * The String constants in this class should be used by other
 * classes instead of hardcoding the name of a database table
 * into the source code.
 * The integer constants in this class can be used in the place of
 * integer constants
 */
public interface DatabaseNames {

    public static final String SIGNON_TABLE = "signon";
    public static final int MAX_USERID_LENGTH = 25;
    public static final int MAX_PASSWD_LENGTH = 25;
}

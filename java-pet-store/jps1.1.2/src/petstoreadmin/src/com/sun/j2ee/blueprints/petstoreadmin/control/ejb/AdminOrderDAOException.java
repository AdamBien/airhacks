/*
 * $Id: AdminOrderDAOException.java,v 1.1.4.1 2001/03/14 23:56:42 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstoreadmin.control.ejb;

public class AdminOrderDAOException extends Exception {

    public AdminOrderDAOException (String str) {
        super(str);
    }

    public AdminOrderDAOException () {
        super();
    }
}


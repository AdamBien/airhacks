/*
 * $Id: DuplicateAccountException.java,v 1.1.2.2 2001/03/15 00:40:00 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.exceptions;


/** Duplicate Account Exception
  * Signifys to app that someone tried to create
  * an account where the userid has already been used.
 */
public class DuplicateAccountException extends EStoreEventException
                           {

    public DuplicateAccountException (String str) {
        super(str);
    }
}


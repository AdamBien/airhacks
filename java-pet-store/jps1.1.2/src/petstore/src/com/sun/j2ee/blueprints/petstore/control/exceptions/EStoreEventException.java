/*
 * $Id: EStoreEventException.java,v 1.1.2.2 2001/03/15 00:40:01 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.exceptions;

/**
 * This exception is the base class for all the event exceptions.
 */
public class EStoreEventException extends Exception
    implements java.io.Serializable {

    public EStoreEventException() {}

    public EStoreEventException(String str) {
        super(str);
    }
}

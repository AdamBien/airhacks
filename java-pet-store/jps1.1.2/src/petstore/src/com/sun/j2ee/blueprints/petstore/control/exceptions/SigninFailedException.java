/*
 * $Id: SigninFailedException.java,v 1.1.2.3 2001/03/15 00:40:01 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.exceptions;

/**
* This exception is thrown when a user fails to propperly log into
* the application.
*/
public class SigninFailedException extends EStoreEventException  {

    public SigninFailedException (String str) {
        super(str);
    }
}

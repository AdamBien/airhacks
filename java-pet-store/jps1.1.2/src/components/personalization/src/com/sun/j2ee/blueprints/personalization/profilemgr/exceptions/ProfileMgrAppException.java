/*
 * $Id: ProfileMgrAppException.java,v 1.1.2.2 2001/03/01 07:04:14 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.exceptions;

/**
 * ProfileMgrAppException is an exception that extends the
 * standard Exception class. This is thrown by the personalization
 * component when there is some failure because of user error (like invalid
 * field)
 */
public class ProfileMgrAppException extends Exception {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public ProfileMgrAppException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public ProfileMgrAppException () {
        super();
    }

}


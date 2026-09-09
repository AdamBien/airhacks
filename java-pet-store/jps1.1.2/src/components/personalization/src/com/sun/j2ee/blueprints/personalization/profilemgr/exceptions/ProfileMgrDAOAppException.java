/*
 * $Id: ProfileMgrDAOAppException.java,v 1.1.2.2 2001/03/01 07:04:14 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.exceptions;

/**
 * ProfileMgrDAOAppException is an exception that extends the
 * standard Exception class. This is thrown by the DAOs of personalization
 * component when there is some failure because of user error (like invalid
 * field)
 */
public class ProfileMgrDAOAppException extends Exception {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public ProfileMgrDAOAppException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public ProfileMgrDAOAppException () {
        super();
    }

}


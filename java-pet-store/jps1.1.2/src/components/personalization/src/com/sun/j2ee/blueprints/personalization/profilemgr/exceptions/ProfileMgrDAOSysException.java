/*
 * $Id: ProfileMgrDAOSysException.java,v 1.1.2.2 2001/03/01 07:04:15 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.exceptions;

import java.lang.RuntimeException;

/**
 * ProfileMgrDAOSysException is an exception that extends the standrad
 * RuntimeException. This is thrown by the DAOs of personalization
 * component when there is some unrecoverable system exception (like a
 * SQLException while accessing the database)
 */

public class ProfileMgrDAOSysException extends RuntimeException {

    /**
     * Constructor
     * @param str    a string that explains what the exception condition is
     */
    public ProfileMgrDAOSysException (String str) {
        super(str);
    }

    /**
     * Default constructor. Takes no arguments
     */
    public ProfileMgrDAOSysException () {
        super();
    }

}


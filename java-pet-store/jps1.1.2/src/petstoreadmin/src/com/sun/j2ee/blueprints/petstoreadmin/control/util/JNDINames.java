/*
 * $Id: JNDINames.java,v 1.1.2.3 2001/03/14 23:56:42 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstoreadmin.control.util;

/**
 * This class is the central location to store the internal
 * JNDI names of various entities. Any change here should
 * also be reflected in the deployment descriptors.
 */
public interface JNDINames {
    public static final String ADMIN_USER_NAME =
        "java:comp/env/user/AdminId";
}

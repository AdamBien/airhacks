/*
 * $Id: DatabaseNames.java,v 1.1.2.3 2001/03/14 07:33:08 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.util;

/**
 * The DatabaseNames interface stores the name of all the database
 * tables that are accessed by the ProfileMgr.
 * The String constants in this class should be used by other
 * classes instead of hardcoding the name of a database table
 * into the source code.
 */
public interface DatabaseNames {

    public static final String PROFILE_TABLE   = "profile";
    public static final String BANNERDATA_TABLE = "bannerdata";
    public static final int MAX_USERID_LENGTH = 25;
}

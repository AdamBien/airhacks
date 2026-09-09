/*
 * $Id: EJBUtil.java,v 1.1.2.2 2001/03/13 06:57:17 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.util;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.personalization.profilemgr.ejb.ProfileMgrHome;

/**
 * The EJBUtil class is a utility class for obtaining EJB references.
 */
public final class EJBUtil {

    /**
     * Gets the ProfileMgr EJB Home interface <code>ProfileMgrHome</code>
     * interface.
     * @returns         a reference to a <code>ProfileMgrHome</code> object
     *                  created
     * @throws          <code>NamingException</code> is thrown if the
     *                  Home interface name and class could not be found
     */
    public static ProfileMgrHome getProfileMgrHome() throws NamingException {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.PROFILE_EJBHOME);
            return (ProfileMgrHome)
                PortableRemoteObject.narrow(objref, ProfileMgrHome.class);
    }
}

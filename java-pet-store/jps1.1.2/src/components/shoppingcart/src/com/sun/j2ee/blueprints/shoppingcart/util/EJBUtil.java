/*
 * $Id: EJBUtil.java,v 1.5.4.1 2001/03/01 12:33:04 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.util;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.shoppingcart.catalog.ejb.CatalogHome;

/**
 * This is a utility class for obtaining EJB references.
 */
public final class EJBUtil {


    public static CatalogHome getCatalogHome() throws Exception{
        try {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.CATALOG_EJBHOME);
            return (CatalogHome)
                PortableRemoteObject.narrow(objref, CatalogHome.class);
        } catch (NamingException ne) {
            throw new Exception(ne.getMessage());
        }
    }


}

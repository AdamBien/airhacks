/*
 * $Id: CatalogHome.java,v 1.3.4.1 2001/03/01 12:33:03 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.catalog.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/** The Home interface for Catalog */

public interface CatalogHome extends EJBHome {
    public Catalog create() throws RemoteException, CreateException;
}

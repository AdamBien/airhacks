/*
 * $Id: AdminClientControllerHome.java,v 1.2.4.2 2001/03/14 23:56:41 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstoreadmin.control.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * The Home interface for ShoppingSessionController EJB
 */
public interface AdminClientControllerHome extends EJBHome {
    public AdminClientController create()
        throws CreateException, RemoteException;
}

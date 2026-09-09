/*
 * $Id: InventoryHome.java,v 1.3.4.1 2001/02/28 09:54:48 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.inventory.ejb;


import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.EJBHome;

/**
 * The home interface of the Inventory Entity EJB.
 */

public interface InventoryHome extends EJBHome {

    public Inventory findByPrimaryKey (String itemId)
        throws RemoteException, FinderException;
}

/*
 * $Id: ShoppingCartHome.java,v 1.3.4.1 2001/03/01 12:33:01 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.cart.ejb;

import java.util.HashMap;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 *  The home interface of shopping cart EJB.
 */


public interface ShoppingCartHome extends EJBHome {
    public ShoppingCart create() throws RemoteException, CreateException;

    public ShoppingCart create(HashMap startingCart)
        throws RemoteException, CreateException;

}

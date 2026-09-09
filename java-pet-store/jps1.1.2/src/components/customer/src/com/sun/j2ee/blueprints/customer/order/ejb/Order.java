/*
 * $Id: Order.java,v 1.7.4.2 2001/03/05 10:42:18 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.ejb;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.customer.account.ejb.Account;

/**
 * This interface provides method to view the
 * details of an Order.
 */
public interface Order extends EJBObject {

    //
    // A pending order state.
    //
    public static final String PENDING = "P";


    public OrderModel getDetails() throws RemoteException;

    /**
     * @return the Account entity bean for the user
     *         who placed this order.
     */
    public Account getAccount() throws RemoteException, FinderException;
}

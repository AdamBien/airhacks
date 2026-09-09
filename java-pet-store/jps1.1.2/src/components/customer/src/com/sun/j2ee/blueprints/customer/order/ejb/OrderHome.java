/*
 * $Id: OrderHome.java,v 1.4.4.2 2001/03/09 07:39:22 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.ejb;


import java.util.Collection;
import java.util.Locale;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.EJBHome;

import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.util.CreditCard;

import com.sun.j2ee.blueprints.customer.order.exceptions.OrderAppException;

/**
 * The Home Interface for Order EJB
 */

public interface OrderHome extends EJBHome {

    public Order create(Collection lineItems, Address shipToAddr,
                        Address billToAddr,  String shipToFirstName,
                        String shipToLastName, String billToFirstName,
                        String billToLastName, CreditCard chargeCard,
                        String carrier,String userId, double totalPrice, Locale locale)
        throws RemoteException, CreateException, OrderAppException;

    public Order findByPrimaryKey(Integer orderId)
        throws RemoteException, FinderException;

    public Collection findUserOrders(String userId)
        throws RemoteException, FinderException;

}

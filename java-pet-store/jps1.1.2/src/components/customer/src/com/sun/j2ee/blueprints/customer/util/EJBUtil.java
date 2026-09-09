/*
 * $Id: EJBUtil.java,v 1.6.4.1 2001/03/15 00:47:24 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.util;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.CreateException;


import com.sun.j2ee.blueprints.customer.account.ejb.AccountHome;
import com.sun.j2ee.blueprints.customer.order.ejb.OrderHome;

/**
 * This is a utility class for obtaining EJB references.
 */
public final class EJBUtil {

    public static AccountHome getAccountHome() throws NamingException {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.ACCOUNT_EJBHOME);
            return (AccountHome)
                PortableRemoteObject.narrow(objref, AccountHome.class);
    }

    public static OrderHome getOrderHome() throws  NamingException  {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.ORDER_EJBHOME);
            return (OrderHome) PortableRemoteObject.narrow(objref, OrderHome.class);
    }
}

/*
 * $Id: EJBUtil.java,v 1.10.4.3 2001/03/15 00:40:10 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.util;

import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.CreateException;

import com.sun.j2ee.blueprints.shoppingcart.catalog.ejb.CatalogHome;
import com.sun.j2ee.blueprints.shoppingcart.cart.ejb.ShoppingCartHome;
import com.sun.j2ee.blueprints.customer.customer.ejb.CustomerHome;
import com.sun.j2ee.blueprints.customer.order.ejb.OrderHome;
import com.sun.j2ee.blueprints.inventory.ejb.InventoryHome;
import com.sun.j2ee.blueprints.mail.ejb.Mailer;
import com.sun.j2ee.blueprints.mail.ejb.MailerHome;
import com.sun.j2ee.blueprints.signon.ejb.SignOn;
import com.sun.j2ee.blueprints.signon.ejb.SignOnHome;
import com.sun.j2ee.blueprints.personalization.profilemgr.ejb.ProfileMgrHome;
import com.sun.j2ee.blueprints.petstore.control.ejb.ShoppingClientControllerHome;


/**
 * This is a utility class for obtaining EJB references.
 */
public final class EJBUtil {

    public static CustomerHome getCustomerHome() throws javax.naming.NamingException {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.CUSTOMER_EJBHOME);
            return (CustomerHome)
                PortableRemoteObject.narrow(objref, CustomerHome.class);
    }

    public static ProfileMgrHome getProfileMgrHome() throws javax.naming.NamingException {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.PROFILEMGR_EJBHOME);
            return (ProfileMgrHome)
                PortableRemoteObject.narrow(objref, ProfileMgrHome.class);
    }

    public static ShoppingClientControllerHome getSCCHome() throws javax.naming.NamingException {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.SCC_EJBHOME);
            return (ShoppingClientControllerHome)PortableRemoteObject.narrow(objref, ShoppingClientControllerHome.class);
    }

    public static InventoryHome getInventoryHome() throws javax.naming.NamingException {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.INVENTORY_EJBHOME);
            return (InventoryHome)
                PortableRemoteObject.narrow(objref, InventoryHome.class);
    }

    public static OrderHome getOrderHome() throws javax.naming.NamingException  {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.ORDER_EJBHOME);
            return (OrderHome)PortableRemoteObject.narrow(objref, OrderHome.class);
    }

    public static CatalogHome getCatalogHome() throws javax.naming.NamingException  {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.CATALOG_EJBHOME);
            return (CatalogHome)PortableRemoteObject.narrow(objref, CatalogHome.class);
    }

    public static ShoppingCartHome getShoppingCartHome() throws javax.naming.NamingException  {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.CART_EJBHOME);
            return (ShoppingCartHome)PortableRemoteObject.narrow(objref, ShoppingCartHome.class);
    }

    public static Mailer createMailerEJB()
                      throws javax.naming.NamingException,
                               CreateException,  RemoteException {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.MAILER_EJBHOME);
            MailerHome home = (MailerHome)
            PortableRemoteObject.narrow(objref, MailerHome.class);
            return (Mailer) home.create();
    }

    public static SignOnHome getSignOnHome() throws javax.naming.NamingException {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(JNDINames.SIGNON_EJBHOME);
            return (SignOnHome)
                PortableRemoteObject.narrow(objref, SignOnHome.class);
    }

}

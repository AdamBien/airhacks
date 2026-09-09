/*
 * $Id: ShoppingClientControllerWebImpl.java,v 1.13.4.7 2001/03/15 03:50:36 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */
package com.sun.j2ee.blueprints.petstore.control.web;

import java.util.Locale;
import java.util.Collection;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.JNDINames;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import com.sun.j2ee.blueprints.petstore.control.ejb.ShoppingClientControllerHome;
import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.customer.ejb.Customer;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ProfileMgrModel;
import com.sun.j2ee.blueprints.personalization.profilemgr.ejb.ProfileMgr;
import com.sun.j2ee.blueprints.shoppingcart.cart.model.ShoppingCartModel;
import com.sun.j2ee.blueprints.shoppingcart.cart.ejb.ShoppingCart;
import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;


import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;

/**
 * This class is essentially just a proxy object that calls methods on the
 * EJB tier using the com.sun.j2ee.blueprints.petstore.control.ejb.ShoppingClientControllerEJB
 * object. All the methods that access the EJB are synchronized so
 * that concurrent requests do not happen to the stateful session bean.
 *
 * @see com.sun.j2ee.blueprints.petstore.control.ejb.ShoppingClientController
 * @see com.sun.j2ee.blueprints.petstore.control.ejb.ShoppingClientControllerEJB
 * @see com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent
 */
public class ShoppingClientControllerWebImpl implements java.io.Serializable {

    private com.sun.j2ee.blueprints.petstore.control.ejb.ShoppingClientController sccEjb;
    private HttpSession session;

    public ShoppingClientControllerWebImpl() {
    }

    /**
     * constructor for an HTTP client.
     * @param the HTTP session object for a client
     */
    public ShoppingClientControllerWebImpl(HttpSession session) {
        this.session = session;
        ModelManager mm = (ModelManager)session.getAttribute(WebKeys.ModelManagerKey);
        sccEjb = mm.getSCCEJB();
    }


    public synchronized ShoppingCart getShoppingCartEJB() {
        try {
            return sccEjb.getShoppingCart();
        } catch (EStoreAppException ce) {
            throw new GeneralFailureException(ce.getMessage());
        } catch (RemoteException re) {
            throw new GeneralFailureException(re.getMessage());
        }
    }

    public synchronized Customer getCustomerEJB() {
        try {
            return sccEjb.getCustomer();
        } catch (EStoreAppException fe) {
            throw new GeneralFailureException(fe.getMessage());
        } catch (RemoteException re) {
            throw new GeneralFailureException(re.getMessage());
        }
    }

    public synchronized ProfileMgr getProfileMgrEJB() {
        try {
            return sccEjb.getProfileMgr();
        } catch (EStoreAppException fe) {
            throw new GeneralFailureException(fe.getMessage());
        } catch (RemoteException re) {
            throw new GeneralFailureException(re.getMessage());
        }
    }

    /**
     * @return the profile corresponding to the current user.
     * @exception com.sun.j2ee.blueprints.petstore.control.GeneralFailureException
     */
    public synchronized ProfileMgrModel getProfileMgr() {
        try {
            return sccEjb.getProfileMgr().getDetails();
        } catch (EStoreAppException fe) {
            throw new GeneralFailureException(fe.getMessage());
        } catch (RemoteException re) {
            throw new GeneralFailureException(re.getMessage());
        }
    }

   /**
     * @return the shopping cart associated with this client session.
     * @exception com.sun.j2ee.blueprints.petstore.control.GeneralFailureException
     */
    public synchronized ShoppingCartModel getShoppingCart() {
        try {
            Locale locale = JSPUtil.getLocale(session);
            return sccEjb.getShoppingCart().getDetails(locale);
        } catch (EStoreAppException ce) {
            throw new GeneralFailureException(ce.getMessage());
        } catch (RemoteException re) {
            throw new GeneralFailureException(re.getMessage());
        }
     }

    /**
     * feeds the specified event to the state machine of the business logic.
     *
     * @param ese is the current event
     * @return a list of models that got updated because of the
     *         processing of this event.
     * @exception com.sun.j2ee.blueprints.petstore.control.EStoreEventException <description>
     * @exception com.sun.j2ee.blueprints.petstore.control.GeneralFailureException
     */
    public synchronized Collection handleEvent(EStoreEvent ese)
        throws EStoreEventException {
        try {
            return sccEjb.handleEvent(ese);
        } catch (RemoteException re) {
                throw new GeneralFailureException(re.getMessage());
        }
    }

     /**
     * frees up all the resources associated with this controller and
     * destroys itself.
     */
    public synchronized void remove() {
        // call ejb remove on self/shopping cart/etc.
        try {
            sccEjb.remove();
        } catch(RemoveException re){
            // ignore, after all its only a remove() call!
            Debug.print(re);
        } catch(RemoteException re){
            // ignore, after all its only a remove() call!
            Debug.print(re);
        }
    }
}

/*
 * $Id: ShoppingClientControllerEJB.java,v 1.13.4.13 2001/03/15 03:50:35 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.ejb;

import java.util.Collection;
import java.util.HashMap;
import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import com.sun.j2ee.blueprints.petstore.control.event.OrderEvent;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.EJBUtil;
import com.sun.j2ee.blueprints.shoppingcart.cart.ejb.ShoppingCart;
import com.sun.j2ee.blueprints.shoppingcart.cart.ejb.ShoppingCartHome;
import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.customer.ejb.Customer;
import com.sun.j2ee.blueprints.customer.customer.ejb.CustomerHome;
import com.sun.j2ee.blueprints.personalization.profilemgr.ejb.ProfileMgr;
import com.sun.j2ee.blueprints.personalization.profilemgr.ejb.ProfileMgrHome;
import com.sun.j2ee.blueprints.petstore.util.EJBKeys;

import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;


/**
 * Session Bean implementation for ShoppingClientController EJB.
 */
public class ShoppingClientControllerEJB implements SessionBean {

    private StateMachine sm;
    private ShoppingCart cart;
    private SessionContext sc;
    private Customer cust;
    private ProfileMgr pro;

    public ShoppingClientControllerEJB() {}

    /** @return the Customer entity bean for this user. */

    public Customer getCustomer() throws EStoreAppException {
        String userId = (String)sm.getAttribute(EJBKeys.USERNAME);
        Debug.println("ShoppingClientController: getCustomer userId=" + userId);
        if (cust == null) {
            try {
                CustomerHome home = EJBUtil.getCustomerHome();
                cust = home.create();
            } catch (CreateException ce) {
                throw new EStoreAppException("Unable to create a customer instance while getting the details of user " + userId);
            } catch (RemoteException re) {
                throw new EJBException (re);
            } catch (javax.naming.NamingException ne) {
                throw new EJBException (ne);
            }
        }
        return cust;
    }

    /** @return the profilemgr entity bean for this user. */
    public ProfileMgr getProfileMgr() throws EStoreAppException {

        String userId = (String)sm.getAttribute(EJBKeys.USERNAME);
        if (pro == null) {
            try {
                Debug.println("ShoppingClientController: userId: " + userId);
                ProfileMgrHome home = EJBUtil.getProfileMgrHome();
                pro = home.findByPrimaryKey(userId);
            } catch (FinderException fe) {
                throw new EStoreAppException("Unable to find the profile of user " + userId);
            } catch (RemoteException re) {
                throw new EJBException (re);
            } catch (javax.naming.NamingException ne) {
                throw new EJBException (ne);
            }
        }
        return pro;
    }

    /** @return the session EJB associated with this session. */
    public ShoppingCart getShoppingCart() throws EStoreAppException {
        if (cart == null) {
            try {
                ShoppingCartHome cartHome = EJBUtil.getShoppingCartHome();
                cart = cartHome.create();
            } catch (CreateException ce) {
                throw new EStoreAppException("Unable to create a shopping cart");
            } catch (RemoteException re) {
                throw new EJBException(re);
            } catch (javax.naming.NamingException ne) {
                throw new EJBException (ne);
            }
        }
        return cart;
    }

    public void ejbCreate() {
        sm = new StateMachine(this, sc);
    }

    public int getOrder(int requestId) {
        int orderId = -1;

        HashMap orderTable = (HashMap)sm.getAttribute("orderTable");
        if (orderTable != null) {
            if (orderTable.containsKey(requestId + "")) {
                orderId = Integer.parseInt((String)orderTable.get(requestId  + ""));
            }
            else {
                Debug.println("ShoppingClientControllerEJB.getOrder failed trying to lookup the orderID corresponding to requestId = " +  requestId);
                throw new EJBException("ShoppingClientControllerEJB.getOrder failed trying to lookup the orderID corresponding to requestId = " +  requestId);
            }
        } else {
            Debug.println("ShoppingClientContorllerEJB: unable to obtain orderTable for requestID= " + requestId + " because orderTable is  null");
            throw new EJBException("ShoppingClientContorllerEJB: unable to obtain orderTable for requestID= " + requestId + " because orderTable is  null");
        }
        return orderId;
    }

/** returns a Collection of updated Models */

    public Collection handleEvent(EStoreEvent ese)
        throws EStoreEventException {
          return (sm.handleEvent(ese));
    }

    public void setSessionContext(SessionContext sc) {
        this.sc = sc;
    }

    public void ejbRemove() {
        sm = null;
        // this method will be called at the time of sign off.
        // destroy all the EJB's created by the shopping client
        // controller.

        if (cart != null) {
            try {
                cart.remove();
            } catch (RemoteException re) {
                throw new EJBException(re);
            } catch (RemoveException re) {
            }
        }
        cart = null;
        cust = null;
        pro = null;
    }

    public void ejbActivate() {}

    public void ejbPassivate() {}
}


/*
 * $Id: ModelManager.java,v 1.23.4.7 2001/03/16 19:56:19 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */
package com.sun.j2ee.blueprints.petstore.control.web;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.EJBUtil;
import com.sun.j2ee.blueprints.petstore.control.web.CatalogWebImpl;
import com.sun.j2ee.blueprints.petstore.control.web.ShoppingCartWebImpl;
import com.sun.j2ee.blueprints.shoppingcart.cart.model.ShoppingCartModel;
import com.sun.j2ee.blueprints.shoppingcart.cart.ejb.ShoppingCart;
import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.customer.ejb.Customer;
import com.sun.j2ee.blueprints.petstore.control.web.CustomerWebImpl;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ProfileMgrModel;
import com.sun.j2ee.blueprints.personalization.profilemgr.ejb.ProfileMgr;
import com.sun.j2ee.blueprints.petstore.control.web.ProfileMgrWebImpl;
import com.sun.j2ee.blueprints.petstore.control.ejb.ShoppingClientController;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;

/**
 * This interface provides a convenient set of methods for the
 * web tier components to access all the model objects.
 * This class also insures that only one copy of the  model objects
 * are created for web tier access by placing a reference to the
 * model objects in the session.
 */
public class ModelManager extends ModelUpdateNotifier implements java.io.Serializable {

    private ServletContext context;
    private HttpSession session;
    private ShoppingClientController sccEjb = null;
    private ShoppingCart cartEjb = null;
    private Customer custEjb = null;
    private ProfileMgr proEjb = null;
    private ShoppingClientControllerWebImpl scc = null;

    public ModelManager() { }

    public void init(ServletContext context, HttpSession session) {
        this.session = session;
        this.context = context;
        getCustomerWebImpl();
        getCartModel();
        getCatalogModel();
        getProfileMgrModel();
        getInventoryModel();
    }

    public void setSCC(ShoppingClientControllerWebImpl scc) {
        this.scc = scc;
    }

    public InventoryWebImpl getInventoryModel() {
        InventoryWebImpl inventory = (InventoryWebImpl)
        context.getAttribute(WebKeys.InventoryModelKey);
        if (inventory == null) {
            inventory = new InventoryWebImpl();
            context.setAttribute(WebKeys.InventoryModelKey, inventory);
        }
        return inventory;
    }

    public CatalogWebImpl getCatalogModel() {
        CatalogWebImpl catalog = (CatalogWebImpl)
        context.getAttribute(WebKeys.CatalogModelKey);
        if (catalog == null) {
            catalog = new CatalogWebImpl();
            context.setAttribute(WebKeys.CatalogModelKey, catalog);
        }
        return catalog;
    }

    public CustomerWebImpl getCustomerWebImpl() {
        CustomerWebImpl customer = (CustomerWebImpl)
        session.getAttribute(WebKeys.CustomerWebImplKey);
        if (customer == null) {
            customer = new CustomerWebImpl(this);
            session.setAttribute(WebKeys.CustomerWebImplKey, customer);
        }
        return customer;
    }

    public ProfileMgrModel getProfileMgrModel() {
        ProfileMgrModel pro = (ProfileMgrModel)
            session.getAttribute(WebKeys.ProfileMgrModelKey);
        if (pro == null) {
            pro = new ProfileMgrWebImpl(this);
            session.setAttribute(WebKeys.ProfileMgrModelKey, pro);
        }
        return pro;
    }

    public ShoppingCartModel getCartModel() {
        ShoppingCartWebImpl cart = (ShoppingCartWebImpl)
        session.getAttribute(WebKeys.ShoppingCartModelKey);
        if (cart == null) {
            cart = new ShoppingCartWebImpl();
            cart.init(session);
            session.setAttribute(WebKeys.ShoppingCartModelKey, cart);
        }
        return cart;
    }

    public ShoppingClientController getSCCEJB() {
        if (sccEjb == null) {
            try {
                sccEjb = EJBUtil.getSCCHome().create();
            } catch (CreateException ce) {
                throw new GeneralFailureException(ce.getMessage());
            } catch (RemoteException re) {
                throw new GeneralFailureException(re.getMessage());
            } catch (javax.naming.NamingException ne) {
                 throw new GeneralFailureException(ne.getMessage());
            }
        }
        return sccEjb;
    }

    public ShoppingCart getShoppingCartEJB()throws EStoreAppException {
        if (cartEjb == null) {
            if (scc == null) {
                throw new
                GeneralFailureException("ModelManager: Can not get shopping cart EJB");
            } else {
                cartEjb = scc.getShoppingCartEJB();
            }
        }
        return cartEjb;
    }

    public Customer getCustomerEJB() throws EStoreAppException {
        if (custEjb == null) {
            if (scc == null) {
                throw new
                GeneralFailureException("ModelManager: Can not get customer EJB");
            } else {
                custEjb = scc.getCustomerEJB();
            }
        }
        return custEjb;
    }

    public ProfileMgr getProfileMgrEJB() throws EStoreAppException {
        if (proEjb == null) {
            if (scc == null) {
                throw new
                GeneralFailureException("ModelManager: Can not get profilemgr EJB");
            } else {
                proEjb = scc.getProfileMgrEJB();
            }
        }
        return proEjb;
    }
}


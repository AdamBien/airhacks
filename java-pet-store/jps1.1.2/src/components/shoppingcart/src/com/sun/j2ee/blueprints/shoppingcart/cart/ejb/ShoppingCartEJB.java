/*
 * $Id: ShoppingCartEJB.java,v 1.6.4.3 2001/03/13 00:45:15 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */
package com.sun.j2ee.blueprints.shoppingcart.cart.ejb;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import java.rmi.RemoteException;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item;
import com.sun.j2ee.blueprints.shoppingcart.cart.model.CartItem;
import com.sun.j2ee.blueprints.shoppingcart.cart.model.ShoppingCartModel;
import com.sun.j2ee.blueprints.shoppingcart.catalog.ejb.Catalog;

import com.sun.j2ee.blueprints.shoppingcart.util.EJBUtil;
import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class represents the implementation of shopping
 * cart as a Session EJB. The shopping cart EJB uses the
 * catalog EJB to get details about items in the cart.
 *
 * @see Catalog
 */
public class ShoppingCartEJB implements SessionBean {

    private HashMap cart;
    private Catalog catalog;

    public ShoppingCartEJB() {
        cart = new HashMap();
    }

    public ShoppingCartModel getDetails(Locale locale)  {
        ArrayList items = new ArrayList();
        try {
            if (catalog == null) {
                catalog = EJBUtil.getCatalogHome().create();
            }
            Set keys = null;
            if (cart != null) keys = cart.keySet();
            Iterator it = null;
            if (keys != null) it = keys.iterator();
            while ((it != null) &&  it.hasNext()) {
                String itemId = (String) it.next();
                int qtyNeeded = ((Integer) cart.get(itemId)).intValue();
                Item item = catalog.getItem(itemId, locale);
                String productId = item.getProductId();
                Product p = catalog.getProduct(productId, locale);
                String productName = p.getName();
                CartItem cartItem =
                    new CartItem(itemId, productId, productName,
                                 item.getAttribute(), qtyNeeded,
                                 item.getListCost());
                items.add(cartItem);
            }
        } catch (RemoteException re) {
           throw new EJBException(re.getMessage());
        } catch (Exception ex) {
            throw new EJBException(ex.getMessage());
        }
        ShoppingCartModel model = new ShoppingCartModel(items);
        return model;
    }

    public void addItem (String itemNo) {
        cart.put(itemNo, new Integer(1));
    }

    public void addItem (String itemNo,int qty) {
        cart.put(itemNo, new Integer(qty));
    }

    public void deleteItem (String itemNo) {
        cart.remove(itemNo);
    }

    public void updateItemQty (String itemNo, int newQty) {
        cart.remove(itemNo);
        cart.put(itemNo, new Integer(newQty));
    }

    public void empty () {
        cart.clear();
    }

    public void ejbCreate() {
        cart = new HashMap();
    }

    public void ejbCreate(HashMap starting) {
        cart = (HashMap) starting.clone();
    }

    public void setSessionContext(SessionContext sc) {}

    public void ejbRemove() {}

    public void ejbActivate() {}

    public void ejbPassivate() {}
}


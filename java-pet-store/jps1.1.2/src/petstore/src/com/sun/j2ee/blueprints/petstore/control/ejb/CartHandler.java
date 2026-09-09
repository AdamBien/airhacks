/*
 * $Id: CartHandler.java,v 1.6.4.3 2001/03/15 00:39:57 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.ejb;


import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

import com.sun.j2ee.blueprints.shoppingcart.cart.ejb.ShoppingCart;

import com.sun.j2ee.blueprints.petstore.control.event.CartEvent;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;


public class CartHandler extends StateHandlerSupport {

   public void perform(EStoreEvent event) throws EStoreEventException {
        CartEvent ce = (CartEvent)event;
        ShoppingCart cart = machine.getShoppingClientControllerEJB().getShoppingCart();
        try {
            switch (ce.getActionType()) {

            case CartEvent.ADD_ITEM:{
                    Collection itemIds = ce.getItemIds();
                    Iterator it = itemIds.iterator();
                    while (it.hasNext()){
                        cart.addItem((String)it.next());
                    }
                }
            break;
            case CartEvent.DELETE_ITEM: {
                    Collection itemIds = ce.getItemIds();
                    Iterator it = itemIds.iterator();
                    while (it.hasNext()) {
                        cart.deleteItem((String)it.next());
                    }
                }

                break;
            case CartEvent.UPDATE_ITEM :{
                    Collection itemIds = ce.getItemIds();
                    Iterator it = itemIds.iterator();
                    while (it.hasNext()){
                         String itemId = (String)it.next();
                         int quantity = ce.getItemQty(itemId);
                         // change the quanty or delete the item if the item quantity is less than or equal to 0
                         if (quantity > 0){
                            cart.updateItemQty(itemId, quantity);
                         } else {
                             cart.deleteItem(itemId);
                         }
                    }
                }
                break;
            }
        } catch (java.rmi.RemoteException re) {
            throw new EJBException("Irrecoverable error changingcreating / updating the shopping cart:" + re);
        }
    }
}

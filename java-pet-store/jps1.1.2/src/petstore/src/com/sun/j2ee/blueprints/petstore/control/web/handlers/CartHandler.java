/*
 * $Id: CartHandler.java,v 1.1.4.2 2001/03/15 00:40:06 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web.handlers;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;

import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import javax.servlet.http.HttpServletRequest;
import com.sun.j2ee.blueprints.petstore.control.event.CartEvent;
import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * CartHandler
 *
*/
public class CartHandler extends RequestHandlerSupport {

    public EStoreEvent processRequest(HttpServletRequest request){
        Debug.println("Started Create Cart Event");
            String action = request.getParameter("action");
            if (action == null) {
                return null;
            } else if (action.equals("purchaseItem")) {
                return createPurchaseItemEvent(request);
            } else if (action.equals("removeItem")) {
                return createRemoveItemEvent(request);
            } else if (action.equals("updateCart")) {
                return createUpdateCartEvent(request);
            }
        return null;
    }

    private EStoreEvent createUpdateCartEvent(HttpServletRequest request){
        CartEvent event = null;
        HashMap quantities = new HashMap();
        ArrayList itemIds = new ArrayList();
        Debug.println("CartHandler: Updating Cart Item quantities");
        Enumeration enum = request.getParameterNames();

        while ((enum != null) && enum.hasMoreElements()) {
            String param = ((String)enum.nextElement()).trim();
            if ((param != null) && param.startsWith("itemQuantity_")) {
                try{
                    // get the item id number from the parameter
                    String id = param.substring("itemQuantity_".length(),
                    param.length());
                    Integer quantity = new Integer(0);
                    if (id != null) {
                        // remove image map info from the parameter
                        if (id.lastIndexOf(".") != -1) {
                            id = id.substring(0, id.lastIndexOf("."));
                        }
                        try {
                            quantity= Integer.valueOf(request.getParameter(param));
                                                                      itemIds.add(id);
                                                                      quantities.put(id, quantity);
                        } catch (NumberFormatException ex) {
                            // if the user uses something other than numbers leave as is
                        }
                        event = new CartEvent(CartEvent.UPDATE_ITEM,  itemIds, quantities);
                    }
                } catch(Exception e) {
                    Debug.print(e);
                }
            }
        }
        return event;
    }

    private CartEvent createRemoveItemEvent(HttpServletRequest request) {
        Debug.println("Started Remove Action");
        CartEvent event = null;
        try {
            // get the id number from the parameter
            String id = request.getParameter("itemId").trim();
            ArrayList itemIds = new ArrayList();
            itemIds.add(id);
            event = new CartEvent(CartEvent.DELETE_ITEM, itemIds);
        } catch(Exception e) {
            Debug.print(e);
        }
        return event;
    }

    private CartEvent createPurchaseItemEvent(HttpServletRequest request) {
        Debug.println("Started Purchase Action");
        CartEvent event = null;
        try {
            // get the id number from the parameter
            String id = request.getParameter("itemId").trim();
            ArrayList itemIds = new ArrayList();
            itemIds.add(id);
            event = new CartEvent(CartEvent.ADD_ITEM, itemIds);
        } catch(Exception e) {
            Debug.print(e);
        }
        return event;
    }

}

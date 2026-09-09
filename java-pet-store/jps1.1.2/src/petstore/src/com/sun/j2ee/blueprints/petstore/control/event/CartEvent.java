/*
 * $Id: CartEvent.java,v 1.5.4.2 2001/03/15 00:39:59 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.event;

import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents a shopping cart event.
 * It allows updates on multiple items in the shopping cart.
 */
public class CartEvent extends EStoreEventSupport {

    public static final int ADD_ITEM = 0;
    public static final int DELETE_ITEM = 1;
    public static final int UPDATE_ITEM = 2;

    private int actionType;
    private Collection itemIds;
    private HashMap quantities;

    public CartEvent(int actionType, Collection itemIds) {
        this.actionType = actionType;
        this.itemIds = itemIds;
    }

    public CartEvent(int actionType, Collection itemIds, HashMap quantities) {
        this.actionType = actionType;
        this.itemIds = itemIds;
        this.quantities = quantities;
    }

    public Collection getItemIds() {
        return itemIds;
    }

    public int getActionType() {
        return actionType;
    }

    public int getItemQty(String itemId) {
        if (quantities != null) return ((Integer)quantities.get(itemId)).intValue();
        else return -1;
    }

    public String toString() {
        return "CartEvent(" + actionType + ", " + itemIds + ")";
    }

    public String getEventName() {
        return "java:comp/env/event/CartEvent";
    }
}

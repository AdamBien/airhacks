/*
 * $Id: ShoppingCartModel.java,v 1.5.4.3 2001/03/13 00:45:15 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.cart.model;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Collection;
import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class represents the model data for the shopping cart.
 * It is a value object and has fine grained getter methods.
 */
public class ShoppingCartModel implements Serializable {

    private Collection items;

    public ShoppingCartModel(Collection items) {
        this.items = items;
    }

    /**
     * Class constructor with no arguments, used by the web tier.
     */
    public ShoppingCartModel() {}

    public int getSize() {
        if (items != null) return items.size();
        else return 0;
    }


    /** @return an collection of all the CartItems. */
    public Collection getCart() {
        return items;
    }


    /** @return an iterator over all the CartItems. */
    public Iterator getItems() {
        return items.iterator();
    }

    public double getTotalCost() {
        double total = 0;
        for (Iterator li = getItems(); li.hasNext(); ) {
            CartItem item = (CartItem) li.next();
            total += item.getTotalCost();
        }
        return total;
    }

    /**
     * copies over the data from the specified shopping cart. Note
     * that it is a shallow copy.
     */

    public void copy(ShoppingCartModel src) {
        this.items = src.items;
    }
}

/*
 * $Id: ListChunk.java,v 1.1.4.2 2001/03/08 22:50:02 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */


package com.sun.j2ee.blueprints.shoppingcart.catalog.model;


import java.util.Collection;

/**
 * This class represents a list of items/products/categories that is returned
 * to the web tier in response to the getItems/getProducts/getCategories/
 * searchProducts request
*/

public class ListChunk implements java.io.Serializable {

    private int            totalElements;
    private Collection     elementsInThisList;
    private int            firstElementOfThisList;
    private int            countOfElementsInthisList;

    public ListChunk(int count, Collection coll, int first, int curCount) {
        this.totalElements = count;
        this.elementsInThisList = coll;
        this.firstElementOfThisList = first;
        this.countOfElementsInthisList = curCount;
    }

    /**
     * Class constructor with no arguments, used by the web tier.
     */
    public ListChunk() {}

    public int getTotalCount() {
        return totalElements;
    }

    public Collection getCollection() {
        return elementsInThisList;
    }

    public int getCurrentCount() {
        return countOfElementsInthisList;
    }

    public int getFirstElementIndex() {
        return firstElementOfThisList;
    }
}

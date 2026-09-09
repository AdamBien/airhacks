/*
 * $Id: Catalog.java,v 1.9.4.2 2001/03/01 12:33:02 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.catalog.ejb;

import java.util.Collection;
import java.util.Locale;
import java.rmi.RemoteException;

import javax.ejb.EJBObject;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.shoppingcart.catalog.model.ListChunk;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Category;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item;


/**
 * This interface is the remote interface for the catalog EJB.
 * It provides the ejb-tier implementation of the catalog functions
 */

public interface Catalog extends EJBObject {

    public Category getCategory(String categoryId, Locale locale)
    throws RemoteException;

    public ListChunk getCategories(int startIndex, int count,  Locale locale)
    throws RemoteException;

    public ListChunk getProducts(String categoryId,
                  int startIndex,
                  int count, Locale locale)
    throws RemoteException;

    public Product getProduct(String productId, Locale locale)
    throws RemoteException;

    public ListChunk getItems(String productId,
                   int startIndex,
                   int count, Locale locale)
    throws RemoteException;

    public Item getItem(String itemId, Locale locale)
    throws RemoteException;

    public ListChunk searchProducts(Collection keyWords,
                     int startIndex,
                     int count, Locale locale)
    throws RemoteException;

}

/*
 * $Id: CatalogDAO.java,v 1.1.2.1 2001/03/12 09:15:43 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.catalog.dao;

import java.util.Collection;
import java.util.Locale;

import com.sun.j2ee.blueprints.shoppingcart.catalog.exceptions.CatalogDAOSysException;

import com.sun.j2ee.blueprints.shoppingcart.catalog.model.ListChunk;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Category;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item;

/**
 * This class is an interface which will be implemented by database specific
 * code.
 * This class encapsulates all the SQL calls made by Catalog EJB.
 * This layer maps the relational data stored in the database to
 * the objects needed by Catalog EJB.
*/
public interface CatalogDAO {

    public Category getCategory(String categoryId, Locale locale) throws
                                    CatalogDAOSysException;

    public ListChunk getCategories(int startIndex, int count,
                            Locale locale) throws CatalogDAOSysException;

    public Product getProduct(String productId, Locale locale) throws
                                        CatalogDAOSysException;

    public ListChunk getProducts(String categoryId, int startIndex,
                int count, Locale locale) throws CatalogDAOSysException;

    public Item getItem(String itemId, Locale locale) throws
                                        CatalogDAOSysException;

    public ListChunk getItems(String productId, int startIndex, int count,
                                Locale locale) throws CatalogDAOSysException;

    public ListChunk searchProducts(Collection keyWords, int startIndex,
                    int count, Locale locale) throws CatalogDAOSysException;
}

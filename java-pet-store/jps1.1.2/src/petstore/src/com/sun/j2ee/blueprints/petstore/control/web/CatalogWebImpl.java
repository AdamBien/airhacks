/*
 * $Id: CatalogWebImpl.java,v 1.9.4.8 2001/03/16 19:56:18 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import java.util.Collection;
import java.util.Locale;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.shoppingcart.catalog.exceptions.CatalogDAOSysException;
import com.sun.j2ee.blueprints.shoppingcart.catalog.dao.CatalogDAO;
import com.sun.j2ee.blueprints.shoppingcart.catalog.dao.CatalogDAOFactory;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.ListChunk;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Category;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;


/**
 * Web-tier implementation of catalog.
 */
public class CatalogWebImpl implements java.io.Serializable {

    protected CatalogDAO dao;

    public CatalogWebImpl() {
        try {
            dao = CatalogDAOFactory.getDAO();
                } catch (CatalogDAOSysException se) {
            throw new GeneralFailureException(se.getMessage());
        }
    }

    public Category getCategory(String categoryId, Locale locale) {
        try {
            return dao.getCategory(categoryId, locale);
        } catch (CatalogDAOSysException se) {
            throw new GeneralFailureException(se.getMessage());
        }
    }

    public ListChunk getCategories(int stIndex, int count, Locale locale)  {
        try {
            return dao.getCategories(stIndex, count, locale);
        } catch (CatalogDAOSysException se) {
            throw new GeneralFailureException(se.getMessage());
        }
    }

    public ListChunk getProducts(String categoryId,
           int startIndex, int count, Locale locale) {
        try {
            return dao.getProducts(categoryId, startIndex, count, locale);
        } catch (CatalogDAOSysException se) {
            throw new GeneralFailureException(se.getMessage());
        }
    }

    public Product getProduct(String productId, Locale locale) {
        try {
            return dao.getProduct(productId, locale);
        } catch (CatalogDAOSysException se) {
            throw new GeneralFailureException(se.getMessage());
        }
    }

    public ListChunk getItems(String productId, int startIndex,
                          int count, Locale locale) {
        try {
            return dao.getItems(productId, startIndex, count, locale);
        } catch (CatalogDAOSysException se) {
            throw new GeneralFailureException(se.getMessage());
        }
    }

    public Item getItem(String itemId, Locale locale) {
       try {
            return dao.getItem(itemId, locale);
        } catch (CatalogDAOSysException se) {
            throw new GeneralFailureException(se.getMessage());
        }
    }

    public ListChunk searchProducts(Collection keyWords,
          int startIndex, int count, Locale locale) {
        try {
            return dao.searchProducts(keyWords, startIndex, count, locale);
        } catch (CatalogDAOSysException se) {
            throw new GeneralFailureException(se.getMessage());
        }
    }
}

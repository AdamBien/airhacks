/*
 * $Id: CatalogEJB.java,v 1.8.4.8 2001/04/14 00:39:25 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.catalog.ejb;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.EJBException;

import java.util.Collection;
import java.util.Locale;

import com.sun.j2ee.blueprints.shoppingcart.catalog.dao.CatalogDAO;
import com.sun.j2ee.blueprints.shoppingcart.catalog.dao.CatalogDAOFactory;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.ListChunk;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Category;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item;

import com.sun.j2ee.blueprints.shoppingcart.catalog.exceptions.CatalogDAOSysException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * Session Bean implementation of Catalog
 *
 */
public class CatalogEJB implements SessionBean {

    protected CatalogDAO dao;

    public void ejbCreate() {
        try {
            dao = CatalogDAOFactory.getDAO();
        } catch (CatalogDAOSysException se) {
            throw new EJBException(se.getMessage());
        }
    }

    public void setSessionContext(SessionContext sc) {}

    public void ejbRemove() {}

    public void ejbActivate() {
    }

    public void ejbPassivate() {
        dao = null;
    }

    public void destroy() {
        dao = null;
    }

    public Category getCategory(String categoryId, Locale locale) {
        try {
            return dao.getCategory(categoryId, locale);
        } catch (CatalogDAOSysException se) {
            throw new EJBException(se.getMessage());
        }
    }

    public ListChunk getCategories(int stIndex, int count, Locale locale) {
        try {
            return dao.getCategories(stIndex, count, locale);
        } catch (CatalogDAOSysException se) {
            throw new EJBException(se.getMessage());
        }
    }

    public ListChunk getProducts(String categoryId,
            int startIndex, int count, Locale locale) {
        try {
            return dao.getProducts(categoryId, startIndex, count, locale);
        } catch (CatalogDAOSysException se) {
            throw new EJBException(se.getMessage());
        }
    }

    public Product getProduct(String productId, Locale locale) {
        try {
            return dao.getProduct(productId, locale);
        } catch (CatalogDAOSysException se) {
            throw new EJBException(se.getMessage());
        }
    }

    public ListChunk getItems(String productId, int startIndex,
                            int count, Locale locale) {
        try {
            return dao.getItems(productId, startIndex, count, locale);
        } catch (CatalogDAOSysException se) {
            throw new EJBException(se.getMessage());
        }
    }

    public Item getItem(String itemId, Locale locale) {
       try {
            return dao.getItem(itemId, locale);
        } catch (CatalogDAOSysException se) {
            throw new EJBException(se.getMessage());
        }
    }

    public ListChunk searchProducts(Collection keyWords,
            int startIndex, int count, Locale locale) {
        try {
            return dao.searchProducts(keyWords, startIndex, count, locale);
        } catch (CatalogDAOSysException se) {
            throw new EJBException(se.getMessage());
        }
    }
}

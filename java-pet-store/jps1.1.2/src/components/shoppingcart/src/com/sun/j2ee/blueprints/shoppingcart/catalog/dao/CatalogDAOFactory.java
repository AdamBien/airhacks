/*
 * $Id: CatalogDAOFactory.java,v 1.1.2.1 2001/03/12 09:15:43 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.catalog.dao;

import javax.naming.NamingException;
import javax.naming.InitialContext;

import com.sun.j2ee.blueprints.shoppingcart.util.JNDINames;
import com.sun.j2ee.blueprints.shoppingcart.catalog.exceptions.CatalogDAOSysException;

public class CatalogDAOFactory {

    /**
     * This method instantiates a particular subclass implementing
     * the DAO methods based on the information obtained from the
     * deployment descriptor
     */
    public static CatalogDAO getDAO() throws CatalogDAOSysException {

        CatalogDAO catDao = null;
        try {
            InitialContext ic = new InitialContext();
            String className = (String) ic.lookup(JNDINames.CATALOG_DAO_CLASS);
            catDao = (CatalogDAO) Class.forName(className).newInstance();
        } catch (NamingException ne) {
            throw new CatalogDAOSysException("CatalogDAOFactory.getDAO:  NamingException while getting DAO type : \n" + ne.getMessage());
        } catch (Exception se) {
            throw new CatalogDAOSysException("CatalogDAOFactory.getDAO:  Exception while getting DAO type : \n" + se.getMessage());
        }
        return catDao;
    }
}

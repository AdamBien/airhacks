/*
 * $Id: InventoryDAOFactory.java,v 1.1.2.1 2001/03/12 09:15:38 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.inventory.dao;

import javax.naming.NamingException;
import javax.naming.InitialContext;

import com.sun.j2ee.blueprints.inventory.util.JNDINames;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOSysException;

public class InventoryDAOFactory {

    /**
     * This method instantiates a particular subclass implementing
     * the DAO methods based on the information obtained from the
     * deployment descriptor
     */
    public static InventoryDAO getDAO() throws InventoryDAOSysException {

        InventoryDAO invDao = null;
        try {
            InitialContext ic = new InitialContext();
            String className = (String) ic.lookup(JNDINames.INVENTORY_DAO_CLASS);
            invDao = (InventoryDAO) Class.forName(className).newInstance();
        } catch (NamingException ne) {
            throw new InventoryDAOSysException("InventoryDAOFactory.getDAO:  NamingException while getting DAO type : \n" + ne.getMessage());
        } catch (Exception se) {
            throw new InventoryDAOSysException("InventoryDAOFactory.getDAO:  Exception while getting DAO type : \n" + se.getMessage());
        }
        return invDao;
    }
}

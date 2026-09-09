/*
 * $Id: InventoryDAO.java,v 1.1.2.1 2001/03/12 09:15:38 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.inventory.dao;

import com.sun.j2ee.blueprints.inventory.model.MutableInventoryModel;

import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOSysException;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOAppException;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAODBUpdateException;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOFinderException;

/**
 * This encapsulates all the JDBC calls made by the InventoryEJB.
 * This is an interface and the actual logic of inserting,
 * fetching, updating, or deleting  the data in
 * relational database is done InventoryImpl.java
 */
public interface InventoryDAO {
    public MutableInventoryModel load(String key) throws
                     InventoryDAOSysException, InventoryDAOFinderException;
    public void store(MutableInventoryModel model) throws
                     InventoryDAOAppException, InventoryDAODBUpdateException,
                     InventoryDAOSysException;
    public void remove(String key) throws InventoryDAOSysException,
                     InventoryDAODBUpdateException;
    public String findByPrimaryKey(String id) throws InventoryDAOSysException,
                     InventoryDAOFinderException;
}

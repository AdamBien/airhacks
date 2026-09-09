/*
 * $Id: InventoryWebImpl.java,v 1.6.4.3 2001/03/15 00:40:02 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.EJBUtil;

import com.sun.j2ee.blueprints.inventory.ejb.InventoryHome;
import com.sun.j2ee.blueprints.inventory.ejb.Inventory;
import com.sun.j2ee.blueprints.inventory.model.InventoryModel;


/**
 * This class is the web tier representation of the
 * Product Inventory.
 */
public class InventoryWebImpl implements java.io.Serializable {

    public InventoryWebImpl() {}

    /**
     * Get the amount of an item available directly from
     * the InventoryEJB.
     *
     * @return  the amount of an item in inventory.
     *          0 is returned by default.
     *
     * @see InventoryModel
     * @see Inventory
     */
    public int getInventory(String itemId){
        try{
            InventoryHome inventoryHome = EJBUtil.getInventoryHome();
                    Inventory inventory = inventoryHome.findByPrimaryKey(itemId);
            return inventory.getDetails().getQuantity();
        } catch (RemoteException re) {
           Debug.println("InventoryBean: Unable to locate invetory for item " + itemId);
        } catch (FinderException fe) {
           Debug.println("InventoryBean: Unable to locate invetory for item " + itemId);
        } catch (javax.naming.NamingException ne) {
          throw new GeneralFailureException(ne.getMessage());
        }
        return 0;
    }
}

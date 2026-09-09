/*
 * $Id: InventoryEJB.java,v 1.11.4.10 2001/04/14 00:39:24 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.inventory.ejb;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.DuplicateKeyException;
import com.sun.j2ee.blueprints.inventory.dao.InventoryDAO;
import com.sun.j2ee.blueprints.inventory.dao.InventoryDAOFactory;
import com.sun.j2ee.blueprints.inventory.model.InventoryModel;
import com.sun.j2ee.blueprints.inventory.model.MutableInventoryModel;

import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOAppException;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOSysException;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOFinderException;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAODBUpdateException;

/**
 * Implementation of inventory as an Entity Bean
 */
public class InventoryEJB implements EntityBean {

    private MutableInventoryModel itemDetails;
    private transient InventoryDAO inventoryDao;
    private EntityContext context;

    public InventoryEJB() {}

    public void ejbRemove() throws RemoveException {

        try{
            InventoryDAO dao = getDAO();
            dao.remove((String)context.getPrimaryKey());
        } catch (InventoryDAODBUpdateException se) {
            context.setRollbackOnly();
            throw new RemoveException(se.getMessage());
        } catch (InventoryDAOSysException ss) {
            throw new EJBException(ss.getMessage());
        }
    }

    public void setEntityContext(EntityContext ec) {
        context = ec;
    }

    public void ejbLoad() {
        InventoryDAO dao;
        try{
            dao = getDAO();
            this.itemDetails = dao.load((String)context.getPrimaryKey());
        } catch (InventoryDAOSysException se) {
            throw new EJBException (se.getMessage());
        } catch (InventoryDAOFinderException ss) {
            throw new EJBException (ss.getMessage());
        }
    }

    public void ejbStore() {
        try{
            InventoryDAO dao = getDAO();
            dao.store(this.itemDetails);
        } catch (InventoryDAOSysException se) {
            throw new EJBException (se.getMessage());
        } catch (InventoryDAOAppException ss) {
            throw new EJBException (ss.getMessage());
        }
    }

    public String ejbFindByPrimaryKey (String key) throws FinderException {
        try{
            InventoryDAO dao = getDAO();
            String item = dao.findByPrimaryKey(key);
            this.itemDetails = dao.load(item);
            return(item);
        } catch (InventoryDAOFinderException se) {
            throw new FinderException (se.getMessage());
        } catch (InventoryDAOSysException ss) {
            throw new EJBException (ss.getMessage());
        }
    }

    public void unsetEntityContext() {}

    public void ejbActivate() {
    }

    public void ejbPassivate() {
      this.inventoryDao = null;
    }

    // business methods

    /**
     * @return  the InventoryModel containing the
     * inventory data details
     */
    public InventoryModel getDetails() {
        return(new InventoryModel(this.itemDetails.getItemId(),
                                  this.itemDetails.getQuantity()));
    }

    /**
     * reduce the current qty of an item in inventory.
     */
    public void reduceQuantity( int qty) {
        this.itemDetails.reduceQty(qty);
    }

    /**
     * increase the current qty of an item in inventory.
     */
    public void addQuantity(int qty) {
        this.itemDetails.addQty(qty);
    }

    private InventoryDAO getDAO() throws InventoryDAOSysException {
        if(inventoryDao == null) {
            inventoryDao = InventoryDAOFactory.getDAO();
        }
        return inventoryDao;
    }
}

/*
 * $Id: InventoryDAOImpl.java,v 1.1.2.2 2001/03/13 00:39:38 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.inventory.dao;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;

import com.sun.j2ee.blueprints.inventory.util.DatabaseNames;
import com.sun.j2ee.blueprints.inventory.util.JNDINames;

import com.sun.j2ee.blueprints.inventory.model.MutableInventoryModel;

import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOSysException;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOAppException;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAODBUpdateException;
import com.sun.j2ee.blueprints.inventory.exceptions.InventoryDAOFinderException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This is the implementation of InventoryDAO for Oracle, Sybase, cloudscape
 * This class encapsulates all the JDBC calls made by
 * the InventoryEJB. The actual logic of inserting,
 * fetching, updating, or deleting  the data in
 * relational database tables to mirror the state of
 * InventoryEJB is implemented here.
 */
public class InventoryDAOImpl implements InventoryDAO {

    private transient Connection dbConnection = null;
    private transient DataSource datasource   = null;

    public InventoryDAOImpl() throws InventoryDAOSysException {
        try {
            InitialContext ic = new InitialContext();
            datasource = (DataSource)
            ic.lookup(JNDINames.INVENTORY_DATASOURCE);
        } catch (NamingException ne) {
            throw new InventoryDAOSysException(
                    "NamingException while looking" +
                    " up DataSource Connection "
                    + JNDINames.INVENTORY_DATASOURCE
                    + ": \n" + ne.getMessage());
        }
    }

    public MutableInventoryModel load(String id) throws
                 InventoryDAOSysException, InventoryDAOFinderException {
        return(selectInventory(id));
    }

    public void store(MutableInventoryModel model) throws
                 InventoryDAOAppException, InventoryDAODBUpdateException,
                 InventoryDAOSysException {
        updateInventory(model);
    }

    public void remove(String id) throws InventoryDAOSysException,
                 InventoryDAODBUpdateException {
        deleteInventory(id);
    }

    public String findByPrimaryKey(String id) throws InventoryDAOSysException,
                 InventoryDAOFinderException {
        if (itemExists(id))
            return (id);
        throw new InventoryDAOFinderException ("Primary key not found; Item Id = " + id);
    }

    private boolean itemExists(String itemId) throws InventoryDAOSysException {
        getConnection();
        String queryStr ="SELECT itemid FROM " +
                DatabaseNames.INVENTORY_TABLE +
                        " WHERE itemid = " + "'" + itemId.trim() + "'";
        Debug.println("queryString is: "+ queryStr);

        Statement stmt = null;
        ResultSet result = null;
        boolean returnValue = false;
        try {
            stmt = dbConnection.createStatement();
            result = stmt.executeQuery(queryStr);
            if ( !result.next() ) {
                returnValue = false;
            } else {
                itemId = result.getString(1);
                returnValue = true;
            }
        } catch(SQLException se) {
            throw new InventoryDAOSysException("Unable to Query for item " +
                                itemId + "\n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
        return returnValue;
    }

    private boolean isValidData(String id) {
        if ( (id == null) )
            return (false);
        else
            return (true);
    }

    private MutableInventoryModel selectInventory(String itemId) throws
                       InventoryDAOSysException, InventoryDAOFinderException {
        getConnection();
        String queryStr = "SELECT "+
            "itemid, qty "+
                " FROM " + DatabaseNames.INVENTORY_TABLE + " WHERE itemid = "
                    + "'" + itemId.trim() + "'";
        Debug.println("queryString is: "+ queryStr);

        Statement stmt = null;
        ResultSet result = null;
        try {
            stmt = dbConnection.createStatement();
            result = stmt.executeQuery(queryStr);
            if(!result.next())
                throw new InventoryDAOFinderException("No record for primary key " + itemId);
            int i = 1;
            itemId = result.getString(i++);
            int qty = result.getInt(i++);
            return(new MutableInventoryModel(itemId, qty));
        } catch(SQLException se) {
            throw new InventoryDAOSysException("Unable to Query for item " + itemId + "\n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void deleteInventory(String itemId) throws
               InventoryDAOSysException, InventoryDAODBUpdateException {
        getConnection();
        String queryStr = "DELETE FROM " + DatabaseNames.INVENTORY_TABLE +
                " WHERE itemid = "
                    + "'" + itemId.trim() + "'";
        Debug.println("queryString is: "+ queryStr);
        Statement stmt = null;
        try {
            stmt = dbConnection.createStatement();
            int resultCount = stmt.executeUpdate(queryStr);
            if ( resultCount != 1 )
                throw new InventoryDAODBUpdateException("ERROR deleteing inventory from" + " INVENTORY_TABLE!! resultCount = " + resultCount);
        } catch(SQLException se) {
            throw new InventoryDAOSysException("Unable to delete for item " + itemId + "\n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void updateInventory(MutableInventoryModel itemDetails) throws
                    InventoryDAOSysException, InventoryDAOAppException,
                    InventoryDAODBUpdateException {
        getConnection();
        if (!isValidData(itemDetails.getItemId()))
            throw new InventoryDAOAppException ("Illegal data values for update");
        Statement stmt = null;
        try {
            stmt = dbConnection.createStatement();
            String queryStr = "UPDATE " + DatabaseNames.INVENTORY_TABLE
                + " SET " + "qty = " +  itemDetails.getQuantity()
                    + " WHERE itemid = " + "'" + itemDetails.getItemId().trim()
                    + "'";
            Debug.println("queryString is: "+ queryStr);
            int resultCount = stmt.executeUpdate(queryStr);
            if ( resultCount != 1 )
                throw new InventoryDAODBUpdateException ("ERROR updating inventory in" + " INVENTORY_TABLE!! resultCount = " + resultCount);
        } catch(SQLException se) {
            throw new InventoryDAOSysException("Unable to update item " + itemDetails.getItemId() + " \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void getConnection() throws InventoryDAOSysException {
        try {
            dbConnection = datasource.getConnection();
        } catch (SQLException se) {
            throw new InventoryDAOSysException("SQLExcpetion while getting" +
                                        " DB Connection : \n" + se);
        }
    }

    private void closeResultSet(ResultSet result) throws InventoryDAOSysException {
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
            throw new InventoryDAOSysException("SQL Exception while closing " +
                                        "Result Set : \n" + se);
        }
    }

    private void closeStatement(Statement stmt) throws InventoryDAOSysException {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
            throw new InventoryDAOSysException("SQL Exception while closing " +
                                        "Statement : \n" + se);
        }
    }

    private void closeConnection() throws InventoryDAOSysException {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException se) {
            throw new InventoryDAOSysException("SQLExcpetion while closing" +
                                    " DB Connection : \n" + se);
        }
    }
}

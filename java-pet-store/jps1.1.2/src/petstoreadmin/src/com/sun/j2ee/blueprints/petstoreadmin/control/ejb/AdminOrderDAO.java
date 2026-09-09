/*
 * $Id: AdminOrderDAO.java,v 1.5.4.9 2001/03/14 23:56:41 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstoreadmin.control.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Collection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.util.I18nUtil;
import com.sun.j2ee.blueprints.customer.util.CreditCard;
import com.sun.j2ee.blueprints.customer.util.Calendar;
import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.customer.order.model.LineItem;
import com.sun.j2ee.blueprints.customer.util.DatabaseNames;
import com.sun.j2ee.blueprints.petstoreadmin.control.ejb.AdminOrderDAOException;

public class AdminOrderDAO {

    protected Connection dbConnection = null;
    private DataSource datasource   = null;
    private String ESTORE_NAME = "java:comp/env/jdbc/EstoreDataSource";

    public AdminOrderDAO() throws AdminOrderDAOException {
        try {
            InitialContext ic = new InitialContext();
            datasource  = (DataSource)
                    ic.lookup(ESTORE_NAME);
        } catch (NamingException ne) {
            throw new AdminOrderDAOException("NamingException while looking" +
               " up DataSource Connection " +
                    ESTORE_NAME +
                         ": \n" + ne.getMessage());
        }
    }

    private Collection getLineItemDetails(int orderId)
                                throws AdminOrderDAOException {
        String queryStr = "SELECT linenum,itemid,quantity,unitprice FROM " +
                 DatabaseNames.LINE_ITEM_TABLE + " WHERE orderid = " + orderId;

        ArrayList lineItems = new ArrayList();
        Statement stmt = null;
        ResultSet result = null;
        try {
            stmt = dbConnection.createStatement();
            result = stmt.executeQuery(queryStr);
            if ( !result.next() )
                throw new AdminOrderDAOException("No Line Items for orderId: " +
                                   orderId);
            do {
                int lineNo = result.getInt(1);
                String itemNo = result.getString(2);
                int qty = result.getInt(3);
                double unitPrice = result.getFloat(4);
                LineItem LI = new LineItem(itemNo, qty, unitPrice, lineNo);
                lineItems.add(LI);
            } while (result.next());
        } catch(SQLException se) {
            throw new AdminOrderDAOException("SQLException while searching for "
                                            + "orders by status : \n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
        }
        return(lineItems);
    }

    private Collection getOrderDetails(String id)
                                throws AdminOrderDAOException {
        String queryStr = "SELECT "
            + "orderid,userid,shipaddr1,shipaddr2,"
            + "shipcity,shipstate,shipzip,shipcountry,"
            + "billaddr1,billaddr2,billcity,billstate,billzip,billcountry,"
            + "courier,totalprice,"
            + "shiptofirstname,shiptolastname,"
            + "billtofirstname,billtolastname,"
            + "creditcard,exprdate,cardtype,orderdate,locale"
            + " FROM " + DatabaseNames.ORDER_TABLE
            + " WHERE orderid IN (" + id + ")";

        ArrayList orderModels = new ArrayList();
        Statement stmt = null;
        ResultSet result = null;
        try {
            stmt = dbConnection.createStatement();
            result = stmt.executeQuery(queryStr);
            while(result.next()) {
                int orderId = result.getInt(1);
                String uid = result.getString(2).trim();
                String shipaddr1 = result.getString(3).trim();
                String shipaddr2 = result.getString(4);
                if (shipaddr2 != null)
                   shipaddr2 = shipaddr2.trim();
                else
                   shipaddr2 = "";
                String shipcity = result.getString(5).trim();
                String shipstate = result.getString(6).trim();
                String shipzip = result.getString(7).trim();
                String shipcountry = result.getString(8).trim();
                String billaddr1 = result.getString(9).trim();
                String billaddr2 = result.getString(10);
                if (billaddr2 != null)
                   billaddr2 = billaddr2.trim();
                else
                   billaddr2 = "";
                String billcity = result.getString(11).trim();
                String billstate = result.getString(12).trim();
                String billzip = result.getString(13).trim();
                String billcountry = result.getString(14).trim();
                String carrier = result.getString(15).trim();
                double price = result.getDouble(16);
                String stfname = result.getString(17).trim();
                String stlname = result.getString(18).trim();
                String btfname = result.getString(19).trim();
                String btlname = result.getString(20).trim();
                String ccard = result.getString(21).trim();
                String expr = result.getString(22).trim();
                String cardtype = result.getString(23).trim();
                Calendar orderDate = Calendar.getInstance();
                orderDate.setTime(result.getDate(24));
                String localeString = result.getString(25).trim();
                Locale locale = I18nUtil.getLocale(localeString);
                Collection items = getLineItemDetails(orderId);
                orderModels.add(new OrderModel(orderId, items,
                            new Address(shipaddr1, shipaddr2, shipcity,
                                    shipstate, shipzip, shipcountry),
                            new Address(billaddr1, billaddr2, billcity,
                                    billstate, billzip, billcountry),
                            stfname, stlname, btfname, btlname,
                            new CreditCard(ccard, cardtype, expr),
                            carrier, uid, orderDate, "P", price, locale));
            }
        } catch(SQLException se) {
            throw new AdminOrderDAOException("SQLException while getting "
                                    + "order details : \n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
        }
        return orderModels;
    }

    public Collection getAllPendingOrders(Locale locale) throws AdminOrderDAOException {
        String targetLocale = I18nUtil.getLocaleString(locale);
        String queryStr = "SELECT " + DatabaseNames.ORDER_STATUS_TABLE+ ".ORDERID" +
                             " FROM " + DatabaseNames.ORDER_STATUS_TABLE + " , ORDERS " +
                             " WHERE " + DatabaseNames.ORDER_STATUS_TABLE +".ORDERID " +
                             " = " + DatabaseNames.ORDER_TABLE + ".ORDERID " +
                             " AND " + DatabaseNames.ORDER_STATUS_TABLE  + ".STATUS = 'P'" +
                             " AND " + DatabaseNames.ORDER_TABLE + ".LOCALE" +
                             "  =  '" + targetLocale + "'";

        Statement stmt = null;
        ResultSet result = null;
        Collection orderIdList = null;
        try {
            getDBConnection();
            boolean first = true;
            stmt = dbConnection.createStatement();
            result = stmt.executeQuery(queryStr);
            if (result.next()) {
                String orderIds = "";
                do {
                    if(first)
                        first = false;
                    else
                        orderIds += ",";
                    orderIds += result.getInt(1);
                } while (result.next());
                orderIdList = getOrderDetails(orderIds);
            }
        } catch(SQLException se) {
            throw new AdminOrderDAOException("SQLException while searching for "
                                    + "orders by status : \n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
        return(orderIdList);
    }

    public void setNewStatus(ArrayList list) throws AdminOrderDAOException {

        String approveList = "";
        String denyList = "";
        int    approvedCount = 0;
        int    denyCount = 0;
        boolean approveFirst = true;
        boolean denyFirst = true;

        Iterator it = list.iterator();
        while (it.hasNext()){
            String orderId = (String)it.next();
            String newStatus = (String)it.next();
            if(newStatus.equals("approved")) {
                if(approveFirst)
                    approveFirst = false;
                else
                    approveList += ",";
                approveList += orderId;
                approvedCount++;
            }
            if(newStatus.equals("denied")) {
                if(denyFirst)
                    denyFirst = false;
                else
                    denyList += ",";
                denyList += orderId;
                denyCount++;
            }
        }
        if((approvedCount == 0) && (denyCount == 0))
            return;
        Statement stmt = null;
        try {
            getDBConnection();
            stmt = dbConnection.createStatement();

            String queryStr = "";
            int result;

            if(approvedCount != 0) {
                queryStr = "UPDATE orderstatus SET status = 'A' WHERE " +
                        "orderid IN (" + approveList + ")";

                result = stmt.executeUpdate(queryStr);
                if(result != approvedCount) {
                    throw new AdminOrderDAOException("Number of orders approved"
                                        + " does not match !!!!!");
                }
            }
            if(denyCount != 0) {
                queryStr = "UPDATE orderstatus SET status = 'D' WHERE " +
                        "orderid IN (" + denyList + ")";

                result = stmt.executeUpdate(queryStr);
                if(result != denyCount) {
                    throw new AdminOrderDAOException("Number of orders denied "
                                    + "does not match !!!!!");
                }
            }
        } catch(SQLException se) {
            throw new AdminOrderDAOException("SQLException while updating "
                                    + "orders status : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    protected void getDBConnection() throws AdminOrderDAOException {
        try {
            dbConnection = datasource.getConnection();
        } catch (SQLException se) {
            throw new AdminOrderDAOException("SQLException while getting " +
                                      "DB connection : \n" + se.getMessage());
        }
    }

    protected void closeResultSet(ResultSet rs) throws AdminOrderDAOException {
        try {
            if (rs!= null) {
                rs.close();
            }
        } catch(SQLException se) {
            throw new AdminOrderDAOException("SQLException while closing " +
                                "result set : \n" + se.getMessage());
        }
    }

    protected void closeStatement(Statement stmt)
                                throws AdminOrderDAOException {
        try {
            if (stmt!= null) {
                stmt.close();
            }
        } catch(SQLException se) {
            throw new AdminOrderDAOException("SQLException while closing " +
                                "result set : \n" + se.getMessage());
        }
    }

    protected void closeConnection() throws AdminOrderDAOException {
        try {
            if (dbConnection!= null && !dbConnection.isClosed()) {
                dbConnection.close();
                dbConnection = null;
            }
        } catch(SQLException se) {
            throw new AdminOrderDAOException("SQLException while closing " +
                                "DB connection : \n" + se.getMessage());
        }
    }
}


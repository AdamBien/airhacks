/*
 * $Id: OrderDAOCS.java,v 1.1.2.4 2001/03/15 03:50:33 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.j2ee.blueprints.customer.util.I18nUtil;
import com.sun.j2ee.blueprints.customer.util.DatabaseNames;
import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.order.model.LineItem;
import com.sun.j2ee.blueprints.customer.order.model.MutableOrderModel;

import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOSysException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOAppException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAODBUpdateException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class represents the implementation of the
 * create and store methods of the base class OrderDAOImpl for
 * Cloudscape.
 *
 * @see UUIDGenerator
 */
public class OrderDAOCS extends OrderDAOImpl {

    public OrderDAOCS() throws OrderDAOSysException {
        super();
    }

    /**
     * part of the OrderDAO abstract class
     */
    public int create(MutableOrderModel details) throws OrderDAOSysException,
                                         OrderDAODBUpdateException,
                                         OrderDAOAppException {
        int orderId = getUniqueOrderId();
        insertOrder(orderId, details);
        insertLineItem(orderId, details);
        insertOrderStatus(orderId, details);
        return(orderId);
    }

    private int getUniqueOrderId() throws OrderDAOSysException,
                                           OrderDAODBUpdateException {
        try {
            getDBConnection();
            return(UUIDGenerator.nextSeqNum(dbConnection));
        } catch(OrderDAODBUpdateException oddb) {
            throw new OrderDAODBUpdateException(oddb.getMessage());
        } catch(OrderDAOSysException se) {
            throw new OrderDAOSysException("SQLException while getting " +
                    "order ID : \n" + se);
        } finally {
            closeConnection();
        }
    }

    /**
     * part of the OrderDAO abstract class
     */
    public void store(MutableOrderModel details) throws OrderDAOSysException,
                               OrderDAODBUpdateException,
                               OrderDAOAppException {
        updateOrder(details);
        updateLineItem(details);
        updateOrderStatus(details);
    }

    private void insertOrder(int orderId, MutableOrderModel details) throws
                                                 OrderDAODBUpdateException,
                                                 OrderDAOAppException,
                                                 OrderDAOSysException {
        if (!isValidData(details))
            throw new OrderDAOAppException("Illegal data values for insert");

        String queryStr = "INSERT INTO " + DatabaseNames.ORDER_TABLE
            + "(orderid,userid,orderdate,"
            + "shipaddr1,shipaddr2,shipcity,shipstate,shipzip,shipcountry,"
            + "billaddr1,billaddr2,billcity,billstate,billzip,billcountry,"
            + "courier,totalprice,"
            + "shiptofirstname,shiptolastname,"
            + "billtofirstname,billtolastname,"
            + "creditcard,exprdate,cardtype, locale)" + "VALUES ("
            +  orderId + ","
            + "'" + details.getUserId().trim()  + "',"
            +  "'" + details.getOrderDate().getCloudscapeDateString() + "',"
            + "'"+details.getShipToAddr().getStreetName1().trim() +"',";

        if (details.getShipToAddr().getStreetName2() != null)
            queryStr += "'"+details.getShipToAddr().getStreetName2().trim() +"',";
        else
            queryStr += "' ',";

        queryStr +=  "'"+details.getShipToAddr().getCity().trim() + "',"
            + "'"+details.getShipToAddr().getState().trim() + "',"
            + "'"+details.getShipToAddr().getZipCode().trim() + "',"
            + "'"+details.getShipToAddr().getCountry().trim() + "',"
            + "'"+details.getBillToAddr().getStreetName1().trim() +"',";

        if (details.getBillToAddr().getStreetName2() != null)
            queryStr += "'"+details.getShipToAddr().getStreetName2().trim() +"',";
        else
            queryStr += "' ',";

        queryStr += "'"+details.getBillToAddr().getCity().trim() + "',"
            + "'"+details.getBillToAddr().getState().trim() + "',"
            + "'"+details.getBillToAddr().getZipCode().trim() + "',"
            + "'"+details.getBillToAddr().getCountry().trim() + "',"
            + "'"+details.getCarrier().trim() + "',"
            + details.getTotalPrice() + ","
            + "'"+details.getShipToFirstName().trim() + "',"
            + "'"+details.getShipToLastName().trim() + "',"
            + "'"+details.getBillToFirstName().trim() + "',"
            + "'"+details.getBillToLastName().trim() + "',"
            + "'"+details.getCreditCard().getCardNo().trim() + "',"
            + "'"+details.getCreditCard().getExpiryDateString().trim() + "',"
            + "'"+details.getCreditCard().getCardType().trim() + "',"
            + "'"+I18nUtil.getLocaleString(details.getLocale()) + "' )";
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection, queryStr);
            int resultCount = stmt.executeUpdate();

            if ( resultCount != 1 )
                throw new OrderDAODBUpdateException(
                    "ERROR in ORDER_TABLE INSERT !! resultCount = "
                        + resultCount);
        } catch(SQLException se) {
            Debug.println("!!!!SQL Ex while insertOrder : " + se);
            throw new OrderDAOSysException("SQLException while inserting " +
                    "order " + orderId + " : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void insertLineItem(int orderId, MutableOrderModel details) throws
                                         OrderDAOSysException,
                                         OrderDAODBUpdateException,
                                         OrderDAOAppException {
        LineItem LI;
        int resultCount;

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            for (Iterator it = details.getLineItems().iterator() ; it.hasNext() ;) {
                LI = (LineItem) it.next();
                if (LI == null)
                    throw new OrderDAOAppException("LineItem is null");
                String queryStr = "INSERT INTO "
                    + DatabaseNames.LINE_ITEM_TABLE
                    + "(orderid,linenum,itemid,quantity,unitprice) VALUES ("
                    + orderId + ","
                    + LI.getLineNo() + ","
                    + "'"+ LI.getItemNo().trim() + "',"
                    + LI.getQty() + ","
                    + LI.getUnitPrice() + ")";
                Debug.println("queryString is: "+ queryStr);
                stmt = createPreparedStatement(dbConnection, queryStr);
                resultCount = stmt.executeUpdate();
                if ( resultCount != 1 )
                    throw new OrderDAODBUpdateException
                        ("ERROR in LINE_ITEM_TABLE INSERT !! resultCount = "
                            + resultCount);
            }
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while inserting " +
                    "line item for order " + orderId + " : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void insertOrderStatus(int orderId, MutableOrderModel details)
                                throws OrderDAOSysException,
                                           OrderDAODBUpdateException {

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            int lineNum = UUIDGenerator.nextSeqNum(dbConnection);

            String queryStr = "INSERT INTO "
                + DatabaseNames.ORDER_STATUS_TABLE
                + "(orderid,linenum,updatedate,status) VALUES ("
                + orderId + ","
                + lineNum + ","
                + " current_date,"
                + "'" + details.getStatus().trim() + "')";
            Debug.println("queryString is: "+ queryStr);
            stmt = createPreparedStatement(dbConnection, queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount != 1 )
                throw new OrderDAODBUpdateException
                    ("ERROR in ORDER_STATUS_TABLE INSERT !! resultCount = "
                        + resultCount);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while inserting " +
                    "order status : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void updateOrder(MutableOrderModel details) throws
                                      OrderDAODBUpdateException,
                                      OrderDAOSysException {
        String queryStr = "UPDATE " + DatabaseNames.ORDER_TABLE
            + " SET userid = " + "'" + details.getUserId().trim() + "',"
            + "orderdate = " + "'" + details.getOrderDate().getCloudscapeDateString()+ "',"
            + "shipaddr1 = " + "'" + details.getShipToAddr().getStreetName1().trim() + "',";

        if (details.getShipToAddr().getStreetName2() != null)
            queryStr += "shipaddr2 = " + "'"
                    + details.getShipToAddr().getStreetName2().trim() + "',";
        else
            queryStr += "shipaddr2 = ' ',";

        queryStr += "shipcity = " + "'" + details.getShipToAddr().getCity().trim() + "',"
            + "shipstate = " + "'" + details.getShipToAddr().getState().trim() + "',"
            + "shipzip = " + "'" + details.getShipToAddr().getZipCode().trim() + "',"
            + "shipcountry = " + "'" + details.getShipToAddr().getCountry().trim() + "',"
            + "billaddr1 = " + "'" + details.getBillToAddr().getStreetName1().trim() + "',";

        if (details.getBillToAddr().getStreetName2() != null)
            queryStr += "billaddr2 = " + "'"
                + details.getBillToAddr().getStreetName2().trim() + "',";
        else
            queryStr += "billaddr2 = ' ',";

        queryStr +=  "billcity = " + "'" + details.getBillToAddr().getCity().trim() + "',"
            + "billstate = " + "'" + details.getBillToAddr().getState().trim() + "',"
            + "billzip = " + "'" + details.getBillToAddr().getZipCode().trim() + "',"
            + "billcountry = " + "'" + details.getBillToAddr().getCountry().trim() + "',"
            + "courier = " + "'" + details.getCarrier().trim() + "',"
            + "totalprice = " + formatPrice(details.getTotalPrice()) + ","
            + "shiptofirstname = " + "'" + details.getShipToFirstName().trim() + "',"
            + "shiptolastname = " + "'" + details.getShipToLastName().trim() + "',"
            + "billtofirstname = " + "'" + details.getBillToFirstName().trim() + "',"
            + "billtolastname = " + "'" + details.getBillToLastName().trim() + "',"
            + "creditcard = " + "'" + details.getCreditCard().getCardNo().trim() + "',"
            + "exprdate = " + "'" + details.getCreditCard().getExpiryDateString().trim() + "',"
            + "cardtype = " + "'" + details.getCreditCard().getCardType().trim()+ "',"
            + "locale = " + "'" + I18nUtil.getLocaleString(details.getLocale())+ "'"
            + " WHERE orderid = " + details.getOrderId();
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection, queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount != 1 )
                throw new OrderDAODBUpdateException
                    ("ERROR updating order in ORDER_TABLE!! resultCount = " +
                             resultCount);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while updating " +
                    "order : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private String formatPrice(double price){
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        DecimalFormat df = (DecimalFormat)nf;
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        String pattern = "##.00";
        df.applyPattern(pattern);
        df.setDecimalSeparatorAlwaysShown(true);
        return df.format(price);
    }

    private void updateLineItem(MutableOrderModel details) throws
                                         OrderDAOSysException,
                                         OrderDAOAppException,
                                         OrderDAODBUpdateException {
        LineItem LI;
        int resultCount;

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            for (Iterator it = details.getLineItems().iterator() ; it.hasNext() ;) {
                LI = (LineItem) it.next();
                if (LI == null)
                    throw new OrderDAOAppException("LineItem is null");
                String queryStr = "UPDATE "+ DatabaseNames.LINE_ITEM_TABLE
                    + " SET itemid = " + "'"+ LI.getItemNo().trim() + "',"
                    + "quantity = " + LI.getQty() + ","
                    + "unitprice = " + LI.getUnitPrice()
                    + " WHERE orderid = " + details.getOrderId()
                    + " AND linenum = " + LI.getLineNo();
                Debug.println("queryString is: "+ queryStr);
                stmt = createPreparedStatement(dbConnection, queryStr);
                resultCount = stmt.executeUpdate();
                if ( resultCount != 1 )
                    throw new OrderDAODBUpdateException
                        ("ERROR updating LINE_ITEM_TABLE  !! resultCount = "
                                + resultCount);
            }
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while updating " +
                    "line item : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void updateOrderStatus(MutableOrderModel details) throws
                                        OrderDAOSysException,
                                        OrderDAODBUpdateException {

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            String queryStr = "UPDATE " + DatabaseNames.ORDER_STATUS_TABLE
                + " SET status = " + "'"+ details.getStatus().trim() + "'"
                + " WHERE orderid = " + details.getOrderId();
            Debug.println("queryString is: "+ queryStr);
            stmt = createPreparedStatement(dbConnection,queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount != 1 )
                throw new OrderDAODBUpdateException
                    ("ERROR updating ORDER_STATUS_TABLE !! resultCount = "
                            + resultCount);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while updating " +
                    "order status : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }
}

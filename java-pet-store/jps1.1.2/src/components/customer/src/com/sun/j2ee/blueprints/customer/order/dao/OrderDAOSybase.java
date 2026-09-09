/*
 * $Id: OrderDAOSybase.java,v 1.1.2.5 2001/04/07 00:21:01 inder Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.dao;

import java.util.Iterator;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Locale;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.j2ee.blueprints.customer.order.model.LineItem;
import com.sun.j2ee.blueprints.customer.order.model.MutableOrderModel;
import com.sun.j2ee.blueprints.customer.util.DatabaseNames;
import com.sun.j2ee.blueprints.customer.util.I18nUtil;

import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOSysException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOAppException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAODBUpdateException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class represents the implementation of the
 * create and store methods of the base class OrderDAOImpl for
 *  Sybase.
 */
public class OrderDAOSybase extends OrderDAOImpl {

    public OrderDAOSybase() throws OrderDAOSysException {
        super();
    }

    /**
    * part of the OrderDAO abstract class
    */
    public int create(MutableOrderModel details) throws OrderDAOSysException,
                                         OrderDAODBUpdateException,
                                         OrderDAOAppException {
        int orderId = insertOrder(details);
        insertLineItem(orderId, details);
        insertOrderStatus(orderId, details);
        return(orderId);
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

    private int insertOrder(MutableOrderModel details) throws
                                                 OrderDAODBUpdateException,
                                                 OrderDAOAppException,
                                                 OrderDAOSysException {
        PreparedStatement stmt = null;
        int oid = -1;

        if (!isValidData(details))
            throw new OrderDAOAppException ("Illegal data values for insert");

        getDBConnection();
        String queryStr = "INSERT INTO " + DatabaseNames.ORDER_TABLE +
            "(userid,orderdate,"
            + "shipaddr1,shipaddr2,shipcity,shipstate,shipzip,shipcountry,"
            + "billaddr1,billaddr2,billcity,billstate,billzip,billcountry,"
            + "courier,totalprice,"
            + "shiptofirstname,shiptolastname,"
            + "billtofirstname,billtolastname,"
            + "creditcard,exprdate,cardtype,locale)" + "VALUES ("
            + "'" + details.getUserId().trim() + "',"
            + "'"+ details.getOrderDate().getFullDateString().trim()+ "',"
            + "'"+ details.getShipToAddr().getStreetName1().trim() +"',";

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
            + "CONVERT(DECIMAL(10,2)," +details.getTotalPrice() + "),"
            + "'"+details.getShipToFirstName().trim() + "',"
            + "'"+details.getShipToLastName().trim() + "',"
            + "'"+details.getBillToFirstName().trim() + "',"
            + "'"+details.getBillToLastName().trim() + "',"
            + "'"+details.getCreditCard().getCardNo().trim() + "',"
            + "'"+details.getCreditCard().getExpiryDateString().trim() + "',"
            + "'"+details.getCreditCard().getCardType().trim()+ "',"
            + "'"+I18nUtil.getLocaleString(details.getLocale()) + "' )";

        Debug.println("queryString is: "+ queryStr);
        int resultCount = 0;
        try {
           stmt = dbConnection.prepareStatement(queryStr);
           resultCount = stmt.executeUpdate();
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while executing update for statement");
        }

        try{
           if ( resultCount != 1 )
               throw new OrderDAODBUpdateException(
                "ERROR in ORDER_TABLE INSERT !! resultCount = " + resultCount);
           else {
               queryStr = "SELECT @@identity";
               stmt = dbConnection.prepareStatement(queryStr);
               ResultSet rs = stmt.executeQuery();
               if ( !rs.next() ) {
                  throw new OrderDAOAppException("ERROR in selecting OrderId !!");
               } else {
                   oid = rs.getInt(1);
                   if (oid < 1)
                       throw new OrderDAOAppException
                           ("ERROR in getting OrderId !! orderId = "+ oid);
               }
            }
          } catch(SQLException se) {
               throw new OrderDAOSysException("SQLException while inserting " +
                    "order " + oid + " : \n" + se);
          } finally {
             try{
                 if(stmt != null) stmt.close();
                 closeConnection();
             } catch(SQLException se) {
                 throw new OrderDAOSysException("SQLException while closing statement and connection");
             }
          }
        return(oid);
    }

    private void insertLineItem(int orderId, MutableOrderModel details) throws
                            OrderDAOSysException,
                            OrderDAOAppException, OrderDAODBUpdateException {
        LineItem LI;
        int resultCount;

        try {
            getDBConnection();
            PreparedStatement stmt = null;
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
                stmt = dbConnection.prepareStatement(queryStr);
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
            closeConnection();
        }
    }

    private void insertOrderStatus(int orderId, MutableOrderModel details)
                                           throws OrderDAOSysException,
                                           OrderDAODBUpdateException  {

        try {
            getDBConnection();
            PreparedStatement stmt = null;
            String queryStr = "INSERT INTO "
                + DatabaseNames.ORDER_STATUS_TABLE
                + "(orderid,timestamp,status) VALUES ("
                + orderId + ","
                + "getdate(),"
                + "'" + details.getStatus().trim() + "')";
            Debug.println("queryString is: "+ queryStr);
            stmt = dbConnection.prepareStatement(queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount != 1 )
                throw new OrderDAODBUpdateException
                    ("ERROR in ORDER_STATUS_TABLE INSERT !! resultCount = "
                        + resultCount);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while inserting " +
                    "order status for order " + orderId + " : \n" + se);
        } finally {
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

    private void updateOrder(MutableOrderModel ordr) throws
                   OrderDAODBUpdateException, OrderDAOSysException{

        String queryStr = "UPDATE " + DatabaseNames.ORDER_TABLE
            + " SET userid  = " + "'" + ordr.getUserId().trim() + "',"
            + "orderdate = " + "'" + ordr.getOrderDate().getFullDateString().trim() + "',"
            + "shipaddr1 = " + "'" + ordr.getShipToAddr().getStreetName1().trim() + "',";

        if (ordr.getShipToAddr().getStreetName2() != null)
            queryStr += "shipaddr2 = " +
                "'" + ordr.getShipToAddr().getStreetName2().trim() + "',";
        else
            queryStr += "shipaddr2 = ' ',";

        queryStr += "shipcity = " + "'" + ordr.getShipToAddr().getCity().trim() + "',"
            + "shipstate = " + "'" + ordr.getShipToAddr().getState().trim() + "',"
            + "shipzip = " + "'" + ordr.getShipToAddr().getZipCode().trim() + "',"
            + "shipcountry = " + "'" + ordr.getShipToAddr().getCountry().trim() + "',"
            + "billaddr1 = " + "'" + ordr.getBillToAddr().getStreetName1().trim() + "',";

        if (ordr.getBillToAddr().getStreetName2() != null)
            queryStr += "billaddr2 = " + "'"
                + ordr.getBillToAddr().getStreetName2().trim() + "',";
        else
            queryStr += "billaddr2 = ' ',";

        queryStr +=  "billcity = " + "'" + ordr.getBillToAddr().getCity().trim() + "',"
            + "billstate = " + "'" + ordr.getBillToAddr().getState().trim() + "',"
            + "billzip = " + "'" + ordr.getBillToAddr().getZipCode().trim() + "',"
            + "billcountry = " + "'" + ordr.getBillToAddr().getCountry().trim() + "',"
            + "courier = " + "'" + ordr.getCarrier().trim() + "',"
            + "totalprice = " + "CONVERT(DECIMAL(10,2),"
            + formatPrice(ordr.getTotalPrice()) + "),"
            + "shiptofirstname = " + "'" + ordr.getShipToFirstName().trim() + "',"
            + "shiptolastname = " + "'" + ordr.getShipToLastName().trim() + "',"
            + "billtofirstname = " + "'" + ordr.getBillToFirstName().trim() + "',"
            + "billtolastname = " + "'" + ordr.getBillToLastName().trim() + "',"
            + "creditcard = " + "'" + ordr.getCreditCard().getCardNo().trim() + "',"
            + "exprdate = " + "'" + ordr.getCreditCard().getExpiryDateString().trim() + "',"
            + "cardtype = " + "'" + ordr.getCreditCard().getCardType().trim() + "', "
            + "locale = " + "'" + I18nUtil.getLocaleString(ordr.getLocale()) + "'"
            + " WHERE orderid = " + ordr.getOrderId();

        Debug.println("queryString is: "+ queryStr);
        try {
            getDBConnection();
            PreparedStatement stmt = dbConnection.prepareStatement(queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount != 1 )
                throw new OrderDAODBUpdateException
                    ("ERROR updating order in ORDER_TABLE!! resultCount = " +
                         resultCount);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while updating " +
                    "order : \n" + se);
        } finally {
            closeConnection();
        }
    }

    private void updateLineItem(MutableOrderModel ordr) throws
                                         OrderDAOSysException,
                                         OrderDAOAppException,
                                         OrderDAODBUpdateException  {
        LineItem LI;
        int resultCount;

        try {
            getDBConnection();
            PreparedStatement stmt = null;
            for (Iterator it = ordr.getLineItems().iterator() ; it.hasNext() ;) {
                LI = (LineItem) it.next();
                if (LI == null)
                    throw new OrderDAOAppException("LineItem is null");
                String queryStr = "UPDATE "+ DatabaseNames.LINE_ITEM_TABLE
                    + " SET itemid = " + "'"+ LI.getItemNo().trim() + "',"
                    + "quantity = " + LI.getQty() + ","
                    + "unitprice = " + "CONVERT(DECIMAL(10,2),"
                    + LI.getUnitPrice() + ")"
                    + " WHERE orderid = " + ordr.getOrderId()
                    + " AND linenum = " + LI.getLineNo();

                Debug.println("queryString is: "+ queryStr);
                stmt = dbConnection.prepareStatement(queryStr);
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
            closeConnection();
        }
    }

    private void updateOrderStatus(MutableOrderModel ordr) throws
                                        OrderDAOSysException,
                                        OrderDAODBUpdateException  {

        try {
            getDBConnection();
            PreparedStatement stmt = null;
            String queryStr = "UPDATE " + DatabaseNames.ORDER_STATUS_TABLE
                + " SET status = " + "'"+ ordr.getStatus().trim() + "'"
                + " WHERE orderid = " + ordr.getOrderId();
            Debug.println("queryString is: "+ queryStr);
            stmt = dbConnection.prepareStatement(queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount != 1 )
                throw new OrderDAODBUpdateException
                    ("ERROR updating ORDER_STATUS_TABLE !! resultCount = "
                            + resultCount);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while updating " +
                    "order status : \n" + se);
        } finally {
            closeConnection();
        }
    }
}

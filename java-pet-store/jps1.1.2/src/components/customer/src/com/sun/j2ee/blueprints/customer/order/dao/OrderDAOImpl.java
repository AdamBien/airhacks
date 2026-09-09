/*
 * $Id: OrderDAOImpl.java,v 1.1.2.4 2001/03/15 03:50:34 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Collection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.sun.j2ee.blueprints.customer.util.JNDINames;
import com.sun.j2ee.blueprints.customer.util.I18nUtil;
import com.sun.j2ee.blueprints.customer.util.DatabaseNames;
import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.util.Calendar;
import com.sun.j2ee.blueprints.customer.util.CreditCard;
import com.sun.j2ee.blueprints.customer.order.model.LineItem;
import com.sun.j2ee.blueprints.customer.order.model.MutableOrderModel;
import com.sun.j2ee.blueprints.customer.order.dao.OrderDAO;

import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOSysException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOAppException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAODBUpdateException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAODupKeyException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderDAOFinderException;

import com.sun.j2ee.blueprints.util.tracer.Debug;
/**
 * This class implements OrderDAO for Oracle, Sybase and cloudscape databases.
 * This class has the common code for the above three DBMS. The create and
 * store methods are implemented in database specific classes OrderDAOXXX.java.
 */
public class OrderDAOImpl implements OrderDAO {

    protected transient Connection dbConnection = null;
    private transient DataSource datasource   = null;

    public OrderDAOImpl() throws OrderDAOSysException {
        try {
            InitialContext ic = new InitialContext();
            datasource  = (DataSource) ic.lookup(JNDINames.ESTORE_DATASOURCE);
        } catch (NamingException ne) {
            throw new OrderDAOSysException("NamingException while looking" +
                                               " up DataSource Connection " +
                                             JNDINames.ESTORE_DATASOURCE +
                                                    ": \n" + ne.getMessage());
        }
    }


    public int create(MutableOrderModel details) throws OrderDAOSysException,
                                         OrderDAODBUpdateException,
                                         OrderDAOAppException {
        return(details.getOrderId());
    };

    public void store(MutableOrderModel details) throws OrderDAOSysException,
                                        OrderDAOAppException,
                                        OrderDAODBUpdateException {};

    public MutableOrderModel load(int orderId) throws OrderDAOFinderException,
                              OrderDAOSysException {
        MutableOrderModel details = selectOrder(orderId);
        details.setLineItem(selectLineItem(orderId));
        details.setStatus(selectOrderStatus(orderId));
        return(details);
    }

    public void remove(int orderId) throws OrderDAODBUpdateException,
                                OrderDAOSysException {
        deleteLineItem(orderId);
        deleteOrderStatus(orderId);
        deleteOrder(orderId);
    }

    public Integer findByPrimaryKey(int orderId) throws OrderDAOSysException,
                                             OrderDAOFinderException {
        if (orderIdExists(orderId))
            return (new Integer(orderId));
        throw new OrderDAOFinderException("primary key not found:" + orderId);
    }

    public Collection findUserOrders(String userId) throws
                                              OrderDAOFinderException,
                                              OrderDAOSysException {
        return(getOrderIds(userId));
    }


    private boolean orderIdExists (int orderId) throws OrderDAOSysException {
        String queryStr ="SELECT orderid FROM " +
                DatabaseNames.ORDER_TABLE + " WHERE orderid = " + orderId;
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        ResultSet result = null;
        boolean returnValue = false;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection,queryStr);
            result = stmt.executeQuery();
            if ( !result.next() ) {
                returnValue = false;
            } else {
                returnValue = true;
            }
            return returnValue;
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while searching for " +
                        "order ID " + orderId + ": \n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
    }

    private Collection getOrderIds(String userId) throws
                                              OrderDAOFinderException,
                                              OrderDAOSysException {
        String queryStr ="SELECT orderid FROM " +
            DatabaseNames.ORDER_TABLE
                + " WHERE userid = " + "'" + userId.trim() +"'";
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        ResultSet result = null;
        ArrayList orderIdList = new ArrayList();
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection,queryStr);
            result = stmt.executeQuery();
            if ( !result.next() ) {
                throw new OrderDAOFinderException("No Orders found for user: " +
                                                                userId);
            } else {
                do {
                    orderIdList.add( new Integer(result.getInt(1)));
                } while (result.next());
            }
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while searching for " +
                        "orders for user " + userId + " : \n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
        return orderIdList;
    }

    private MutableOrderModel selectOrder(int orderId) throws
                              OrderDAOFinderException,
                              OrderDAOSysException {
        String queryStr = "SELECT "
            + "userid,orderdate,shipaddr1,shipaddr2,"
            + "shipcity,shipstate,shipzip,shipcountry,"
            + "billaddr1,billaddr2,billcity,billstate,billzip,billcountry,"
            + "courier,totalprice,"
            + "shiptofirstname,shiptolastname,"
            + "billtofirstname,billtolastname,"
            + "creditcard,exprdate,cardtype,locale"
            + " FROM " + DatabaseNames.ORDER_TABLE
        + " WHERE orderid = " + orderId;
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection,queryStr);
            result = stmt.executeQuery();

            if ( !result.next() )
                throw new OrderDAOFinderException("No record for primary key" +
                                   orderId);
            int i = 1;
            String userId = result.getString(i++);
            Calendar orderDate = Calendar.getInstance();
            orderDate.setTime(result.getDate(i++));

            String street1 = result.getString(i++);
            String street2 = result.getString(i++);
            String city = result.getString(i++);
            String state = result.getString(i++);
            String zip = result.getString(i++);
            String country = result.getString(i++);
            Address shipToAddr = new Address(street1,street2,city,state,zip,country);

            street1 = result.getString(i++);
            street2 = result.getString(i++);
            city = result.getString(i++);
            state = result.getString(i++);
            zip = result.getString(i++);
            country = result.getString(i++);
            Address billToAddr = new Address(street1,street2,city,state,zip,country);

            String carrier = result.getString(i++);
            double totalPrice = result.getFloat(i++);

            String shipToFirstName = result.getString(i++);
            String shipToLastName = result.getString(i++);
            String billToFirstName = result.getString(i++);
            String billToLastName = result.getString(i++);

            String cardNo = result.getString(i++);
            String expiryDate = result.getString(i++);
            String cardType = result.getString(i++);
            String localeString = result.getString(i++);
            //Locale locale = new Locale(country, language);
            Locale locale = I18nUtil.getLocale(localeString);
            CreditCard chargeCard = new CreditCard(cardNo,cardType,expiryDate);
            return(new MutableOrderModel(orderId, null, shipToAddr, billToAddr,
                          shipToFirstName, shipToLastName,
                          billToFirstName, billToLastName, chargeCard,
                          carrier, userId, orderDate, null,totalPrice, locale));
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while searching for "+
                        "order ID " + orderId + " : \n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
    }

    private Collection selectLineItem(int orderId) throws
                              OrderDAOFinderException,
                              OrderDAOSysException {

        String queryStr = "SELECT "+
            "linenum,itemid,quantity,unitprice"
            + " FROM " + DatabaseNames.LINE_ITEM_TABLE
        + " WHERE orderid = " + orderId;
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection,queryStr);
            ArrayList lineItemList = new ArrayList();
            result = stmt.executeQuery();
            if ( !result.next() )
                throw new OrderDAOFinderException("No Line Items for orderId: "
                                                  + orderId);
            do {
                int lineNo = result.getInt(1);
                String itemNo = result.getString(2);
                int qty = result.getInt(3);
                double unitPrice = result.getFloat(4);
                LineItem LI = new LineItem(itemNo, qty, unitPrice, lineNo);
                lineItemList.add(LI);
            } while (result.next());
            return(lineItemList);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while searching for "+
                        "orders by status : \n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
    }

    private String selectOrderStatus(int orderId) throws
                              OrderDAOFinderException,
                              OrderDAOSysException {

        String queryStr = "SELECT "
            + "status" + " FROM " + DatabaseNames.ORDER_STATUS_TABLE
                + " WHERE orderid = " + orderId;
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection,queryStr);
            result = stmt.executeQuery();
            if ( !result.next() )
                throw new OrderDAOFinderException(
                        "Order status not found for orderId: " + orderId);
            return(result.getString(1));
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while searching for "
                        + "status of order " + orderId + " : \n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void deleteOrder(int orderId) throws OrderDAODBUpdateException,
                                OrderDAOSysException {
        String queryStr = "DELETE FROM " + DatabaseNames.ORDER_TABLE
                + " WHERE orderid = " + orderId;
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection,queryStr);
            int resultCount = stmt.executeUpdate();

            if ( resultCount != 1 )
                throw new OrderDAODBUpdateException
                ("ERROR deleteing order from ORDER_TABLE!! resultCount = " +
                                         resultCount);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while deleting " +
                        "orders ID " + orderId + " : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void deleteLineItem(int orderId) throws OrderDAODBUpdateException,
                                         OrderDAOSysException {
        String queryStr = "DELETE FROM " + DatabaseNames.LINE_ITEM_TABLE
                        + " WHERE orderid = " + orderId;
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection,queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount < 1 )
                throw new OrderDAODBUpdateException
                    ("ERROR deleteing line items!! resultCount = " +
                                         resultCount);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while removing LItem " +
                        "order ID " + orderId + " : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void deleteOrderStatus(int orderId) throws
                                OrderDAODBUpdateException,
                                OrderDAOSysException {
        String queryStr = "DELETE FROM " + DatabaseNames.ORDER_STATUS_TABLE
                    + " WHERE orderid = " + orderId;
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection,queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount < 1 )
                throw new OrderDAODBUpdateException
                    ("ERROR deleteing order status!! resultCount = " +
                         resultCount);
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while deleting " +
                        "orders by status; Id " + orderId + " : \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    protected boolean isValidData(MutableOrderModel order) {
        if ( (order.getUserId() == null) ||
             (order.getLineItems()  == null) ||
             (order.getLineItems().size()  == 0) ||
             (order.getOrderDate()  == null) ||
             (order.getShipToAddr().getStreetName1() == null) ||
             (order.getShipToAddr().getCity() == null) ||
             (order.getShipToAddr().getState() == null) ||
             (order.getShipToAddr().getZipCode() == null) ||
             (order.getShipToAddr().getZipCode() == null) ||
             (order.getShipToAddr().getCountry() == null) ||
             (order.getBillToAddr().getStreetName1() == null) ||
             (order.getBillToAddr().getCity() == null) ||
             (order.getBillToAddr().getState() == null) ||
             (order.getBillToAddr().getZipCode() == null) ||
             (order.getBillToAddr().getCountry() == null) ||
             (order.getShipToFirstName() == null) ||
             (order.getShipToLastName() == null) ||
             (order.getBillToFirstName() == null) ||
             (order.getBillToLastName() == null) ||
             (order.getCarrier() == null) ||
             (order.getCreditCard().getCardNo() == null) ||
             (order.getCreditCard().getCardType() == null) ||
             (order.getCreditCard().getExpiryDateString() == null) )

            return false;
        else
            return true;
    }

    protected void getDBConnection() throws OrderDAOSysException {
        try {
            dbConnection = datasource.getConnection();
        } catch (SQLException se) {
            throw new OrderDAOSysException("SQLException while getting " +
                                      "DB connection : \n" + se.getMessage());
        }
    }

    protected void closeConnection() throws OrderDAOSysException {
        try {
            if (dbConnection!= null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch(SQLException se) {
            throw new OrderDAOSysException("SQLException while closing " +
                                "DB connection : \n" + se.getMessage());
        }
    }

    protected void closeResultSet(ResultSet result) throws OrderDAOSysException {
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
            throw new OrderDAOSysException("SQL Exception while closing " +
                                        "Result Set : \n" + se);
        }
    }

    protected void closeStatement(PreparedStatement stmt) throws OrderDAOSysException {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
            throw new OrderDAOSysException("SQL Exception while closing " +
                                        "Statement : \n" + se);
        }
    }

    /**
     * This method allows us to create a prepared search statement that will be friendly
     * To Japanese in cloudscape and other databases.
     * Basically we use a prepared statement that contants '?' where Japanese characters
     * may occur and then we use the stmt.setString(index, "search string")
     *
     * This technique should not affect the English searchs.
     *
    */

    protected PreparedStatement createPreparedStatement(Connection con, String querry)
            throws SQLException {
        ArrayList targetStrings = new ArrayList();
        String processedQuerry = "";
        int startIndex = 0;
        if (startIndex != -1) {
            int index = startIndex;
            int literalStart = -1;
            while (index < querry.length()) {
                if (querry.charAt(index) == '\'') {
                    if (literalStart == -1 && index + 1 < querry.length()) {
                        literalStart = index +1;
                    } else {
                        String targetString = querry.substring(literalStart, index);
                        targetStrings.add(targetString);
                        literalStart = -1;
                        processedQuerry += "?";
                        index++;
                    }
                }
               if (index < querry.length() && literalStart == -1) {
                    processedQuerry += querry.charAt(index);
                }
                index++;
            }
            PreparedStatement stmt = con.prepareStatement(processedQuerry + " ");
            Iterator it = targetStrings.iterator();
            int counter =1;
            while (it.hasNext()) {
                String arg = (String)it.next();
                stmt.setString(counter++, arg);
            }
            return stmt;
        } else {
            PreparedStatement stmt = con.prepareStatement(querry);
            return stmt;
        }
    }
}


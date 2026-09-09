/*
 * $Id: AccountDAOImpl.java,v 1.1.2.4 2001/03/15 03:50:32 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.account.dao;

import java.sql.Connection;
//import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;

import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;

import com.sun.j2ee.blueprints.customer.util.JNDINames;
import com.sun.j2ee.blueprints.customer.account.dao.AccountDAO;
import com.sun.j2ee.blueprints.customer.util.DatabaseNames;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.account.model.AccountModel;

import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAOSysException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAOAppException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAODBUpdateException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAOFinderException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountDAODupKeyException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class implements AccountDAO for Oracle, Sybase and cloudscape databases
 * This class encapsulates all the JDBC calls made by the AccountEJB.
 * Actual logic of inserting/fetching/updating/deleting  the data in
 * relational database tables to mirror the state of AccountEJB is
 * implemented here.
 */
public class AccountDAOImpl implements AccountDAO {

    private transient Connection dbConnection = null;
    private transient DataSource datasource   = null;

    public AccountDAOImpl() throws AccountDAOSysException {
        try {
            InitialContext ic = new InitialContext();
            datasource = (DataSource) ic.lookup(JNDINames.ESTORE_DATASOURCE);
        } catch (NamingException ne) {
            throw new AccountDAOSysException("Naming Exception while looking "
                                             + " up DataSource Connection " +
                                                JNDINames.ESTORE_DATASOURCE +
                                                    ": \n" + ne.getMessage());
        }
    }

    public void create(AccountModel details) throws AccountDAOSysException,
                                AccountDAODupKeyException,
                                AccountDAODBUpdateException,
                                AccountDAOAppException {
        insertAccount(details);
    }

    public AccountModel load(String id) throws AccountDAOSysException,
                              AccountDAOFinderException {
        return(selectAccount(id));
    }

    public void store(AccountModel details) throws AccountDAODBUpdateException,
                               AccountDAOAppException,
                               AccountDAOSysException  {
        updateAccount(details);
    }

    public void remove(String id) throws AccountDAODBUpdateException,
                                AccountDAOSysException {
        deleteAccount(id);
    }

    public String findByPrimaryKey(String userId) throws
                                            AccountDAOFinderException,
                                            AccountDAOSysException {
        if (userExists(userId))
            return (userId);
        throw new AccountDAOFinderException("primary key not found :"+userId);
    }

    private boolean userExists (String userId) throws AccountDAOSysException {
        PreparedStatement stmt = null;
        ResultSet result = null;
        boolean returnValue = false;
        String queryStr ="SELECT userid FROM " +
                    DatabaseNames.ACCOUNT_TABLE
                        + " WHERE userid = " + "'" + userId.trim() + "'";
        Debug.println("queryString is: "+ queryStr);

        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection, queryStr);
            result = stmt.executeQuery();
            if ( !result.next() ) {
                returnValue = false;
            } else {
                userId = result.getString(1);
                returnValue = true;
            }
        } catch(SQLException se) {
            throw new AccountDAOSysException(
                           "SQLException while checking for an"
                           + " existing user - id -> " + userId + " :\n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
        return returnValue;
    }

    private boolean isValidData(String userId, ContactInformation info) {
        if ( (userId == null) ||
             ( info.getEMail() == null) ||
             (info.getGivenName() == null) || (info.getFamilyName() == null)
             || (info.getAddress().getStreetName1() == null) ||
             (info.getAddress().getCity() == null) ||
             (info.getAddress().getState() == null) ||
             (info.getAddress().getZipCode() == null) ||
             (info.getAddress().getCountry() == null)
             || (info.getTelephone() == null) )
            return (false);
        else
            return (true);
    }

    private void insertAccount(AccountModel details) throws
                                 AccountDAOSysException,
                                 AccountDAODupKeyException,
                                 AccountDAODBUpdateException,
                                 AccountDAOAppException {

        if (!isValidData(details.getUserId(), details.getContactInformation()))
            throw new AccountDAOAppException("Illegal data values for insert");
        if (userExists(details.getUserId()))
            throw new AccountDAODupKeyException("Account exists for "+
                                                details.getUserId());

        PreparedStatement stmt = null;
        ContactInformation info = details.getContactInformation();
        String queryStr = "INSERT INTO " + DatabaseNames.ACCOUNT_TABLE +
            "(userid,email,firstname,lastname,status,"
            + "addr1,addr2,city,state,zip,country,"
            + "phone)" + "VALUES ("
            + "'" + details.getUserId().trim() + "',"
            + "'" + info.getEMail().trim() + "',"
            + "'" + info.getGivenName().trim() + "',"
            + "'" + info.getFamilyName().trim() + "',"
            + "'" + details.getStatus().trim() + "',"
            + "'" + info.getAddress().getStreetName1().trim() +"',";

        if (info.getAddress().getStreetName2() != null)
            queryStr += "'"+info.getAddress().getStreetName2().trim() +"',";
        else
            queryStr += "' ',";

        queryStr +=  "'" + info.getAddress().getCity().trim() + "',"
            + "'" + info.getAddress().getState().trim() + "',"
            + "'" + info.getAddress().getZipCode().trim() + "',"
            + "'" + info.getAddress().getCountry().trim() + "',"
            + "'" + info.getTelephone().trim() + "' )";

        Debug.println("queryString is: "+ queryStr);

        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection, queryStr);
            int resultCount = stmt.executeUpdate();

            if ( resultCount != 1 ) {
                throw new AccountDAODBUpdateException(
                    "ERROR in ACCOUNT_TABLE INSERT !! resultCount = " +
                                   resultCount);
            }
        } catch(SQLException ae) {
            throw new AccountDAOSysException(
                        "SQLException while inserting new " +
                        "account; id = " + details.getUserId() + " :\n" + ae);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private AccountModel selectAccount(String userId) throws
                                         AccountDAOSysException,
                                         AccountDAOFinderException {

        PreparedStatement stmt = null;
        ResultSet result = null;

        String queryStr = "SELECT "+
            "userid,status,email,firstname,lastname,"+
                "addr1,addr2,city,state,zip,country,phone"+
                    " FROM " + DatabaseNames.ACCOUNT_TABLE +
                        " WHERE userid = " + "'" + userId.trim() + "'";
        Debug.println("queryString is: "+ queryStr);

        try {
            getDBConnection();
            //stmt = dbConnection.createStatement();
            //result = stmt.executeQuery(queryStr);
            stmt = createPreparedStatement(dbConnection, queryStr);
            result = stmt.executeQuery();

            if ( !result.next() )
                throw new AccountDAOFinderException(
                                  "No record for primary key " + userId);

            int i = 1;
            userId = result.getString(i++);
            String status = result.getString(i++);
            String email = result.getString(i++);
            String firstName = result.getString(i++);
            String lastName = result.getString(i++);
            String street1 = result.getString(i++);
            String street2 = result.getString(i++);
            String city = result.getString(i++);
            String state = result.getString(i++);
            String zip = result.getString(i++);
            String country = result.getString(i++);
            String phone = result.getString(i++);

            Address addr = new Address(street1, street2, city, state, zip,
                               country);
            ContactInformation info =
                new ContactInformation(lastName, firstName, phone,
                                          email, addr);
            return(new AccountModel(userId, status, info));
        } catch(SQLException ae) {
            throw new AccountDAOSysException("SQLException while getting " +
                      "account; id = " + userId + " :\n" + ae);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void deleteAccount (String userId) throws
                                         AccountDAODBUpdateException,
                                         AccountDAOSysException {
        String queryStr = "DELETE FROM " + DatabaseNames.ACCOUNT_TABLE
        + " WHERE userid = " + "'" + userId.trim() + "'";
        PreparedStatement stmt = null;
        Debug.println("queryString is: "+ queryStr);

        try {
            getDBConnection();
            //stmt = dbConnection.createStatement();
            //int resultCount = stmt.executeUpdate(queryStr);
            stmt = createPreparedStatement(dbConnection, queryStr);
            int resultCount = stmt.executeUpdate();

            if (resultCount != 1)
                throw new AccountDAODBUpdateException
                ("ERROR deleteing account from ACCOUNT_TABLE!! resultCount = "+
                 resultCount);
        } catch(SQLException se) {
            throw new AccountDAOSysException("SQLException while removing " +
                            "account; id = " + userId + " :\n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void updateAccount(AccountModel details) throws
                                         AccountDAODBUpdateException,
                                         AccountDAOAppException,
                                         AccountDAOSysException {
        if (!isValidData(details.getUserId(), details.getContactInformation()))
            throw new AccountDAOAppException("Illegal data values for update");

        ContactInformation info = details.getContactInformation();
        String queryStr = "UPDATE " + DatabaseNames.ACCOUNT_TABLE + " SET "
            + "status = " + "'" + details.getStatus().trim() + "',"
            + "email = " + "'" + info.getEMail().trim() + "',"
            + "firstname = " + "'" + info.getGivenName().trim() + "',"
            + "lastname = " + "'" + info.getFamilyName().trim() + "',"
            + "addr1 = " + "'"
            + info.getAddress().getStreetName1().trim() + "',";
        if (info.getAddress().getStreetName2() != null)
            queryStr += " addr2 = " + "'"
                +info.getAddress().getStreetName2().trim() +"',";
        else
            queryStr += " addr2 = " + "' ',";
            queryStr +=  "city = " + "'" + info.getAddress().getCity().trim()
            + "'," + "state = " + "'" + info.getAddress().getState().trim()
            + "'," + "zip = " + "'" + info.getAddress().getZipCode().trim()
            + "'," + "country = " + "'" + info.getAddress().getCountry().trim()
            + "'," + "phone = " + "'" + info.getTelephone().trim() + "'"
            + " WHERE userid = " + "'" + details.getUserId().trim() + "'";
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection, queryStr);
            int resultCount = stmt.executeUpdate();
            if (resultCount != 1)
                throw new AccountDAODBUpdateException
                ("ERROR updating account in ACCOUNT_TABLE!! resultCount = " +
                 resultCount);
        } catch(SQLException se) {
            throw new AccountDAOSysException("SQLException while updating " +
                     "account; id = " + details.getUserId() + " :\n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void getDBConnection() throws AccountDAOSysException {
        try {
            dbConnection = datasource.getConnection();
        } catch (SQLException se) {
            throw new AccountDAOSysException("SQL Exception while getting " +
                                "DB connection : \n" + se);
        }
        return;
    }

    private void closeConnection() throws AccountDAOSysException {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
        }
        } catch (SQLException se) {
            throw new AccountDAOSysException("SQL Exception while closing " +
                                        "DB connection : \n" + se);
        }
    }

    private void closeResultSet(ResultSet result) throws AccountDAOSysException {
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
            throw new AccountDAOSysException("SQL Exception while closing " +
                                        "Result Set : \n" + se);
        }
    }

    private void closeStatement(PreparedStatement stmt) throws AccountDAOSysException {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
            throw new AccountDAOSysException("SQL Exception while closing " +
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

    private PreparedStatement createPreparedStatement(Connection con, String querry)
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

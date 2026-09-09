/*
 * $Id: SignOnDAOImpl.java,v 1.1.2.3 2001/04/03 21:38:58 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Context;

import com.sun.j2ee.blueprints.signon.util.DatabaseNames;
import com.sun.j2ee.blueprints.signon.util.JNDINames;

import com.sun.j2ee.blueprints.signon.model.MutableSignOnModel;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAOSysException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAOAppException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAODBUpdateException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAODupKeyException;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAOFinderException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This is the implementation of SignOnDAO for Oracle, Sybase, cloudscape
 * This class encapsulates all the JDBC calls made by
 * the SignOnEJB. The actual logic of inserting,
 * fetching, updating, or deleting  the data in
 * relational database tables to mirror the state of
 * SignOnEJB is implemented here.
 */
public class SignOnDAOImpl implements SignOnDAO {

    private transient Connection dbConnection = null;
    private transient DataSource datasource   = null;

    public SignOnDAOImpl() throws SignOnDAOSysException {
        try {
            InitialContext ic = new InitialContext();
            datasource = (DataSource)
            ic.lookup(JNDINames.SIGNON_DATASOURCE);
        } catch (NamingException ne) {
            throw new SignOnDAOSysException("NamingException while looking" +
                    " up DataSource Connection "
                    + JNDINames.SIGNON_DATASOURCE
                    + ": \n" + ne.getMessage());
        }
    }

    public void create(MutableSignOnModel details) throws
                                  SignOnDAOSysException,
                                  SignOnDAODBUpdateException,
                                  SignOnDAODupKeyException,
                                  SignOnDAOAppException {
        insertUserInfo(details);
    }

    public MutableSignOnModel load(String id) throws SignOnDAOSysException,
                                         SignOnDAOFinderException {
        return(selectSignOn(id));
    }

    public void store(MutableSignOnModel details) throws SignOnDAOAppException,
                               SignOnDAODBUpdateException,
                               SignOnDAOSysException {
        updateSignOn(details);
    }

    public void remove(String id) throws SignOnDAOSysException,
                                SignOnDAODBUpdateException {
        deleteSignOn(id);
    }

    public String findByPrimaryKey(String userName) throws
                                            SignOnDAOSysException,
                                            SignOnDAOFinderException {
        if (userNameExists(userName)) {
            return (userName);
        }
        throw new SignOnDAOFinderException ("Primary key not found; User Name = " + userName);
    }

    private boolean userNameExists(String userName) throws SignOnDAOSysException {
        getConnection();

        String queryStr ="SELECT username FROM " +
                DatabaseNames.SIGNON_TABLE +
                        " WHERE username = " + "'" + userName.trim() + "'";
        Debug.println("queryString is: "+ queryStr);
        PreparedStatement stmt = null;
        ResultSet result = null;
        boolean returnValue = false;
        try {
            stmt = createPreparedStatement(dbConnection,queryStr);
            result = stmt.executeQuery();
            if ( !result.next() ) {
                returnValue = false;
            } else {
                userName = result.getString(1);
                returnValue = true;
            }
        } catch(SQLException se) {
            throw new SignOnDAOSysException("Unable to Query for user name " +
                                userName + "\n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
        return returnValue;
    }

    private boolean isValidData(MutableSignOnModel details) {
        if ( (details.getUserName() == null) )
            return (false);
        else
            return (true);
    }

    private MutableSignOnModel selectSignOn(String userName) throws
                                        SignOnDAOSysException,
                                        SignOnDAOFinderException {
        getConnection();
        String queryStr = "SELECT "+
            "username, password "+
                " FROM " + DatabaseNames.SIGNON_TABLE + " WHERE username = "
                    + "'" + userName.trim() + "'";
        Debug.println("queryString is: "+ queryStr);

        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            stmt = createPreparedStatement(dbConnection,queryStr);
            result = stmt.executeQuery();
            if(!result.next())
                throw new SignOnDAOFinderException("No record for primary key " + userName);
            int i = 1;
            userName = result.getString(i++);
            String pwd = result.getString(i++);
            return(new MutableSignOnModel(userName, pwd));
        } catch(SQLException se) {
            throw new SignOnDAOSysException("Unable to Query for user name " + userName + "\n" + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void deleteSignOn(String userName) throws SignOnDAOSysException,
                                        SignOnDAODBUpdateException {
        getConnection();
        String queryStr = "DELETE FROM " + DatabaseNames.SIGNON_TABLE +
                " WHERE username = "
                    + "'" + userName.trim() + "'";
        Debug.println("queryString is: "+ queryStr);
        PreparedStatement stmt = null;
        try {
            stmt = createPreparedStatement(dbConnection,queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount != 1 )
                throw new SignOnDAODBUpdateException("ERROR deleteing user sign on info from" + " SIGNON_TABLE!! resultCount = " + resultCount);
        } catch(SQLException se) {
            throw new SignOnDAOSysException("Unable to delete for user name " + userName + "\n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void updateSignOn(MutableSignOnModel user) throws
                                        SignOnDAOAppException,
                                        SignOnDAODBUpdateException,
                                        SignOnDAOSysException {
        getConnection();
        if (!isValidData(user))
            throw new SignOnDAOAppException ("Illegal data values for update");

        PreparedStatement stmt = null;
        try {

            String queryStr = "UPDATE " + DatabaseNames.SIGNON_TABLE
                + " SET " + "password = " +  "'" + user.getPassWord().trim()
                + "'" + " WHERE username = " + "'" + user.getUserName().trim()
                + "'";
            stmt = createPreparedStatement(dbConnection,queryStr);
            Debug.println("queryString is: "+ queryStr);
            int resultCount = stmt.executeUpdate();
            if ( resultCount != 1 )
                throw new SignOnDAODBUpdateException ("ERROR updating signon in" + " SIGNON_TABLE!! resultCount = " + resultCount);
        } catch(SQLException se) {
            throw new SignOnDAOSysException("Unable to update user name " + user.getUserName() + " \n" + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void insertUserInfo(MutableSignOnModel user) throws
                                  SignOnDAOSysException,
                                  SignOnDAODBUpdateException,
                                  SignOnDAODupKeyException,
                                  SignOnDAOAppException {

        if(!isValidData(user))
            throw new SignOnDAOAppException(
                            "SignOnDAO: Illegal data values for insert");

        if (userNameExists(user.getUserName()))
            throw new SignOnDAODupKeyException("SignOnDAO: UserName Exists: "
                                        + user.getUserName());

        PreparedStatement stmt = null;
        try {
            getConnection();


            String queryStr = "INSERT INTO " + DatabaseNames.SIGNON_TABLE +
                    "(username,password)" + "VALUES ("
                    + "'" + user.getUserName().trim() + "',"
                    + "'" + user.getPassWord().trim() + "' )";
            stmt = createPreparedStatement(dbConnection, queryStr);
            int resultCount = stmt.executeUpdate();

            if ( resultCount != 1 )
                throw new SignOnDAODBUpdateException(
                    "SignOnDAO: ERROR in SIGNON_TABLE INSERT !! " +
                    "resultCount = " + resultCount);
        } catch(SQLException se) {
            throw new SignOnDAOSysException("SQLException while inserting " +
                            "sign on info : " + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void getConnection() throws SignOnDAOSysException {
        try {
            dbConnection = datasource.getConnection();
        } catch (SQLException se) {
            throw new SignOnDAOSysException("SQLExcpetion while getting" +
                                        " DB Connection : \n" + se);
        }
    }

    private void closeResultSet(ResultSet result) throws SignOnDAOSysException{
        try {
            if (result != null) {
                result.close();
            }
        } catch (SQLException se) {
            throw new SignOnDAOSysException("SQL Exception while closing " +
                                        "Result Set : \n" + se);
        }
    }

    private void closeStatement(PreparedStatement stmt) throws SignOnDAOSysException {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
            throw new SignOnDAOSysException("SQL Exception while closing " +
                                        "Statement : \n" + se);
        }
    }

    private void closeConnection() throws SignOnDAOSysException {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException se) {
            throw new SignOnDAOSysException("SQLExcpetion while closing" +
                                    " DB Connection : \n" + se);
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

/*
 * $Id: ProfileMgrDAOImpl.java,v 1.1.2.3 2001/04/03 21:38:58 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.dao;

import java.util.ArrayList;
import java.util.Iterator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.sun.j2ee.blueprints.personalization.util.JNDINames;
import com.sun.j2ee.blueprints.personalization.util.DatabaseNames;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.MutableProfileMgrModel;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;

import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOSysException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOAppException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOFinderException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAODupKeyException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAODBUpdateException;

/**
 * The ProfileMgrDAOImpl class encapsulates all the persistent store
 * calls made by the ProfileMgrEJB.
 * The actual logic of inserting/fetching/updating/deleting the data (in
 * relational database tables to mirror the state of ProfileMgrEJB) is
 * implemented here.
 */
public class ProfileMgrDAOImpl implements ProfileMgrDAO {

    private transient Connection dbConnection = null;
    private transient DataSource datasource   = null;

    /**
     * Constructor with no arguments, establishes the datasource.
     */
    public ProfileMgrDAOImpl() throws ProfileMgrDAOSysException {
        try {
            InitialContext ic = new InitialContext();
            datasource = (DataSource) ic.lookup(JNDINames.ESTORE_DATASOURCE);
        } catch (NamingException ne) {
            throw new ProfileMgrDAOSysException(
                                        "NamingException while looking" +
                                        " up DataSource Connection " +
                                        JNDINames.ESTORE_DATASOURCE +
                                        ": \n" + ne.getMessage());
        }
    }

    /**
     * Creates a personal preferences profile for this user and persists the
     * data in persistent store.  Mirrors the ejbCreate method.
     * @param detail    the <code>MutableProfileMgrModel</code> of the user is
     *                  passed as argument
     * @throws  <code>ProfileMgrDAOAppException</code> is thrown if the profile
     *          could not be persisted because of an user error in inputs.
     * @throws  <code>ProfileMgrDAODBUpdateException</code> is thrown if a
     *          recoverable error occurred while updating the database.
     * @throws  <code>ProfileMgrDAODupKeyException</code> is thrown if a
     *          with the same userid exists in the database
     * @throws  <code>ProfileMgrDAOSysException</code> is thrown if an
     *          irrecoverable error occurred while persisting the profile
     */
    public void create(MutableProfileMgrModel details) throws
                                ProfileMgrDAODupKeyException,
                                ProfileMgrDAODBUpdateException,
                                ProfileMgrDAOAppException,
                                ProfileMgrDAOSysException {
        insertProfile(details);
    }

    /**
     * Loads a personal preferences profile from persistent store into memory.
     * Mirrors the ejbLoad method.
     * @param userId  A <code>String</code> that represents the user id
     * @returns the profile of the user <code>MutableProfileMgrModel</code>
     * @throws  <code>ProfileMgrDAOFinderException</code> is thrown if a
     *          profile was not found for the given user
     * @throws  <code>ProfileMgrDAOSysException</code> is thrown if an
     *          irrecoverable error occurred while loading the profile
     */
    public MutableProfileMgrModel load(String userId) throws
                              ProfileMgrDAOSysException,
                              ProfileMgrDAOFinderException {
        return(selectProfile(userId));
    }

    /**
     * Stores a personal preferences profile from memory into persistent store.
     * Mirrors the ejbStore method.
     * @param details the <code>MutableProfileMgrModel</code> of the user
     * @throws  <code>ProfileMgrDAOAppException</code> is thrown if the profile
     *          could not be persisted because of an user error in inputs.
     * @throws  <code>ProfileMgrDAODBUpdateException</code> is thrown if a
     *          recoverable error occurred while updating the database.
     * @throws  <code>ProfileMgrDAOSysException</code> is thrown if an
     *          irrecoverable error occurred while persisting the profile
     */
    public void store(MutableProfileMgrModel details) throws
                                        ProfileMgrDAOSysException,
                                        ProfileMgrDAOAppException,
                                        ProfileMgrDAODBUpdateException {
        updateProfile(details);
    }

    /**
     * Removes a personal preferences profile from persistent store.
     * Mirrors the ejbRemove method.
     * @param userId    a string that represents the userId to be removed
     * @throws  <code>ProfileMgrDAODBUpdateException</code> is thrown if a
     *          recoverable error occurred while updating the database.
     * @throws  <code>ProfileMgrDAOSysException</code> is thrown if an
     *          irrecoverable error occurred while removing the profile
     */
    public void remove(String userId) throws ProfileMgrDAOSysException,
                                ProfileMgrDAODBUpdateException {
        deleteProfile(userId);
    }

    /**
     * Finds a personal preferences profile from persistent store.
     * Mirrors the ejbFindByPrimaryKey method.
     * @returns a string which represents the primary key for this profile
     * @throws  <code>ProfileMgrDAOFinderException</code> is thrown if a
     *          profile was not found for the given user
     * @throws  <code>ProfileMgrDAOSysException</code> is thrown if an
     *          irrecoverable error occurred while accessing the database
     */
    public String findByPrimaryKey(String userId) throws
                                            ProfileMgrDAOSysException,
                                            ProfileMgrDAOFinderException {

        if (userExists(userId))
            return (userId);
        throw new ProfileMgrDAOFinderException (
                        "ProfileMgrDAO: primary key not found:" + userId);
    }

   /**
    * Gets the banner preference for this user from persistent store.
    * @param    favCategory     the favorite category for this user.
    * @returns  the string representing the name of the banner.
    * @throws   <code>ProfileMgrDAOSysException</code> is thrown if
    *           an irrecoverable error happened while getting the banner
    */
    public String getBanner(String favCategory) throws
                                        ProfileMgrDAOSysException {
        String favCat = favCategory;
        String bName = null;

        String queryStr = "SELECT favcategory, bannername FROM " +
            DatabaseNames.BANNERDATA_TABLE +
            " WHERE favcategory =  " + "'" + favCat.trim() + "'";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            getDBConnection();
            stmt = createPreparedStatement(dbConnection,queryStr);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int i = 1;
                favCat = rs.getString(i++).trim();
                bName = rs.getString(i++).trim();
            }
        } catch(SQLException se) {
            throw new ProfileMgrDAOSysException("SQLException while getting " +
                   "Banner for favorite category " + favCategory + " : " + se);
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection();
        }
        return bName;
    }

    private boolean userExists(String userId) throws
                                      ProfileMgrDAOSysException {

        PreparedStatement stmt = null;
        boolean retVal = false;
        try {
            getDBConnection();
            String queryStr ="SELECT userid FROM " +
                            DatabaseNames.PROFILE_TABLE
                            + " WHERE userid = " + "'" + userId.trim() + "'";
            stmt = createPreparedStatement(dbConnection,queryStr);
            ResultSet result = stmt.executeQuery();
            if(!result.next()) {
                retVal = false;
            } else {
                userId = result.getString(1);
                retVal = true;
            }
        } catch(SQLException se) {
            throw new ProfileMgrDAOSysException(
                            "SQLException while checking " +
                            "for presence of user id " + userId + " : " + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
        return retVal;
    }

    private boolean isValidData (MutableProfileMgrModel details) {
        if(((details.getUserId() == null) ||
            (details.getExplicitInformation().getLangPref() == null) ||
            (details.getExplicitInformation().getFavCategory() == null)))
            return (false);
        else
            return (true);
    }

    private void insertProfile(MutableProfileMgrModel details) throws
                                        ProfileMgrDAOAppException,
                                        ProfileMgrDAODupKeyException,
                                        ProfileMgrDAODBUpdateException,
                                        ProfileMgrDAOSysException {

        if(!isValidData(details))
            throw new ProfileMgrDAOAppException(
                            "ProfileMgrDAO: Illegal data values for insert");

        if (userExists(details.getUserId()))
            throw new ProfileMgrDAODupKeyException("ProfileMgrDAO: Profile " +
                                     "Exists: " + details.getUserId());

        PreparedStatement stmt = null;
        try {
            getDBConnection();

            String queryStr = "INSERT INTO " + DatabaseNames.PROFILE_TABLE +
                    "(userid,langpref,mylistopt,banneropt,"
                    + "favcategory)" + "VALUES ("
                    + "'" + details.getUserId() + "',"
                    + "'" + details.getExplicitInformation().getLangPref()
                    + "',"
                    + (details.getExplicitInformation().getMyListOpt()? 1:0)
                    + ","
                    + (details.getExplicitInformation().getBannerOpt()? 1:0)
                    + ",'"
                    + details.getExplicitInformation().getFavCategory()
                    + "' )";
            stmt = createPreparedStatement(dbConnection,queryStr);
            int resultCount = stmt.executeUpdate();

            if ( resultCount != 1 )
                throw new ProfileMgrDAODBUpdateException(
                    "ProfileMgrDAO: ERROR in PROFILE_TABLE INSERT !! " +
                    "resultCount = " + resultCount);
        } catch(SQLException se) {
            throw new ProfileMgrDAOSysException("SQLException while inserting "
                                                + "profile : " + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private MutableProfileMgrModel selectProfile(String userId) throws
                                        ProfileMgrDAOFinderException,
                                        ProfileMgrDAOSysException {

        PreparedStatement stmt = null;
        ResultSet result = null;
        try {
            getDBConnection();
            String queryStr = "SELECT "+
                    "userid,langpref,mylistopt,banneropt,favcategory"+
                    " FROM " + DatabaseNames.PROFILE_TABLE +
                    " WHERE userid = " + "'" + userId.trim() + "'";
            stmt = createPreparedStatement(dbConnection,queryStr);
            result = stmt.executeQuery();

            if ( !result.next() )
                throw new ProfileMgrDAOFinderException(
                        "ProfileMgrDAO: No record for primary key " + userId);

            int i = 1;
            userId = result.getString(i++);
            String langPref = result.getString(i++);
            int myListOpt = result.getInt(i++);
            int bannerOpt = result.getInt(i++);
            String favCategory = result.getString(i++);
            ExplicitInformation ein = new ExplicitInformation(langPref,
                           favCategory, (myListOpt==1),(bannerOpt==1));
            return(new MutableProfileMgrModel(userId, ein));
        } catch(SQLException se) {
            throw new ProfileMgrDAOSysException("SQLException while selecting "
                              + "profile of user id " + userId + " : " + se);
        } finally {
            closeResultSet(result);
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void deleteProfile(String userId) throws ProfileMgrDAOSysException,
                                        ProfileMgrDAODBUpdateException {

        PreparedStatement stmt = null;
        try {
            getDBConnection();
            String queryStr = "DELETE FROM " + DatabaseNames.PROFILE_TABLE
                    + " WHERE userid = " + "'" + userId.trim() + "'";
            stmt = createPreparedStatement(dbConnection,queryStr);
            int resultCount = stmt.executeUpdate();

            if (resultCount != 1)
                throw new ProfileMgrDAODBUpdateException
                    ("ProfileMgrDAO: ERROR deleteing profile from " +
                        "PROFILE_TABLE!! resultCount = " + resultCount);
        } catch(SQLException se) {
            throw new ProfileMgrDAOSysException("SQLException while deleting "
                                     + "user id " + userId + " : " + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void updateProfile(MutableProfileMgrModel userDetails) throws
                                        ProfileMgrDAOSysException,
                                        ProfileMgrDAOAppException,
                                        ProfileMgrDAODBUpdateException {
        if (!isValidData(userDetails))
            throw new ProfileMgrDAOAppException(
                "ProfileMgrDAO: Illegal profile data values for update");

        PreparedStatement stmt = null;
        ExplicitInformation eInfo = userDetails.getExplicitInformation();
        try {
            getDBConnection();

            String queryStr = "UPDATE " + DatabaseNames.PROFILE_TABLE + " SET "
                + "langpref = " + "'" + eInfo.getLangPref().trim() + "',"
                + "favcategory = " + "'" + eInfo.getFavCategory().trim() + "',"
                + "mylistopt = " + ((eInfo.getMyListOpt())? 1 :0)
                + ",banneropt = " + ((eInfo.getBannerOpt())? 1 :0)
                + " WHERE userid = " + "'" + userDetails.getUserId().trim()
                + "'";
            stmt = createPreparedStatement(dbConnection,queryStr);
            int resultCount = stmt.executeUpdate();
            if (resultCount != 1)
                throw new ProfileMgrDAODBUpdateException
                    ("ProfileMgrDAO: ERROR updating profile in " +
                        "PROFILE_TABLE!! resultCount = " + resultCount);
        } catch(SQLException se) {
            throw new ProfileMgrDAOSysException("SQLException while updating "
                      + "profile of user id " + userDetails.getUserId() +
                                                " : " + se);
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    private void getDBConnection() throws ProfileMgrDAOSysException {
        try {
            dbConnection = datasource.getConnection();
        } catch (SQLException se) {
            throw new ProfileMgrDAOSysException("SQLException while getting " +
                                          "DB connection " + se.getMessage());
        }
    }

    private void closeResultSet(ResultSet rs) throws
                                       ProfileMgrDAOSysException {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch(SQLException se) {
            throw new ProfileMgrDAOSysException("SQLException while closing " +
                                          "result set " + se.getMessage());
        }
    }

    private void closeStatement(PreparedStatement stmt) throws
                                          ProfileMgrDAOSysException {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch(SQLException se) {
            throw new ProfileMgrDAOSysException("SQLException while closing " +
                                          "statement " + se.getMessage());
        }
    }

    private void closeConnection() throws ProfileMgrDAOSysException {
        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch(SQLException se) {
            throw new ProfileMgrDAOSysException("SQLException while closing " +
                                          "DB connection " + se.getMessage());
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

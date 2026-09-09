/*
 * $Id: ProfileMgrDAO.java,v 1.1.2.2 2001/03/13 06:57:15 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.dao;

import com.sun.j2ee.blueprints.personalization.profilemgr.model.MutableProfileMgrModel;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;

import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOSysException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOAppException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAODupKeyException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOFinderException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAODBUpdateException;

/**
 * ProfileMgrDAO is an interface for the Data Access Object (DAO).
 * This interface encapsulates all the JDBC calls made by the ProfileMgrEJB.
 * The actual logic of inserting/fetching/updating/deleting the data in
 * relational database tables to mirror the state of ProfileMgrEJB is
 * implemented in the <code>ProfileMgrDAOImpl</code> class.
 */
public interface ProfileMgrDAO {

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
                              ProfileMgrDAOAppException,
                              ProfileMgrDAODupKeyException,
                              ProfileMgrDAODBUpdateException,
                              ProfileMgrDAOSysException;

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
                              ProfileMgrDAOFinderException;

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
                              ProfileMgrDAODBUpdateException;

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
                                             ProfileMgrDAODBUpdateException;

    /**
     * Finds a personal preferences profile from persistent store.
     * Mirrors the ejbFindByPrimaryKey method.
     * @returns a string which represents the primary key for this profile
     * @throws  <code>ProfileMgrDAOFinderException</code> is thrown if a
     *          profile was not found for the given user
     * @throws  <code>ProfileMgrDAOSysException</code> is thrown if an
     *          irrecoverable error occurred while accessing the database
     */
    public String findByPrimaryKey(String id) throws ProfileMgrDAOSysException,
                                              ProfileMgrDAOFinderException;

   /**
    * Gets the banner preference for this user from persistent store.
    * @param    favCategory     the favorite category for this user.
    * @returns  the string representing the name of the banner.
    * @throws   <code>ProfileMgrDAOSysException</code> is thrown if
    *           an irrecoverable error happened while getting the banner
    */
    public String getBanner(String favCategory) throws
                                        ProfileMgrDAOSysException;
}




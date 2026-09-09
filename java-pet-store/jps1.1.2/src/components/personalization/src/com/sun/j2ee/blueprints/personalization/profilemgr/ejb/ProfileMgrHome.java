/*
 * $Id: ProfileMgrHome.java,v 1.1.2.3 2001/03/13 06:57:16 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBHome;

import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrAppException;

/**
 * ProfileMgrHome is an interface which provides methods to create or to find
 * personal preferences for a particular user.
 */
public interface ProfileMgrHome extends EJBHome {

    /**
     * Creates a personal preference profile for a particular user.
     * @param userId    a string which represents the id of this user
     * @param eInfo     an <code>ExplicitInformation</code> structure
     *                  containing the personal preferences of this user
     * @returns         a reference to a <code>ProfileMgr</code> object created
     * @throws          <code>RemoteException</code> if an irrecoverable error
     *                  occurred while creating the personal perference
     * @throws          <code>CreateException</code> is thrown if there a
     *                  recoverable error happened while creating the profile
     * @throws          <code>DuplicateKeyException</code> is thrown if the
     *                  profile already exists for this user
     * @throws          <code>ProfileMgrAppException</code> if an wrong/missing
     *                  field was specified by the user
     */
    public ProfileMgr create(String userId, ExplicitInformation eInfo)
                                 throws RemoteException,
                                        DuplicateKeyException,
                                        CreateException,
                                        ProfileMgrAppException;

    /**
     * Finds a personal preferences profile for a particular user.
     * @param userId    a string which represents the id of this user
     * @returns         a reference to a <code>ProfileMgr</code> object found
     * @throws          <code>RemoteException</code> if an irrecoverable error
     *                  occurred while creating the personal perference
     * @throws          <code>FinderException</code> is thrown if a profile
     *                  could not be found for this user
     */
    public ProfileMgr findByPrimaryKey(String userId)
        throws RemoteException, FinderException;
}

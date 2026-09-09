/*
 * $Id: ProfileMgrEJB.java,v 1.1.2.10 2001/04/14 00:39:24 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.ejb;

import java.rmi.RemoteException;
import javax.naming.NamingException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import com.sun.j2ee.blueprints.personalization.util.EJBUtil;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ProfileMgrModel;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.MutableProfileMgrModel;
import com.sun.j2ee.blueprints.personalization.util.DatabaseNames;
import com.sun.j2ee.blueprints.personalization.profilemgr.dao.ProfileMgrDAO;
import com.sun.j2ee.blueprints.personalization.profilemgr.dao.ProfileMgrDAOFactory;

import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOSysException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOAppException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAODupKeyException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOFinderException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAODBUpdateException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrAppException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrAppInvalidCharException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrAppLongIdException;

/**
 * ProfileMgrEJB is a class which represents the implementation of
 * a personal preferences profile as an Entity Bean using bean managed
 * persistance.
 */
public class ProfileMgrEJB implements EntityBean {

    private MutableProfileMgrModel currentProfile;
    private transient ProfileMgrDAO profileMgrDao;
    private EntityContext context;

    /**
     * Default class constructor with no arguments.
    */
    public ProfileMgrEJB() {}

    /**
     * Creates a personal preference profile for a particular user.
     * @param userId    a string which represents the id of this user
     * @param eInfo     an <code>ExplicitInformation</code> structure
     *                  containing the personal preferences of this user
     * @returns         a string that is the userid of the profile
     * @throws          <code>EJBException</code> if an irrecoverable error
     *                  occurred while creating the personal perference
     * @throws          <code>CreateException</code> is thrown if there a
     *                  recoverable error happened while creating the profile
     * @throws          <code>DuplicateKeyException</code> is thrown if the
     *                  profile already exists for this user
     * @throws          <code>ProfileMgrAppException</code> if an wrong/missing
     *                  field was specified by the user
     */
    public String ejbCreate (String userId, ExplicitInformation eInfo)
    throws CreateException, DuplicateKeyException, ProfileMgrAppException {

        if(userId.length() > DatabaseNames.MAX_USERID_LENGTH)
            throw new ProfileMgrAppLongIdException("The user id given should "
                            + " be less than "
                            + DatabaseNames.MAX_USERID_LENGTH + " chars long");
        if( (userId.indexOf('%') != -1) ||
            (userId.indexOf('*') != -1))
            throw new ProfileMgrAppLongIdException("The user id given should "
                            + " not have '%' or '*' characters");

        // set the instance data
        this.currentProfile = new MutableProfileMgrModel(userId, eInfo);

        try {
            ProfileMgrDAO dao = getDAO();
            dao.create(this.currentProfile);
            return (userId);
        } catch (ProfileMgrDAODBUpdateException se) {
            context.setRollbackOnly();
            throw new CreateException("ProfileMgrEJB: create failed : " +
                                            se.getMessage());
        } catch (ProfileMgrDAODupKeyException dup) {
            throw new DuplicateKeyException("ProfileMgrEJB: DUP KEY : "+
                                            dup.getMessage());
        }catch (ProfileMgrDAOAppException se) {
            throw new ProfileMgrAppException(se.getMessage());
        }catch (ProfileMgrDAOSysException se) {
            throw new EJBException("ProfileMgrEJB: create failed : " +
                                            se.getMessage());
        }
    }

    /**
     * A post create method for this EJB object.
     * @param userId    a string which represents the id of this user
     * @param eInfo     an <code>ExplicitInformation</code> structure
     *                  containing the personal preferences of this user
     * @throws          <code>RemoteException</code> if an irrecoverable error
     *                  occurred while creating the personal perference
     * @throws          <code>CreateException</code> is thrown if there a
     *                  recoverable error happened while creating the profile
     * @throws          <code>DuplicateKeyException</code> is thrown if the
     *                  profile already exists for this user
     * @throws          <code>ProfileMgrAppException</code> if an wrong/missing
     *                  field was specified by the user
     */
    public void ejbPostCreate(String userId, ExplicitInformation eInfo)
        throws CreateException, DuplicateKeyException, ProfileMgrAppException {
    }

    /**
     * Removes a personal preferences profile from persistent store.
     * @throws  <code>RemoveException</code> is thrown if a
     *          recoverable error occurred while updating the database.
     * @throws  <code>EJBException</code> is thrown if an
     *          irrecoverable error occurred while removing the profile
     */
    public void ejbRemove() throws RemoveException {
        try {
            ProfileMgrDAO dao = getDAO();
            dao.remove((String)context.getPrimaryKey());
        } catch (ProfileMgrDAODBUpdateException se) {
            context.setRollbackOnly();
            throw new RemoveException("ProfileMgrEJB: remove failed : " +
                                            se.getMessage());
        } catch (ProfileMgrDAOSysException sys) {
            throw new EJBException(sys.getMessage());
        }
    }

    /**
     * Sets the <code>EntityContext</code> for this personal preference profile
     * EJB object.
     * @param ec    the <code>EntityContext</code> for tis profile EJB object
     */
    public void setEntityContext(EntityContext ec) {
        context = ec;
    }

    /**
     * Unsets the <code>EntityContext</code> for this personal preference
     * profile EJB object.
     */
    public void unsetEntityContext() {}

    /**
     * Loads a personal preferences profile from persistent store into memory.
     * @throws  <code>EJBException</code> is thrown if any error occurred
     */
    public void ejbLoad() {
        ProfileMgrDAO dao;
        try {
            dao = getDAO();
            this.currentProfile =
                dao.load((String)context.getPrimaryKey());
        } catch (ProfileMgrDAOSysException se) {
            throw new EJBException("ProfileMgrDAOException while load : " +
                                            se.getMessage());
        } catch (ProfileMgrDAOFinderException fe) {
             throw new EJBException("ProfileMgrDAOException while load : " +
                                            fe.getMessage());
        }
    }

    /**
     * Stores a personal preferences profile from memory into persistent store.
     * @throws  <code>EJBException</code> is thrown if any error occurs
     */
    public void ejbStore() {
        try {
            ProfileMgrDAO dao = getDAO();
            dao.store(this.currentProfile);
        } catch (ProfileMgrDAOSysException se) {
            throw new EJBException("ProfileMgrDAOException while store : " +
                                            se.getMessage());
        } catch (ProfileMgrDAOAppException fe) {
            throw new EJBException("ProfileMgrDAOException while store : " +
                                            fe.getMessage());
        }
    }

    /**
     * Finds a personal preferences profile from persistent store.
     * @returns a string which represents the primary key for this profile
     * @throws  <code>FinderException</code> is thrown if a
     *          profile was not found for the given user
     * @throws  <code>EJBException</code> is thrown if an
     *          irrecoverable error occurred while accessing the database
     */
    public String ejbFindByPrimaryKey(String key) throws FinderException {
        try {
            ProfileMgrDAO dao = getDAO();
            String userId = dao.findByPrimaryKey(key);
            this.currentProfile = dao.load(userId);
            return(userId);
        } catch (ProfileMgrDAOSysException se) {
            throw new EJBException("ProfileMgrDAOException while findBy : " +
                                            se.getMessage());
        } catch (ProfileMgrDAOFinderException fe) {
            throw new FinderException("ProfileMgrDAOException while findBy : "
                                      + fe.getMessage());
        }
    }

    /**
     * Activates the personal preference profile for this EJB object.
     */
    public void ejbActivate() {
    }

    /**
     * Passivates the personal preference profile for this EJB object.
     */
    public void ejbPassivate() {
      this.profileMgrDao = null;
    }

    // business methods
    /**
     * Updates the <code>ExplicitInformation</code> personal preferences data
     * for this user.
     * @param   eInfo   an <code>ExplicitInformation</code> structure
     *                  containing the new personal preferences of this user
     */
    public void updateExplicitInformation(ExplicitInformation eInfo) {
        this.currentProfile.setExplicitInformation(eInfo);
    }

    /**
     * Returns  the personal preferences model for this user.
     * @return  the <code>ProfileMgrModel</code> personal prefrences data
     *          for this user
     */
    public ProfileMgrModel getDetails() {
        return(new ProfileMgrModel(currentProfile.getUserId(),
                                   currentProfile.getExplicitInformation()));
    }

    // private methods
    private ProfileMgrDAO getDAO() throws ProfileMgrDAOSysException {
        if(profileMgrDao == null) {
            profileMgrDao = ProfileMgrDAOFactory.getDAO();
        }
        return profileMgrDao;
    }
}

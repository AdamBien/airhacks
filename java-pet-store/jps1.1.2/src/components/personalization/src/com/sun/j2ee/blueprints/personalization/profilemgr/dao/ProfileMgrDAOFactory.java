/*
 * $Id: ProfileMgrDAOFactory.java,v 1.1.2.2 2001/03/13 06:57:15 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.dao;

import javax.naming.NamingException;
import javax.naming.InitialContext;

import com.sun.j2ee.blueprints.personalization.util.JNDINames;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrDAOSysException;

/**
 * ProfileMgrDAOFactory is a class whose method provide a way
 * to create Data Access Objects (DAOs).
 */
public class ProfileMgrDAOFactory {

    /**
     * A static method that gets a specific subclass which implements
     * the DAO methods based on the information obtained from the
     * <code>profilemgr_dao_class</code> environment entry name in
     * the deployment descriptor
     * @returns a <code>ProfileMgrDAO</code> object
     * @throws  <code>ProfileMgrDAOSysException</code> if the
     *          <code>ProfileMgrDAO</code> could not be obtained
     */
    public static ProfileMgrDAO getDAO() throws ProfileMgrDAOSysException {

        ProfileMgrDAO proDao = null;
        try {
            InitialContext ic = new InitialContext();
            String className = (String)
                               ic.lookup(JNDINames.PROFILEMGR_DAO_CLASS);
            proDao = (ProfileMgrDAO) Class.forName(className).newInstance();
        } catch (NamingException ne) {
            throw new ProfileMgrDAOSysException("ProfileMgrDAOFactory.getDAO: "
                      + "NamingException while getting DAO type : \n" +
                                                ne.getMessage());
        } catch (Exception se) {
            throw new ProfileMgrDAOSysException("ProfileMgrDAOFactory.getDAO: "
                      + "Exception while getting DAO type : \n" +
                                                se.getMessage());
        }
        return proDao;
    }
}

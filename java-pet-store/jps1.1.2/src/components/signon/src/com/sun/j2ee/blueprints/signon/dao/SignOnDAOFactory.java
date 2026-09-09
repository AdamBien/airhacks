/*
 * $Id: SignOnDAOFactory.java,v 1.1.2.1 2001/03/12 09:15:45 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.dao;

import javax.naming.NamingException;
import javax.naming.InitialContext;

import com.sun.j2ee.blueprints.signon.util.JNDINames;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnDAOSysException;

public class SignOnDAOFactory {

    /**
     * This method instantiates a particular subclass implementing
     * the DAO methods based on the information obtained from the
     * deployment descriptor
     */
    public static SignOnDAO getDAO() throws SignOnDAOSysException {

        SignOnDAO signOnDao = null;
        try {
            InitialContext ic = new InitialContext();
            String className = (String) ic.lookup(JNDINames.SIGNON_DAO_CLASS);
            signOnDao = (SignOnDAO) Class.forName(className).newInstance();
        } catch (NamingException ne) {
            throw new SignOnDAOSysException("SignOnDAOFactory.getDAO:  NamingException while getting DAO type : \n" + ne.getMessage());
        } catch (Exception se) {
            throw new SignOnDAOSysException("SignOnDAOFactory.getDAO:  Exception while getting DAO type : \n" + se.getMessage());
        }
        return signOnDao;
    }
}

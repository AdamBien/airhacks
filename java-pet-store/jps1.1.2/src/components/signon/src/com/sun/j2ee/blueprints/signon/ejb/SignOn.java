/*
 * $Id: SignOn.java,v 1.1.2.3 2001/03/01 11:01:22 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.sun.j2ee.blueprints.signon.model.SignOnModel;

/**
 * This interface provides methods to view and modify sign on
 * information for a particular user.
*/

public interface SignOn extends EJBObject {

    /**
     * @return the sign on information for this user.
     */
    public SignOnModel getDetails() throws RemoteException;


    /**
     * changes the password
     */
    public void updatePassWord(String passWord) throws RemoteException;

}

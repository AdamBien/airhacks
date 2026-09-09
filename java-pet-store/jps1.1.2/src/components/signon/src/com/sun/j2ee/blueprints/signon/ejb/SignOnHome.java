/*
 * $Id: SignOnHome.java,v 1.1.2.5 2001/03/01 11:01:23 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.ejb;


import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBHome;

import com.sun.j2ee.blueprints.signon.exceptions.SignOnAppException;

/**
 * The home interface of the SignOn Entity EJB.
 */

public interface SignOnHome extends EJBHome {

    public SignOn findByPrimaryKey (String userName)
        throws RemoteException, FinderException;


    public SignOn create(String userName, String passWord)
        throws RemoteException, DuplicateKeyException, CreateException,
               SignOnAppException;

}

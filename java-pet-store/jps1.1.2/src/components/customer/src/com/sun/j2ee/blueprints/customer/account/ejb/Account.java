/*
 * $Id: Account.java,v 1.5.4.1 2001/03/02 12:18:11 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.account.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;

/**
 * This interface provides methods to view and modify account
 * information for a particular account.
*/

public interface Account extends EJBObject {

    /**
     * @return the account information corresponding to this account.
     */
    public AccountModel getDetails() throws RemoteException;

    /**
     * updates the contact information  for the specified account
     */
    public void changeContactInformation(ContactInformation info)
        throws RemoteException;

}

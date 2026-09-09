/*
 * $Id: CustomerHome.java,v 1.3.4.3 2001/03/14 10:03:07 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.customer.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 * The Home interface for Customer EJB
 */
public interface CustomerHome extends EJBHome {

    /** Creates an instance of hte Customer
     * @return <code>Customer</code> instance
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>CreateException</code> if account could not be created
     */
    public Customer create() throws RemoteException, CreateException;
}

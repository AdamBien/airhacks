/*
 * $Id: MailerHome.java,v 1.3.4.1 2001/02/28 08:07:27 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.mail.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * The Home interface for MailerEJB
 */
public interface MailerHome extends EJBHome {

    public Mailer create() throws RemoteException, CreateException;
}

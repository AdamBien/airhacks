/*
 * $Id: MailWebHelper.java,v 1.1.2.6 2001/04/10 01:35:52 ro89390 Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.mailerapp;

import java.rmi.RemoteException;
import javax.naming.NamingException;
import javax.ejb.CreateException;

import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;

import com.sun.j2ee.blueprints.mail.ejb.MailerHome;
import com.sun.j2ee.blueprints.mail.ejb.Mailer;
import com.sun.j2ee.blueprints.mail.ejb.EMailMessage;
import com.sun.j2ee.blueprints.mail.exceptions.MailerAppException;

import java.util.Locale;

/**
 * A helper class which takes care of interfacing with the Mailer EJB
 * component.
 */
public class MailWebHelper  {

    /** JNDI name for the Mailer EJB Component. */
    protected static final String MAILER_EJBHOME = "java:comp/env/ejb/mailer";

    public MailWebHelper() {}

    /**
     * Sends an e-mail with the given message and subject to the given
     * e-mail address.
     */
    public void sendEmail(String message,
                          String subject,
                          String emailAddress)
        throws MailHandlerException {

        // Send the e-mail.
        try {
            Mailer mailer = createMailerEJB();
            EMailMessage emm
                = new EMailMessage(subject,
                                   message,
                                   emailAddress,
                                   Locale.getDefault());
            mailer.sendMail(emm);

        }
        // Catch EJB exceptions and re-throw them as application
        // exceptions.
        catch (MailerAppException me) {
            throw new MailHandlerException("MailWebHelper:: MailerAppException : " + me);
        }
        catch (CreateException ce) {
            throw new MailHandlerException("MailWebHelper:: CreateException Error trying to lookup EJB" + ce);
        }
        catch (RemoteException re) {
            throw new MailHandlerException("MailWebHelper:: RemoteException Error trying to lookup EJB" + re);
        }
        catch (NamingException ne) {
            throw new MailHandlerException("MailWebHelper:: NamingException Error trying to lookup EJB" + ne);
        }
    }

    /**
     * A helper method which creates and returns an instance of the
     * Mailer EJB component.
     */
    private Mailer createMailerEJB()
        throws NamingException, CreateException, RemoteException {

        InitialContext initial = new InitialContext();
        Object objref = initial.lookup(MAILER_EJBHOME);
        MailerHome home = (MailerHome)
        PortableRemoteObject.narrow(objref, MailerHome.class);
        return (Mailer) home.create();
    }
}

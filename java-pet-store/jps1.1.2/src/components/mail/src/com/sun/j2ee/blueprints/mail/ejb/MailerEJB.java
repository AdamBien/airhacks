/*
 * $Id: MailerEJB.java,v 1.7.4.4 2001/02/28 08:07:27 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.mail.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.sun.j2ee.blueprints.mail.exceptions.MailerAppException;

/**
 *  Session Bean implementation of MailerEJB.
 *  Used to send a mail messageconfirmation such as anemail
 *  to a customer after a purchase is completed.
 *  This functionality is enabled by the petstore by modifying the
 *  sendConfirmationMail property of the petstore at deployment time.
 *
 *  @see MailHelper
*/

public class MailerEJB implements SessionBean {

    public void ejbCreate() {}

    public void setSessionContext(SessionContext sc) {}

    public void ejbRemove() {}

    public void ejbActivate() {}

    public void ejbPassivate() {}

    /**
     * This method sends an email message. Most of the mailing logic
     * is in the MailHelper class.
     *
     * @param  eMess  content  for the email message and also the
     *                information such as recipient required to
     *                send the message.
     * @see    EMailMessage
     * @see    MailHelper
     */
    public void sendMail(EMailMessage eMess) throws MailerAppException {

        // Exception is just declared as there is only one recoverable
        // app exception that is thrown by the MailHelper which should be
        // passed on as it is

        getMailHelper().createAndSendMail(eMess);
    }

    private MailHelper getMailHelper() {
        return (new MailHelper());
    }
}

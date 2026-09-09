/*
 * $Id: MailHandler.java,v 1.1.2.7 2001/04/10 01:35:51 ro89390 Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.mailerapp;

import javax.servlet.http.*;

/**
 * Handles responsibilities related to sending e-mail.
 */
public class MailHandler {

    /**
     * Handles a request to send an e-mail, and provides an
     * appropriate response.
     *
     * Post-condition: Set the bean with info to populate response.
     */
    public void handle(HttpServletRequest req, HttpServletResponse resp)
        throws MailHandlerException {

        // Read the e-mail parameters and validate them.
        String message = req.getParameter("mail_message");
        String subject = req.getParameter("mail_subject");
        String emailAddress = req.getParameter("mail_emailaddress");
        validate(message, subject, emailAddress);

        // Delegate the mail-sending work to the helper.
        MailWebHelper mailer = new MailWebHelper();
        mailer.sendEmail(message, subject, emailAddress);

        // Populate a result bean with data for display, and place it
        // in the response.
        ResultBean resultBean = new ResultBean();
        resultBean.setEmailAddress(emailAddress);
        resultBean.setSubject(subject);
        resultBean.setResultMessage(message);
        req.setAttribute("result", resultBean);
    }

    /**
     * Validates the given feedback.
     */
    protected void validate(String message,
                            String subject,
                            String emailAddress)
        throws MailHandlerException {

        if (message == null || message.trim().length() == 0
            || subject == null || subject.trim().length() == 0
            || emailAddress == null || emailAddress.trim().length() == 0) {
            throw new MailHandlerException("Unfortunately, there was a problem:Your message must have entry in all the fields. Your message has not been sent.");
        }
    }
}

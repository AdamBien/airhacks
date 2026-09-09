/*
 * $Id: MailHelper.java,v 1.5.4.5 2001/03/13 00:43:32 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.mail.ejb;

import java.util.Date;
import java.util.Locale;

import javax.activation.DataHandler;
import javax.ejb.SessionContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.Session;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.naming.InitialContext;

import com.sun.j2ee.blueprints.mail.util.JNDINames;
import com.sun.j2ee.blueprints.mail.exceptions.MailerAppException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * A helper class to create and send mail.
 */
public class MailHelper {


    /**
     * This method creates an email message and sends it using the
     * J2EE mail services
     * @param eMess contains the information needed to send the email
     *              message and also the contents of the email message
     *
     * @see    EMailMessage
     */
    public void createAndSendMail(EMailMessage eMess) throws MailerAppException {
        try {
            Debug.println("Sending message" +
                          "\nTo: " + eMess.getEmailReceiver() +
                          "\nSubject: " + eMess.getSubject() +
                          "\nContents: " + eMess.getHtmlContents());

            InitialContext ic = new InitialContext();
            Session session = (Session) ic.lookup(JNDINames.MAIL_SESSION);


            if (Debug.debuggingOn)
                session.setDebug(true);

            // construct the message
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom();

            msg.setRecipients(Message.RecipientType.TO,
                     InternetAddress.parse(eMess.getEmailReceiver(), false));

            msg.setSubject(eMess.getSubject());
            String contentType = "text/plain";
            MimeBodyPart mbp = new MimeBodyPart();
            // future - change content based on locale
            if (eMess.getLocale().equals(Locale.JAPAN)) {
                String[] languages = {"ja"};
                msg.setContentLanguage(languages);
                mbp.setText(eMess.getHtmlContents(), "iso-2022-jp");
            } else {
                  mbp.setText(eMess.getHtmlContents(), "us-ascii");
            }
            msg.setHeader("X-Mailer", "JavaMailer");
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp);
            msg.setContent(mp);
            msg.setSentDate(new Date());

            // send the mail off
            Transport.send(msg);

            Debug.println("\nMail sent successfully.");
        } catch (Exception e) {
            Debug.print("createAndSendMail exception : " + e);
            throw new MailerAppException("Failure while sending mail");
        }
    }
}

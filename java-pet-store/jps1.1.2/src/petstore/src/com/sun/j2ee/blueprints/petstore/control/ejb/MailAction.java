/*
 * $Id: MailAction.java,v 1.11.4.11 2001/04/05 21:51:39 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.control.ejb;

import java.util.Locale;
import java.rmi.RemoteException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.naming.NamingException;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.sun.j2ee.blueprints.mail.ejb.EMailMessage;
import com.sun.j2ee.blueprints.mail.exceptions.MailerAppException;

import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.customer.customer.ejb.Customer;
import com.sun.j2ee.blueprints.customer.customer.ejb.CustomerHome;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.EJBUtil;

import com.sun.j2ee.blueprints.mail.ejb.Mailer;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;

/**
 * This class encapsulates all the logic to build an EMail Message.
 * It uses the Order EJB and Account EJB to get the info to build
 * and send a message.
 *
 */
public class MailAction implements java.io.Serializable {

    public MailAction( ) {
    }

     /**
     * This method creates an email confirmation message
     * for an order and sends an email to the customer.
     * The Order EJB and Account EJB are accessed to
     * create the email message content.
     *
     * @param  order    is the orderEJB used to generate content
     *                  for the email message for a particular
     *                  customer order
     * @return          an EMailMessage object containing all info
     *                  to send the email.
     */
    public void sendConfirmationMessage(String userName,
                                        int orderId,
                                        Locale locale) throws
                                                       CreateException,
                                                       FinderException,
                                                       RemoteException{
        EMailMessage eMess = null;
        CustomerHome home = null;
        Customer cust = null;
        OrderModel orderDetails = null;
        AccountModel accountDetails = null;

        try {
                home = EJBUtil.getCustomerHome();
                cust = home.create();
        } catch (RemoteException re) {
            throw new RemoteException("Irrecoverable error while getting order instance for sending mail : " + re);
        } catch (javax.naming.NamingException ne) {
            throw new RemoteException("Irrecoverable error while getting order instance for sending mail : " + ne);
        } catch (CreateException ce) {
            throw new CreateException("Unable to get a reference to the order to send the mail");
        }

        try {
            orderDetails = cust.getOrderDetails(orderId);
            accountDetails = cust.getAccountDetails(userName);
            if (accountDetails == null) {
                Debug.print("Can't find user corresponding to the order " +
                            orderDetails.getOrderId()+ "!");
            }

            //params:mail subjectLine, message contents, emailAddress
            eMess = new EMailMessage(createSubjectLine(orderDetails),
                           createMessage(orderDetails, accountDetails, locale),
                           accountDetails.getContactInformation().getEMail(), locale);
            Mailer mailer = EJBUtil.createMailerEJB();
            mailer.sendMail(eMess);
        } catch (RemoteException re) {
          throw new RemoteException("Irrecoverable error while getting order instance for sending mail : " + re);
        } catch (NamingException ne) {
           throw new RemoteException("Irrecoverable error while getting order instance for sending mail : " + ne);
        } catch (CreateException ce) {
           throw new CreateException("Unable to get a reference to the order to send the mail");
        } catch (FinderException fe) {
            throw new FinderException("Unable to get a reference to the order to send the mail");
        } catch (MailerAppException mx) {
        }
    }


    /**
     *  Builds the email message contents
     */
    private String createMessage(OrderModel orderDetails,
                                      AccountModel accountDetails, Locale locale) {
      StringBuffer msg = new StringBuffer();
      if (locale.equals(Locale.JAPAN)) {
          // show that we can get Japanese characters in the message.
          // In the future these types of messages should come from resource bundles.
         msg.append("\u5fa1\u6ce8\u6587\u3042\u308a\u304c\u3068\u3046\u3054\u3056\u3044\u307e\u3059\u3002\n ");
      } else {
          msg.append("Thank you for your order.\n");
      }
       msg.append("This mail is a confirmation for your order# " +
                                orderDetails.getOrderId() + "\n");
      msg.append("Please save it for your records.\n");
      msg.append("Your order will be shipped to:\n");
      msg.append(orderDetails.getShipToLastName() + " ");
      msg.append(orderDetails.getBillToFirstName() + "\n");

      msg.append("The total cost of your order is " +
                            JSPUtil.formatCurrency(orderDetails.getTotalPrice() + "",  locale) + "\n");

      return msg.toString();
    }

    /**
     *  Builds the subject line for the  email message
     */
    private String createSubjectLine(OrderModel orderDetails) {
      StringBuffer msg = new StringBuffer();
      msg.append("Your order#" + orderDetails.getOrderId());

      return msg.toString();
    }

}

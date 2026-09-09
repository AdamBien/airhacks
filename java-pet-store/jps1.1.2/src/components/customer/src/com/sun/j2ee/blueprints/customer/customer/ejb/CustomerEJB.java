/*
 * $Id: CustomerEJB.java,v 1.7.4.9 2001/03/14 10:03:07 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.customer.ejb;

import java.util.Collection;
import java.util.Locale;
import java.rmi.RemoteException;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import javax.ejb.DuplicateKeyException;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.customer.order.ejb.Order;
import com.sun.j2ee.blueprints.customer.order.ejb.OrderHome;
import com.sun.j2ee.blueprints.customer.account.ejb.Account;
import com.sun.j2ee.blueprints.customer.account.ejb.AccountHome;
import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.util.CreditCard;
import com.sun.j2ee.blueprints.customer.util.JNDINames;

import com.sun.j2ee.blueprints.customer.account.exceptions.AccountAppException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountAppLongIdException;
import com.sun.j2ee.blueprints.customer.account.exceptions.AccountAppInvalidCharException;
import com.sun.j2ee.blueprints.customer.order.exceptions.OrderAppException;

import com.sun.j2ee.blueprints.customer.customer.exceptions.CustomerAppLongIdException;
import com.sun.j2ee.blueprints.customer.customer.exceptions.CustomerAppInvalidCharException;
import com.sun.j2ee.blueprints.customer.customer.exceptions.CustomerAppException;

/**
 * Session Bean implementation for CustomerEJB EJB.
 */
public class CustomerEJB implements SessionBean {

    AccountHome accountHomeRef = null;
    OrderHome orderHomeRef = null;
    SessionContext context = null;

    /**
     * Constructor - does nothing
     */

    public CustomerEJB() {}

    /**
     * Sets the session context
     * @param sc the <code>SessionContext</code> for this instance
     */
    public void setSessionContext(SessionContext sc) {
        this.context = sc;
    }

    private void getReferences() {
        try {
            Object objref;
            InitialContext initial = new InitialContext();
                if(accountHomeRef == null) {
                    objref = initial.lookup(JNDINames.ACCOUNT_EJBHOME);
                    accountHomeRef = (AccountHome)
                    PortableRemoteObject.narrow(objref, AccountHome.class);
                }
                if(orderHomeRef == null) {
                    objref = initial.lookup(JNDINames.ORDER_EJBHOME);
                    orderHomeRef = (OrderHome)
                    PortableRemoteObject.narrow(objref, OrderHome.class);
                }
        } catch (javax.naming.NamingException ne) {
            throw new EJBException ("Naming Ex while getting facade " +
                                    "references : " + ne);
        }
        return;
    }

    /**
     * the ejbCreate methods that just gets the references
     */
    public void ejbCreate() {
        getReferences();
    }

    /**
     * the ejbRemove methods that does nothing
     */
    public void ejbRemove() {
    }

    /**
     * the ejbActivate methods that just gets the references
     */
    public void ejbActivate() {
       getReferences();
    }

    /**
     * the ejbPassivate methods that resets the references
     */
    public void ejbPassivate() {
        accountHomeRef = null;
        orderHomeRef = null;
    }

    /**
     * Gets the account details give an user id
     * @param userId a string that represents the user Id
     * @return the <code>AccountModel</code> that has the account information
     *         corresponding to a customer's account.
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>FinderException</code> if the user details was not found
     */
    public AccountModel getAccountDetails(String userId) throws
                                   FinderException {
        try {
           if(accountHomeRef == null)
               getReferences();
           Account acct = accountHomeRef.findByPrimaryKey(userId);
           return(acct.getDetails());
        } catch (FinderException fe) {
             throw new FinderException(fe.getMessage());
        } catch (RemoteException re) {
             throw new EJBException(re);
        }
    }

    /**
     * updates the contact information  for the specified account
     * @param info the <code>ContactInformation</code> of the user
     * @param userId a string that represents the user Id
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>FinderException</code> if the user was not found
     */
    public void changeContactInformation(ContactInformation info,
                                         String userId) throws
                                         FinderException {
        try {
           Account acct = accountHomeRef.findByPrimaryKey(userId);
           acct.changeContactInformation(info);
         } catch (FinderException fe) {
             throw new FinderException(fe.getMessage());
        } catch (RemoteException re) {
             throw new EJBException(re);
        }
        return;
    }

    /**
     * Create interface of AccountHome component
     * @param userId a string that represents the user
     * @param password a string that represents the password of the user
     * @param status a string the represents the user's status
     * @param info the <code>ContactInformation</code> of the user
     * @return <code>true</code> if create was OK; <code>false</code> ifnot
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>CreateException</code> if account could not be created
     * @throws <code>DuplicateKeyException</code> if an account with the same
     *         user id is already present
     * @throws <code>CustomerAppException</code> if there was an user error
    */
    public boolean createAccount(String userId, String password, String status,
        ContactInformation info) throws CreateException, DuplicateKeyException,
                                        CustomerAppException {
        Account acct = null;
        try {
           if(accountHomeRef == null)
               getReferences();
           acct = accountHomeRef.create(userId, password, status, info);
        } catch (DuplicateKeyException dup) {
            throw new DuplicateKeyException("User exits for " + userId);
        } catch (CreateException re) {
             throw new CreateException(re.getMessage());
        } catch (AccountAppLongIdException re) {
             throw new CustomerAppLongIdException(re.getMessage());
        } catch (AccountAppInvalidCharException re) {
             throw new CustomerAppInvalidCharException(re.getMessage());
        } catch (AccountAppException re) {
             throw new CustomerAppException(re.getMessage());
        } catch (RemoteException re) {
             throw new EJBException(re);
        }
        if(acct == null)
           return(false);
        return(true);
    }

    /**
     * Return the details of an order
     * @param orderId an integer the order Id
     * @return <code>OrderModel</code> that has the order details
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>FinderException</code> if the order details was not found
    */
    public OrderModel getOrderDetails(int orderId) throws FinderException {
        try {
           if(orderHomeRef == null)
               getReferences();
           Order ordr = orderHomeRef.findByPrimaryKey(new Integer(orderId));
           if(ordr == null)
               return(null);
           return(ordr.getDetails());
        } catch (FinderException re) {
             throw new FinderException(re.getMessage());
        } catch (RemoteException re) {
             throw new EJBException(re);
        }
    }

    /**
     * Create interface of Order component
     * @return an integer that represents the order ID
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>CreateException</code> if account could not be created
     * @throws <code>CustomerAppException</code> if there was an user error
    */
    public int createOrder(String userId, Collection lineItems,
                           Address shipToAddr,
        Address billToAddr,  String shipToFirstName,
        String shipToLastName, String billToFirstName,
        String billToLastName, CreditCard chargeCard,
        String carrier, double totalPrice,
        Locale locale) throws CreateException, CustomerAppException {

        try {
           if(orderHomeRef == null)
               getReferences();

           Order ordr = orderHomeRef.create(lineItems, shipToAddr, billToAddr,
                           shipToFirstName, shipToLastName, billToFirstName,
                           billToLastName, chargeCard, carrier, userId,
                           totalPrice, locale);
           if(ordr == null)
               return(-1);
           return(ordr.getDetails().getOrderId());
        } catch (CreateException re) {
             throw new CreateException(re.getMessage());
        } catch (OrderAppException re) {
            throw new CustomerAppException(re.getMessage());
        } catch (RemoteException re) {
             throw new EJBException(re);
        }
    }
}

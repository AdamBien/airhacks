/*
 * $Id: Customer.java,v 1.7.4.6 2001/03/14 10:03:07 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.customer.ejb;

import java.util.Collection;
import java.util.Locale;
import java.rmi.RemoteException;
import javax.ejb.EJBObject;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.DuplicateKeyException;

import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.customer.order.ejb.Order;
import com.sun.j2ee.blueprints.customer.account.ejb.Account;
import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.util.CreditCard;

import com.sun.j2ee.blueprints.customer.customer.exceptions.CustomerAppException;

/**
 * This is the session facade for the customer component. This is implemented
 * as a session bean which exports interfaces of the Account and order
 * components
 */

public interface Customer extends EJBObject {

    /**
     * Gets the account details give an user id
     * @param userId a string that represents the user Id
     * @return the <code>AccountModel</code> that has the account information
     *         corresponding to a customer's account.
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>FinderException</code> if the user details was not found
     */
    public AccountModel getAccountDetails(String userId)
        throws RemoteException, FinderException;

    /**
     * updates the contact information  for the specified account
     * @param info the <code>ContactInformation</code> of the user
     * @param userId a string that represents the user Id
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>FinderException</code> if the user was not found
     */
    public void changeContactInformation(ContactInformation info,
                                         String userId)
        throws RemoteException, FinderException;

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
    public boolean createAccount(String userId, String password,
                          String status, ContactInformation info)
        throws RemoteException, CreateException, DuplicateKeyException, CustomerAppException;

    /**
     * Return the details of an order
     * @param orderId an integer the order Id
     * @return <code>OrderModel</code> that has the order details
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>FinderException</code> if the order details was not found
    */
    public OrderModel getOrderDetails(int orderId)
        throws RemoteException, FinderException;

    /**
     * Create interface of Order component
     * @return an integer that represents the order ID
     * @throws <code>RemoteException</code> for irrecoverable errors
     * @throws <code>CreateException</code> if account could not be created
     * @throws <code>CustomerAppException</code> if there was an user error
    */
    public int createOrder(String userId, Collection lineItems, Address shipToAddr,
                        Address billToAddr,  String shipToFirstName,
                        String shipToLastName, String billToFirstName,
                        String billToLastName, CreditCard chargeCard,
                        String carrier, double totalPrice, Locale locale)
        throws RemoteException, CreateException, CustomerAppException;
}

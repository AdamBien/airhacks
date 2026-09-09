/*
 * $Id: CustomerWebImpl.java,v 1.1.2.7 2001/03/16 19:56:18 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import java.rmi.RemoteException;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.petstore.util.JNDINames;
import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.customer.customer.ejb.Customer;
import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;
import com.sun.j2ee.blueprints.petstore.control.web.ModelUpdateListener;
import com.sun.j2ee.blueprints.petstore.control.ejb.ShoppingClientController;

import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class is the web-tier representation of the Account.
 */
public class CustomerWebImpl implements ModelUpdateListener, java.io.Serializable {

    private ModelManager mm;
    private Customer custEjb;
    private String userId;
    private boolean loggedIn = false;
    private AccountModel account = null;

    public CustomerWebImpl() {}

    public CustomerWebImpl(ModelManager mm) {
        this.mm = mm;
        mm.addListener(JNDINames.CUSTOMER_EJBHOME, this);
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public ContactInformation getContactInformation() {
        if (account == null) return null;
        else return account.getContactInformation();
    }

    public AccountModel getAccount() {
        return account;
    }

    /**
     *  Set by the SigninHandler.doAfter() method
     */

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void performUpdate()  throws EStoreAppException {
        // Get data from the EJB
        if (custEjb == null) {
            custEjb = mm.getCustomerEJB();
        }
        if (custEjb != null) {
            try {
               // check if the user account was ok
               account = custEjb.getAccountDetails(getUserId());
               if (account != null) loggedIn = true;
            } catch (FinderException fe) {
                //
            } catch(RemoteException e) {
                Debug.println("*** CustomerWebImpl: preformUpdate caught " + e);
            }
        }
    }
}


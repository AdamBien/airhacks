/*
 * $Id: AccountModel.java,v 1.5.4.5 2001/03/15 00:25:03 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.account.model;

import java.rmi.RemoteException;

import com.sun.j2ee.blueprints.customer.util.ContactInformation;

/**
 * This class provides methods to view and modify account
 * information for a particular account.
 */
public class AccountModel implements java.io.Serializable {

    protected String userId;
    private String status;
    private ContactInformation info;

    public AccountModel(String userId, String status,
                        ContactInformation info) {
        this.userId = userId;
        this.status = status;
        this.info = info;
    }

    /**
     * Class constructor with no arguments, used by the web tier.
     */
    public AccountModel() {}

    // get and set methods for the instance variables

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public ContactInformation getContactInformation() {
        return info;
    }

    public String toString() {
        String ret = null;
        ret = "userId = " + userId + "\n";
        ret += "status = " + status + "\n";
        ret += "contact info = " + info.toString() + "\n";
        return ret;
    }

    /** shallow copy */
    public void copy(AccountModel other) {
        this.userId = other.userId;
        this.status = other.status;
        this.info = other.info;
    }
}

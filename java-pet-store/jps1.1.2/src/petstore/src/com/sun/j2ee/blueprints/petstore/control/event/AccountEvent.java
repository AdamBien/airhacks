/*
 * $Id: AccountEvent.java,v 1.10.4.4 2001/03/15 00:39:59 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.event;

import java.io.Serializable;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;

/**
 * This event is sent from the web tier to the EJB Controller to notify
 * the EJB Controller that a change needs to be made in the Account
 * and ProfileMgr model data.
 */
public class AccountEvent extends EStoreEventSupport {
    public static final int CREATE_ACCOUNT = 0;
    public static final int DELETE_ACCOUNT = 1;
    public static final int UPDATE_ACCOUNT = 2;
    public static final int REFRESH_ACCOUNT = 3;

    private int actionType;
    private String userId;
    private ContactInformation info;
    private ExplicitInformation eInfo;
    private String status;
    private String password;

    public AccountEvent() {}

    public AccountEvent(int actionType) {
        this.actionType = actionType;
    }

    public void setInfo(String userId, String password,
                        String status, ContactInformation info,
                        ExplicitInformation eInfo) {
        this.actionType = CREATE_ACCOUNT;
        this.userId = userId;
        this.password = password;
        this.status = status;
        this.info = info;
        this.eInfo = eInfo;
    }

    public void setInfo(String userId, ContactInformation info,
                        ExplicitInformation eInfo) {
        this.actionType = UPDATE_ACCOUNT;
        this.userId = userId;
        this.info = info;
        this.eInfo = eInfo;
    }

    public int getActionType() {
        return actionType;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public ContactInformation getContactInformation() {
        return info;
    }

    public ExplicitInformation getExplicitInformation() {
        return eInfo;
    }

    public String getEventName() {
        return "java:comp/env/event/AccountEvent";
    }
}

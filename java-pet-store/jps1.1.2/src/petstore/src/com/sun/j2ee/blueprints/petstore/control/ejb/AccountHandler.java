/*
 * $Id: AccountHandler.java,v 1.15.4.13 2001/03/16 19:56:15 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.ejb;

import java.rmi.RemoteException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.FinderException;
import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.JNDINames;
import com.sun.j2ee.blueprints.petstore.util.EJBUtil;

import com.sun.j2ee.blueprints.petstore.util.EJBKeys;

import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.customer.customer.ejb.Customer;
import com.sun.j2ee.blueprints.customer.customer.ejb.CustomerHome;
import com.sun.j2ee.blueprints.personalization.profilemgr.ejb.ProfileMgr;
import com.sun.j2ee.blueprints.personalization.profilemgr.ejb.ProfileMgrHome;

import com.sun.j2ee.blueprints.petstore.control.event.AccountEvent;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;

import com.sun.j2ee.blueprints.signon.ejb.SignOn;
import com.sun.j2ee.blueprints.signon.ejb.SignOnHome;

import com.sun.j2ee.blueprints.signon.exceptions.SignOnAppException;
import com.sun.j2ee.blueprints.customer.customer.exceptions.CustomerAppException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrAppException;

import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrAppException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.DuplicateAccountException;

public class AccountHandler extends StateHandlerSupport {

  public void perform(EStoreEvent event) throws EStoreEventException {
        AccountEvent ae = (AccountEvent)event;

        switch (ae.getActionType()) {
        case AccountEvent.CREATE_ACCOUNT: {
            Debug.println("AccountHandler (ejb): CREATE_ACCOUNT event");
            try {
               CustomerHome home = EJBUtil.getCustomerHome();
               Customer cust = home.create();
               cust.createAccount(ae.getUserId(), ae.getPassword(), ae.getStatus(), ae.getContactInformation());
               Debug.println("AccountHandler: created user account for " + ae.getUserId());
               ProfileMgrHome pHome = EJBUtil.getProfileMgrHome();
               pHome.create(ae.getUserId(), ae.getExplicitInformation());
               Debug.println("AccountHandler: created user profile for " + ae.getUserId());
               SignOnHome signonHome = EJBUtil.getSignOnHome();
               signonHome.create(ae.getUserId(), ae.getPassword());
               Debug.println("AccountHandler: created signON for " + ae.getUserId());
               machine.setAttribute(EJBKeys.USERNAME, ae.getUserId());
               machine.setAttribute(EJBKeys.PASSWORD, ae.getPassword());
            } catch (DuplicateKeyException dke) {
                throw new DuplicateAccountException("An account already exists for " + ae.getUserId());
            } catch (CreateException ce) {
                throw new EStoreAppException("Unable to create a new account for " + ae.getUserId() + " at this time");
            }  catch (javax.naming.NamingException ce) {
                throw new EJBException("Irrecoverable error creating account: " + ce);
            } catch (ProfileMgrAppException appex) {
                throw new EStoreAppException("Validation of user specified fields failed while creating the profile of user " + ae.getUserId());
            } catch (SignOnAppException signOnex) {
                throw new EStoreAppException("Validation of user specified fields failed while creating the username and password entry for user " + ae.getUserId());
            } catch (CustomerAppException customerex) {
                throw new EStoreAppException("Validation of user specified fields failed while creating an account for user " + ae.getUserId());
            } catch (java.rmi.RemoteException re) {
                throw new EJBException("Irrecoverable error while creating account: " + re);
            }
        } break;
          case AccountEvent.UPDATE_ACCOUNT: {
            Debug.println("AccountHandler (ejb): UPDATE_ACCOUNT event");
            try {
               Debug.println("AccountHandler (ejb): updating user account for " + ae.getUserId());
               CustomerHome home = EJBUtil.getCustomerHome();
               Customer cust = home.create();
               cust.changeContactInformation(ae.getContactInformation(),ae.getUserId());

               Debug.println("AccountHandler (ejb): updating user profile for " + ae.getUserId());
               ProfileMgrHome pHome = EJBUtil.getProfileMgrHome();
               ProfileMgr profileBeanRef = pHome.findByPrimaryKey(ae.getUserId());
               Debug.println("AccountHandler (ejb), perform, updateAccountEvent, eInfo=" + ae.getExplicitInformation().toString());
               profileBeanRef.updateExplicitInformation(ae.getExplicitInformation());

            } catch (java.rmi.RemoteException re) {
                throw new EJBException("Irrecoverable error while updating account: " + re);
            } catch (javax.ejb.CreateException fe) {
                throw new EStoreAppException("Error while creating a cusotmer instance while updating account of user " + ae.getUserId());
            } catch (javax.ejb.FinderException fe) {
                throw new EStoreAppException("Unable to find an account/profile with user id : " + ae.getUserId());
            } catch (javax.naming.NamingException ce) {
                Debug.println("AccountHandler naming exception: " + ce);
                throw new EJBException("Irrecoverable error while updating account : " + ce);
            }
        } break;
        default:
            Debug.print("Error: not implemented yet");
            break;
        }
  }
}


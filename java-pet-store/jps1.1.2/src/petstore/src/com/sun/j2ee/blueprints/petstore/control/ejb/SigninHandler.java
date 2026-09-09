/*
 * $Id: SigninHandler.java,v 1.1.2.9 2001/03/15 00:39:58 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.ejb;

import java.rmi.RemoteException;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.EJBKeys;
import com.sun.j2ee.blueprints.petstore.util.JNDINames;

import com.sun.j2ee.blueprints.customer.customer.ejb.Customer;
import com.sun.j2ee.blueprints.petstore.util.EJBUtil;
import com.sun.j2ee.blueprints.customer.customer.ejb.CustomerHome;
import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.petstore.control.event.SigninEvent;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;

import com.sun.j2ee.blueprints.signon.ejb.SignOn;
import com.sun.j2ee.blueprints.signon.ejb.SignOnHome;
import com.sun.j2ee.blueprints.signon.exceptions.SignOnAppException;
import com.sun.j2ee.blueprints.signon.model.SignOnModel;

import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.SigninFailedException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;

public class SigninHandler extends StateHandlerSupport {

  public void perform(EStoreEvent event) throws EStoreEventException {
      SigninEvent se = (SigninEvent)event;
      Debug.println("SigninEvent: " + se);

      // validate that the password matches the  one in the account

       SignOnModel account = null;
       try {
           SignOnHome home = EJBUtil.getSignOnHome();
           SignOn cust = home.findByPrimaryKey(se.getUserName());
           account = cust.getDetails();
       } catch (FinderException ce) {
           throw new SigninFailedException("Unable to find the password details for user " + se.getUserName());
       } catch (RemoteException re) {
           throw new GeneralFailureException("Irrecoverable error while trying to verify the the user while signin : " + re);
       } catch (javax.naming.NamingException ne) {
           throw new GeneralFailureException("Irrecoverable error while trying to verify the the user while signin : " + ne);
       }

       if ((account != null) & account.getPassWord().equals(se.getPassword())) {
           Debug.println("Password valid for =" + se.getUserName());
           machine.setAttribute(EJBKeys.USERNAME,se.getUserName());
           machine.setAttribute(EJBKeys.PASSWORD,se.getPassword());
       } else {
           Debug.println("Invalide password: for =" + se.getUserName());
              throw new SigninFailedException("password is wrong");
       }
  }
}

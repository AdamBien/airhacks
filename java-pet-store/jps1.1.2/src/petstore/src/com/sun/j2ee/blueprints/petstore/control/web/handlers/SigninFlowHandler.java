/*
 * $Id: SigninFlowHandler.java,v 1.1.2.5 2001/03/15 00:40:08 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web.handlers;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.util.tracer.Debug;

import com.sun.j2ee.blueprints.petstore.control.web.CustomerWebImpl;
import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;
/**
 * SigninFlow Handler
 *
*/
public class SigninFlowHandler implements FlowHandler {


    public void doStart(HttpServletRequest request) {
    }

    public String processFlow(HttpServletRequest request)
        throws EStoreEventException {
        Debug.println("SigninFlowHandler:processRequest");
        ModelManager mm = (ModelManager)request.getSession().getAttribute(WebKeys.ModelManagerKey);
        CustomerWebImpl customer = mm.getCustomerWebImpl();
        String nextScreen = null;
        if (customer.isLoggedIn()) {
            String targetScreen = (String)request.getSession().getAttribute(WebKeys.SigninTargetURL);
            if (targetScreen != null) return "TARGET_URL";
            else return "1";
        } else {
            return nextScreen = "2";
        }
    }

    public void doEnd(HttpServletRequest request) {

    }

}


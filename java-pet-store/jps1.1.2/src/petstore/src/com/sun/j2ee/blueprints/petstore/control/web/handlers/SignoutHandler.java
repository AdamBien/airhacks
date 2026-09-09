/*
 * $Id: SignoutHandler.java,v 1.1.2.5 2001/03/15 00:40:08 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.control.web.handlers;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import javax.ejb.RemoveException;

import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import com.sun.j2ee.blueprints.petstore.control.event.SignoutEvent;
import javax.servlet.http.HttpServletRequest;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;


/**
 * SignoutHandler
 * This class removes the ShoppingClientControllerEJB and destroys
 * the current HttpSession.
 *
*/
public class SignoutHandler extends RequestHandlerSupport {

    public EStoreEvent processRequest(HttpServletRequest request){
        Debug.println("Creating Signout Event");
        return new SignoutEvent();
    }

    public void doEnd(HttpServletRequest request) {
        ModelManager mm = (ModelManager)request.getSession().getAttribute(WebKeys.ModelManagerKey);
        Debug.println("Signout doEnd");
        // get locale so the log off message can be in the correct language
        String language = mm.getProfileMgrModel().getExplicitInformation().getLangPref();
        Locale locale = JSPUtil.getLocaleFromLanguage(language);
        try {
        //remove the ShoppingClientControllerEJB
         mm.getSCCEJB().remove();
        } catch (java.rmi.RemoteException rex) {
            Debug.println("SignoutHandler error removing ShoppingClientController: " + rex);
        } catch (RemoveException rem) {
            Debug.println("SignoutHandler error removing ShoppingClientController: " + rem);
        }
        request.getSession().invalidate();
        // get new session and put in a new gui controller
        HttpSession validSession = request.getSession(true);
        // put the previous language in the session so the proper signout message is displayed
        request.getSession().setAttribute(WebKeys.LanguageKey, locale);
        mm = new ModelManager();
        request.getSession().setAttribute(WebKeys.ModelManagerKey, mm);
        mm.init(context, validSession);
        request.getSession().setAttribute(WebKeys.ModelManagerKey, mm);
    }
}

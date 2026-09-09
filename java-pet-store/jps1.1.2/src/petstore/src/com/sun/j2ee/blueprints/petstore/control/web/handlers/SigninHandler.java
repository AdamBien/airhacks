/*
 * $Id: SigninHandler.java,v 1.1.2.9 2001/04/03 21:38:59 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.control.web.handlers;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Enumeration;
import javax.servlet.http.HttpSession;

import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import com.sun.j2ee.blueprints.petstore.control.event.SigninEvent;
import javax.servlet.http.HttpServletRequest;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;
import com.sun.j2ee.blueprints.petstore.control.web.CustomerWebImpl;

/**
 * SigninHandler
 *
*/
public class SigninHandler extends RequestHandlerSupport {

    public EStoreEvent processRequest(HttpServletRequest request){
        Debug.println("Signin Handler: processRequest()");
        Locale currentLocale = JSPUtil.getLocale(request.getSession());
        String userName =  request.getParameter("j_username");
        if (currentLocale.equals(Locale.JAPAN)) userName = JSPUtil.convertJISEncoding(userName);
        String password =  request.getParameter("j_password");
        if (currentLocale.equals(Locale.JAPAN)) password = JSPUtil.convertJISEncoding(password);
        String targetScreen =  request.getParameter("target_screen");
        // set the userId in the CustomerWebImpl
        ModelManager mm = (ModelManager)request.getSession().getAttribute(WebKeys.ModelManagerKey);
        CustomerWebImpl customer = mm.getCustomerWebImpl();
        customer.setUserId(userName);
        return new SigninEvent(userName, password);
    }

    public void doEnd(HttpServletRequest request) {
        ModelManager mm = (ModelManager)request.getSession().getAttribute(WebKeys.ModelManagerKey);
        if (request.getSession().getAttribute("language") == null) {
                    // get the language here
                    String language = mm.getProfileMgrModel().getExplicitInformation().getLangPref();
                    Debug.println("RequestProcessor: settting language to : " + language);
                    // set the locale here
                    Locale locale = JSPUtil.getLocaleFromLanguage(language);
                    request.getSession().setAttribute(WebKeys.LanguageKey, locale);
                    Debug.println("Account Handler set language to: " + language);
                } else {
                    Debug.println("RequestProcessor: language has already been set");
                }
        }

}



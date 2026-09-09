/*
 * $Id: MainServlet.java,v 1.14.4.8 2001/03/15 00:40:02 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.beans.Beans;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import com.sun.j2ee.blueprints.petstore.util.JNDINames;

import com.sun.j2ee.blueprints.petstore.control.exceptions.SigninFailedException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;
import com.sun.j2ee.blueprints.petstore.control.web.CustomerWebImpl;
import com.sun.j2ee.blueprints.petstore.control.web.ScreenFlowXmlDAO;
import com.sun.j2ee.blueprints.petstore.control.web.RequestProcessor;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;

public class MainServlet extends HttpServlet {

    private HashMap urlMappings;

    public void init() {
        Debug.println("MainServlet: Initializing");
        String requestMappingsURL = null;
        try {
            requestMappingsURL = getServletContext().getResource("/WEB-INF/xml/requestmappings.xml").toString();
        } catch (java.net.MalformedURLException ex) {
            Debug.println("ScreenFlowManager: initializing ScreenFlowManager malformed URL exception: " + ex);
        }
       urlMappings = ScreenFlowXmlDAO.loadRequestMappings(requestMappingsURL);
       getServletContext().setAttribute(WebKeys.URLMappingsKey, urlMappings);
       Debug.println("MainServlet: loaded urlMappings");
       String serverType = null;
       try {
            InitialContext ic = new InitialContext();
            serverType = (String)ic.lookup(JNDINames.SERVER_TYPE);
            getServletContext().setAttribute(WebKeys.ServerTypeKey, serverType);
        } catch (NamingException ex) {
            Debug.println("Server Type not specified in deployment descriptor: using default J2ee Security Adapter");
        }
        getScreenFlowManager();
        getRequestProcessor();
        Debug.println("MainServlet: Initialization complete");
    }

     public  void doPost(HttpServletRequest request, HttpServletResponse  response)
        throws IOException, ServletException {
        doGet(request, response);
    }

    public  void doGet(HttpServletRequest request, HttpServletResponse  response)
        throws IOException, ServletException {
        String selectedURL = request.getPathInfo();
        // the current tomcat is resetting the outputstream so this is a workaround
        if ((selectedURL != null) && selectedURL.equals("/white")) return;
        HttpSession session = request.getSession();
        ScreenFlowManager screenManager = null;
        ModelManager modelManager= (ModelManager)request.getSession().getAttribute(WebKeys.ModelManagerKey);
        if ( modelManager == null ) {
            try {
                        modelManager = (ModelManager) Beans.instantiate(this.getClass().getClassLoader(), "com.sun.j2ee.blueprints.petstore.control.web.ModelManager");
             } catch (Exception exc) {
                 throw new ServletException ("Cannot create bean of class ModelManager");
             }
             session.setAttribute(WebKeys.ModelManagerKey, modelManager);
             modelManager.init(getServletContext(), session);
         }
         Debug.println("MainServlet: url " + selectedURL);
         // check if url is protected before processing request
         URLMapping current = getURLMapping(selectedURL);
         if ((current != null) && current.requiresSignin()) {
             CustomerWebImpl customer = modelManager.getCustomerWebImpl();
             if (customer.isLoggedIn()) {
                 doProcess(request);
             } else {
                 String signinScreen = getScreenFlowManager().getSigninScreen();
                 session.setAttribute(WebKeys.CurrentScreen, signinScreen);
                 session.setAttribute(WebKeys.SigninTargetURL, selectedURL);
             }
         } else {
             doProcess(request);
         }
            /**
                Default to the base language or the site.
             If a language is found in the session use that template.
             */
             Locale locale = JSPUtil.getLocale(request.getSession());
             getServletConfig().getServletContext().getRequestDispatcher(getScreenFlowManager().getTemplate(locale)).forward(request, response);

    }

    private void doProcess(HttpServletRequest request) throws ServletException {
        try {
                 getRequestProcessor().processRequest(request);
                 getScreenFlowManager().getNextScreen(request);
        } catch (Throwable ex) {
            String className = ex.getClass().getName();
            String exceptionScreen = getScreenFlowManager().getExceptionScreen(className);
            Debug.println("MainServlet: target screen is: " + exceptionScreen);
            // put the exception in the request
            request.setAttribute("javax.servlet.jsp.jspException", ex);
            if (exceptionScreen != null) {
                request.getSession().setAttribute(WebKeys.CurrentScreen, exceptionScreen);
            } else {
                // send to general error screen
                Debug.println("MainServlet: unknown exception: " + className);
                throw new ServletException("MainServlet: unknown exception: " + className);
           }
       }
    }

    private RequestProcessor getRequestProcessor() {
         RequestProcessor rp = (RequestProcessor)getServletContext().getAttribute(WebKeys.RequestProcessorKey);
         if ( rp == null ) {
             Debug.println("MainServlet: initializing request processor");
             rp = new RequestProcessor();
             rp.init(getServletContext());
             getServletContext().setAttribute(WebKeys.RequestProcessorKey, rp);
        }
       return rp;
    }

    private ScreenFlowManager getScreenFlowManager() {
            ScreenFlowManager screenManager = (ScreenFlowManager)getServletContext().getAttribute(WebKeys.ScreenManagerKey);
            if (screenManager == null ) {
                Debug.println("MainServlet: Loading screen flow definitions");
                screenManager = new ScreenFlowManager();
                screenManager.init(getServletContext());
                getServletContext().setAttribute(WebKeys.ScreenManagerKey, screenManager);
             }
        return screenManager;
    }

    /**
     * The UrlMapping object contains information that will match
     * a url to a mapping object that contains information about
     * the current screen, the RequestHandler that is needed to
     * process a request, and the RequestHandler that is needed
     * to insure that the propper screen is displayed.
    */

    private URLMapping getURLMapping(String urlPattern) {
        if ((urlMappings != null) && urlMappings.containsKey(urlPattern)) {
            return (URLMapping)urlMappings.get(urlPattern);
        } else {
            return null;
        }
    }

}

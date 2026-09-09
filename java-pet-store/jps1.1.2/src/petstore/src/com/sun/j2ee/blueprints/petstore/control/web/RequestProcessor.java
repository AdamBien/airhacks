/*
 * $Id: RequestProcessor.java,v 1.13.4.14 2001/03/16 19:56:20 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */
package com.sun.j2ee.blueprints.petstore.control.web;

import java.util.Collection;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.rmi.RemoteException;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;
import com.sun.j2ee.blueprints.petstore.control.web.ScreenFlowManager;

import com.sun.j2ee.blueprints.petstore.control.web.handlers.RequestHandler;


/**
 * This is the web tier controller for the sample application.
 *
 * This class is responsible for processing all requests received from
 * the Main.jsp and generating necessary events to modify data which
 * are sent to the ShoppingClientControllerWebImpl.
 *
 */
public class RequestProcessor implements java.io.Serializable {

    private ServletContext context;
    private HashMap urlMappings;

    /** Empty constructor for use by the JSP engine. */
    public RequestProcessor() {}


    public void init(ServletContext context) {
        this.context = context;
        urlMappings = (HashMap)context.getAttribute(WebKeys.URLMappingsKey);
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


    /**
    * This method is the core of the RequestProcessor. It receives all requests
    *  and generates the necessary events.
    */
    public void processRequest(HttpServletRequest request) throws EStoreEventException {
        EStoreEvent event = null;
        String selectedUrl = request.getPathInfo();
        ModelManager mm = (ModelManager)request.getSession().getAttribute(WebKeys.ModelManagerKey);
        ShoppingClientControllerWebImpl scc = (ShoppingClientControllerWebImpl)request.getSession().getAttribute(WebKeys.WebControllerKey);
        if (scc == null) {
            scc = new ShoppingClientControllerWebImpl(request.getSession());
            mm.setSCC(scc);
            request.getSession().setAttribute(WebKeys.WebControllerKey, scc);
        }
       RequestHandler handler = getHandler(selectedUrl);
       if (handler != null) {
           handler.setServletContext(context);
           handler.doStart(request);
           event = handler.processRequest(request);
           if (event != null) {
                   Collection updatedModelList = scc.handleEvent(event);
                   mm.notifyListeners(updatedModelList);
           }
           handler.doEnd(request);
        }
    }

    /**
     * This method load the necessary RequestHandler class necessary to process a the
     * request for the specified URL.
     */

    private RequestHandler getHandler(String selectedUrl) {
        RequestHandler handler = null;
        URLMapping urlMapping = getURLMapping(selectedUrl);
        String requestProcessorString = null;
        if (urlMapping != null) {
            requestProcessorString = urlMapping.getRequestHandler();
            if (urlMapping.useRequestHandler()) {
                try {
                    Debug.println("RequestProcessor: loading handler " + requestProcessorString);
                    handler = (RequestHandler)getClass().getClassLoader().loadClass(requestProcessorString).newInstance();
                    Debug.println("RequestProcessor: loaded handler " + requestProcessorString);
                } catch (Exception ex) {
                   Debug.println("RequestProcessor caught loading handler: " + ex);
                }
            }
        }
        return handler;
    }

}

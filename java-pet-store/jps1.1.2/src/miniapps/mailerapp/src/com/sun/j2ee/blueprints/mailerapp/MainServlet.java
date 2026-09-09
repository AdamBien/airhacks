/*
 * $Id: MainServlet.java,v 1.1.2.6 2001/04/10 01:35:52 ro89390 Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.mailerapp;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

/**
 * The single entry point for all web requests.
 */
public class MainServlet extends HttpServlet {

    /** The mappings of URLs to actual JSP pages. */
    protected Map nameSpace = new HashMap();

    /** The handler that deals with sending e-mail. */
    protected MailHandler mailHandler = null;


    // ------------------------------
    // Overridden HttpServlet methods
    // ------------------------------

    public void init() {
        initPathMapping();
        mailHandler = new MailHandler();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        process(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        process(req, resp);
    }


    // --------------
    // Helper methods
    // --------------

    /**
     * Processes the given HTTP request and provides an appropriate
     * response.
     */
    protected void process(HttpServletRequest req,
                           HttpServletResponse resp)
        throws IOException, ServletException {

        resp.setContentType("text/html");
        String requestURL = req.getPathInfo();
        String responseURL = null;

        // If the given request should be handled by the mail handler,
        // let it handle the request. Otherwise, send the appropriate
        // page to the client.
        if ("/sendMail".equals(requestURL)) {
            try {
                mailHandler.handle(req, resp);
                responseURL = getResponseURL(requestURL);
            }
            catch (MailHandlerException mhe) {
                req.setAttribute("error_message", mhe.getMessage());
                responseURL = getResponseURL("/error");
            }
        }
        else {
            responseURL = getResponseURL(requestURL);
        }

        getServletConfig().getServletContext()
            .getRequestDispatcher(responseURL).forward(req, resp);
    }

    /**
     * Returns the page to which the given URL is mapped.
     */
    protected String getResponseURL(String u) {
        return (String) nameSpace.get(u);
    }

    /**
     * Initialize table which maps URLs to JSP pages.
     */
    protected void initPathMapping() {
        nameSpace.put("/index", "/index.jsp");
        nameSpace.put("/entermaildata", "/entermaildata.jsp");
        nameSpace.put("/error", "/error.jsp");

        // When an e-mail is sent via POST, the /sendMail
        // URL is requested.
        nameSpace.put("/sendMail", "/showmail.jsp");
    }
}

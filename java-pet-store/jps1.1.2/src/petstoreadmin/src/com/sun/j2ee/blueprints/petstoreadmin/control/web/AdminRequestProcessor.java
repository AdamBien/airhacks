/*
 * $Id: AdminRequestProcessor.java,v 1.4.4.2 2001/03/14 23:56:42 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstoreadmin.control.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.j2ee.blueprints.petstoreadmin.control.ejb.AdminClientControllerHome;
import com.sun.j2ee.blueprints.petstoreadmin.control.ejb.AdminClientController;


public class AdminRequestProcessor extends HttpServlet {

    private boolean fromDoGet = false;
    private String ACC_NAME = "java:comp/env/ejb/acc/Acc";

    private void doUpdate(HttpServletRequest req) {

        String orderId = null;
        ArrayList updateOrderList = new ArrayList();
        Enumeration e = req.getParameterNames();
        AdminClientControllerHome accHome = null;
        AdminClientController accEJB = null;

        while ((e != null) && e.hasMoreElements()) {
            String param = ((String)e.nextElement()).trim();
            if ((param != null) && param.startsWith("order_")) {
                // get the order id number from the parameter
                orderId = param.substring("order_".length(),param.length());
                if(orderId != null) {
                    String newStatus =
                        req.getParameter("status_" + orderId);
                    if(newStatus.equals("approved") ||
                                    newStatus.equals("denied")) {
                        updateOrderList.add(orderId);
                        updateOrderList.add(newStatus);
                    }
                }
            }
        }
        try {
            InitialContext initial = new InitialContext();
            Object objref = initial.lookup(ACC_NAME);
            accHome = (AdminClientControllerHome)
            PortableRemoteObject.narrow(objref,AdminClientControllerHome.class);
            accEJB = accHome.create();
            accEJB.setOrdersStatus(updateOrderList);
        } catch (CreateException ne) {
            throw new RuntimeException("Create Exception AdmRP SetORder "+
                                ne.getMessage());
        } catch (NamingException ne) {
            throw new RuntimeException("Naming Exception AdmRP SetORder "+
                                ne.getMessage());
        } catch (RemoteException ne) {
            throw new RuntimeException("Remote Exception AdmRP SetORder "+
                                ne.getMessage());
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws java.io.IOException, javax.servlet.ServletException {
        fromDoGet = true;
        doPost(req, resp);
        fromDoGet = false;
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws java.io.IOException, javax.servlet.ServletException {

        String curScreen;

        resp.setContentType("text/html");

        if(fromDoGet) {
            getServletConfig().getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
        }
        else {
            curScreen = req.getParameter("currentScreen").trim();
            if(curScreen.equals("logout")) {
                getServletConfig().getServletContext().getRequestDispatcher("/logout.jsp").forward(req, resp);
            }
            if(curScreen.equals("manageorders")) {
                getServletConfig().getServletContext().getRequestDispatcher("/manageorders.jsp").forward(req, resp);
            }
            if(curScreen.equals("updateorders")) {
                doUpdate(req);
                getServletConfig().getServletContext().getRequestDispatcher("/back.jsp").forward(req, resp);
            }
        }
    }
}

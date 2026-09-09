/*
 * $Id: ManageOrdersBean.java,v 1.2.4.4 2001/03/14 23:56:42 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstoreadmin.control.web;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.io.StringWriter;

import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;
import java.io.IOException;
import java.rmi.RemoteException;
import javax.naming.NamingException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import com.sun.xml.tree.XmlDocument;

import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.petstoreadmin.control.ejb.AdminClientControllerHome;
import com.sun.j2ee.blueprints.petstoreadmin.control.ejb.AdminClientController;

/**
 * This class is used to retrieve the number of "pending" orders that
 * have been placed for the Administrator.
 *
 * This class is also used to support the interoperability demo
 * using MS Excel to view a placed order. For more information
 * about the MS Excel demo for the pet store, see the readme.html
 * file that comes with the petstore download.
 * This class is used to retrieve a set number of orders that
 * have been placed.
 */
public class ManageOrdersBean {

    // methods for the Administrtor unit
    public void init() {}

    public ManageOrdersBean() {}

    private AdminClientController getACC()
        throws NamingException, CreateException, RemoteException {

        InitialContext initial = new InitialContext();
        Object objref = initial.lookup("java:comp/env/ejb/acc/Acc");
        AdminClientControllerHome ref = (AdminClientControllerHome)
        PortableRemoteObject.narrow(objref, AdminClientControllerHome.class);
        return(ref.create());
    }

    public Collection getPendingOrders(Locale locale) {
        Collection orderColl = null;

        try {
            AdminClientController acc = getACC();
            orderColl = acc.getPendingOrders(locale);
        } catch(CreateException ne) {
            throw new RuntimeException("Create Ex while findOrdersBystatus :" +
                    ne.getMessage());
        } catch(NamingException ne) {
            throw new RuntimeException("Naming Ex while findOrdersBystatus :" +
                    ne.getMessage());
        } catch(RemoteException ne) {
            throw new RuntimeException("Remote Ex while findOrdersBystatus :" +
                    ne.getMessage());
        }
        return orderColl;
    }

    // methods for the Excel XML Interoperability demo
    public String getPendingOrdersXML() {
        return getPendingOrdersXML(-1);
    }

    /** @param  max   (if >= 0) specifies the maximum
     *                  number of orders to return.
     *                -1 (or < 0) if unlimited.
     */
    public String getPendingOrdersXML(int max) {
        Collection orderColl;
        DocumentBuilder builder = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException("ManageOrdersBean: caught ParserConfigurationException");
        }
        Document doc = builder.newDocument();
        Element root = doc.createElement("Orders");
        doc.appendChild(root);
        try {
            AdminClientController acc = getACC();
            // default to US orders for now
            orderColl = acc.getPendingOrders(Locale.US);
            int i = 0;
            for (Iterator it = orderColl.iterator();
                     (max < 0 || i < max) && it.hasNext(); ++i) {
                OrderModel details = (OrderModel) it.next();
                root.appendChild(details.toXml(doc));
            }
        } catch(CreateException ne) {
            throw new RuntimeException("Create Ex while findOrdersBystatus :" +
                    ne.getMessage());
        } catch(NamingException ne) {
            throw new RuntimeException("Naming Ex while findOrdersBystatus :" +
                    ne.getMessage());
        } catch(RemoteException ne) {
            throw new RuntimeException("Remote Ex while findOrdersBystatus :" +
                    ne.getMessage());
        }
        StringWriter sw = new StringWriter();
        try {
            ((XmlDocument)doc).write(sw);
        } catch (IOException ioe) {
            // ignore..
        }
        return sw.toString();
    }
}

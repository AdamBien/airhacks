
/*
 * $Id: StateMachine.java,v 1.8.4.4 2001/03/15 00:39:59 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 *
 */


package com.sun.j2ee.blueprints.petstore.control.ejb;

import java.rmi.RemoteException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.SessionContext;

import java.util.Collection;
import java.util.HashMap;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;

import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;

/**
 * This class is a Universal front back end of an application
 * which ties all EJB components together dynamically at runtime.
 *
 * This class should not be updated to handle various event types.
 * This class will use ActionHandlers to handle events that require
 * processing beyond the scope of this class.
 *
 * A second option to event handling is to do so in the XML descriptor
 * itself.
 *
 * State may be stored in the attributeMap
 * Configuration of this file is via an XML descriptor.

 *
 */
public class StateMachine implements java.io.Serializable {

    private ShoppingClientControllerEJB sccejb;
    private ModelUpdateManager mum;
    private HashMap orderTable;
    private HashMap attributeMap;
    private HashMap handlerMap;
    private SessionContext sc;

    public StateMachine(ShoppingClientControllerEJB sccejb, SessionContext sc) {
        this.sccejb = sccejb;
        this.sc = sc;
        this.mum = new ModelUpdateManager();
        attributeMap = new HashMap();
        handlerMap = new HashMap();
    }

    public Collection handleEvent(EStoreEvent ese) throws EStoreEventException{
        Debug.println("StateMachine: received event= " + ese);
        String eventName = ese.getEventName();
        if (eventName != null) {
            Debug.println("StateMachine: processingEvent= " + eventName);
            String handlerName = getHandlerName(eventName);
            Debug.println("StateMachine: process handler class = " + handlerName);
            StateHandler handler = null;
            try {
                 if (handlerMap.get(eventName) != null) {
                    handler = (StateHandler)handlerMap.get(eventName);
                 } else {
                     handler = (StateHandler)Class.forName(handlerName).newInstance();
                     handlerMap.put(eventName, handler);
             }
            } catch (Exception ex) {
                Debug.println("StateMachine: error loading " + handlerName + " :" + ex);
            }
            if (handler != null) {
                Debug.println("StateMachine: loaded handler " + handlerName);
                handler.init(this);
                Debug.println("StateMachine: initialzied " + handlerName);
                // do the magic
                handler.doStart();
                handler.perform(ese);
                handler.doEnd();
                Debug.println("StateMachine: sucessfully processed :" + eventName);
            }
        }
        return (mum.getUpdatedModels(ese));
    }

    private String getHandlerName(String eventName) {
        // do the lookup
        Debug.println("StateMachine: looking up:" + eventName + "<");
        try {
                    InitialContext ic = new InitialContext();
                    return  (String)ic.lookup(eventName);
        } catch (javax.naming.NamingException ex) {
                   Debug.println("AccountHandler caught: " + ex);
                    // ignore.. we are working around it below.
        }
        return null;

    }


    public void setAttribute(String key, Object value) {
        attributeMap.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributeMap.get(key);
    }

    public ShoppingClientControllerEJB getShoppingClientControllerEJB() {
        return sccejb;
    }

    public SessionContext getSessionContext() {
        return sc;
    }

}

/*
 * $Id: ModelUpdateManager.java,v 1.9.4.6 2001/04/05 21:51:39 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */


package com.sun.j2ee.blueprints.petstore.control.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.ejb.EJBException;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.JNDINames;

import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import com.sun.j2ee.blueprints.petstore.control.event.CartEvent;
import com.sun.j2ee.blueprints.petstore.control.event.SigninEvent;
import com.sun.j2ee.blueprints.petstore.control.event.AccountEvent;
import com.sun.j2ee.blueprints.petstore.control.event.OrderEvent;
import com.sun.j2ee.blueprints.petstore.control.event.SigninEvent;
import com.sun.j2ee.blueprints.petstore.control.event.LanguageChangeEvent;

/**
 * This class uses the EStoreEvent type to deduce the list of
 * models that need to be updated because of this event.
 */
public class ModelUpdateManager  implements java.io.Serializable {

    public ModelUpdateManager() {
    }

     /**
     * @return a list of names of models that could have changed due to this event.
     * The names chosen to refer to models is taken from JNDINames.
     * @see com.sun.j2ee.blueprints.petstore.util.JNDINames
     */
    public Collection getUpdatedModels(EStoreEvent ese) {
        Debug.println("ModelUpdateManager: getUpdateModels");

        ArrayList modelList = new ArrayList();

        if (ese instanceof CartEvent) {
                Debug.println("ModelUpdateManager: CartEvent");
                modelList.add(JNDINames.CART_EJBHOME);
        } else if (ese instanceof LanguageChangeEvent) {
                Debug.println("ModelUpdateManager: LanguageEvent");
                modelList.add(JNDINames.CART_EJBHOME);
        } else if (ese instanceof AccountEvent) {
                Debug.println("ModelUpdateManager: AccountEvent");
                modelList.add(JNDINames.CUSTOMER_EJBHOME);
                modelList.add(JNDINames.PROFILEMGR_EJBHOME);
                // cover for language change that may occur
                modelList.add(JNDINames.CART_EJBHOME);
        } else if (ese instanceof OrderEvent) {
        // do not need this functionality right now, OrderBean does not
        // need to know of model change event.

                Debug.println("ModelUpdateManager: OrderEvent");
                modelList.add(JNDINames.CUSTOMER_EJBHOME);
                Debug.println("ModelUpdateManager: OrderEvent : customer done");
                modelList.add(JNDINames.INVENTORY_EJBHOME);
                modelList.add(JNDINames.CART_EJBHOME);
        } else if (ese instanceof SigninEvent) {
                Debug.println("ModelUpdateManager: SigninEvent");
                modelList.add(JNDINames.CUSTOMER_EJBHOME);
                modelList.add(JNDINames.PROFILEMGR_EJBHOME);
                // language may have changed so add the cart to the list
                modelList.add(JNDINames.CART_EJBHOME);
        }
        return modelList;
    }
}

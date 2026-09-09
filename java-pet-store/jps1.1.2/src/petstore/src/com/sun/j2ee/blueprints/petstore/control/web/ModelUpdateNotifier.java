/*
 * $Id: ModelUpdateNotifier.java,v 1.7.4.4 2001/03/16 19:56:19 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;

/**
 * This class is responsible for providing methods to add objects as listeners
 * for a particular model update event and for notifying the listeners when the
 * event actually occurs.
 */
public class ModelUpdateNotifier implements java.io.Serializable {

    private HashMap listenerMap;

    public ModelUpdateNotifier() {
        listenerMap = new HashMap();
    }

    public void notifyListeners(Collection updatedModelList) throws
                                                    EStoreAppException {

        for (Iterator it1 = updatedModelList.iterator() ; it1.hasNext() ;) {
            String modelType = (String) it1.next();
            Collection listeners = (Collection)listenerMap.get(modelType);
            if (listeners != null) {
                for (Iterator it2 = listeners.iterator(); it2.hasNext(); ) {
                    ((ModelUpdateListener) it2.next()).performUpdate();
                }
            }
        }
    }

    public void addListener(String modelType, Object listener) {

        if (listenerMap.get(modelType) == null) {
            ArrayList listeners = new ArrayList();
            listeners.add(listener);
            listenerMap.put(modelType,listeners);
        } else {
            ((ArrayList) listenerMap.get(modelType)).add(listener);
        }
    }
}


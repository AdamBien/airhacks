/*
 * $Id: OrderWebImpl.java,v 1.9.4.6 2001/03/16 19:56:19 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;
import com.sun.j2ee.blueprints.petstore.control.web.ModelUpdateListener;
import com.sun.j2ee.blueprints.customer.order.model.OrderModel;
import com.sun.j2ee.blueprints.customer.customer.ejb.Customer;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.JNDINames;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;

/**
 * This class is the web-tier representation of the order.
 * It is used in the web tier to get an order that has been
 * placed. The requestId represents the order that has been
 * placed. If no order has been placed the requestId will
 * remain as -l.
 */
public class OrderWebImpl extends OrderModel
    implements ModelUpdateListener, java.io.Serializable {

    private ModelManager mm;
    private Customer custEjb;
    private HttpSession session;
    private int requestId = -1;

    public OrderWebImpl() {
        super();
    }

    public void init(HttpServletRequest request){
        this.session = request.getSession();
        Integer id = (Integer)request.getAttribute(WebKeys.RequestIdKey);
        if (id != null) requestId = id.intValue();
        this.mm = (ModelManager)session.getAttribute(WebKeys.ModelManagerKey);
        syncUpData();
    }

    private void syncUpData() {
        int orderId = -1;

        if (custEjb == null) {
            try {
                custEjb = mm.getCustomerEJB();
            } catch (EStoreAppException fe) {
                throw new GeneralFailureException("Error while getting order with Id " + orderId + " : " + fe.getMessage());
            }
        }

        Debug.assert(requestId != -1);
        try {
            orderId = mm.getSCCEJB().getOrder(requestId);
            if (custEjb != null)
                copy(custEjb.getOrderDetails(orderId));
        } catch (FinderException fe) {
            throw new GeneralFailureException("Error while getting order with Id " + orderId + " : " + fe.getMessage());
        } catch (RemoteException re) {
            throw new GeneralFailureException("Error while getting order with Id " + orderId + " : " + re.getMessage());
        }
    }

    public void performUpdate() throws EStoreAppException {
        // do nothing - this is a request scope bean
    }
}


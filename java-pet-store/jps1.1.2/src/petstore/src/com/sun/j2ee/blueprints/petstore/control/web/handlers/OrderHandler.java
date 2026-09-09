/*
 * $Id: OrderHandler.java,v 1.3.4.3 2001/03/16 19:56:20 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.control.web.handlers;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import javax.servlet.http.HttpSession;

import com.sun.j2ee.blueprints.petstore.util.JSPUtil;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import javax.servlet.http.HttpServletRequest;
import com.sun.j2ee.blueprints.petstore.control.event.OrderEvent;
import com.sun.j2ee.blueprints.customer.util.CreditCard;
import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.util.ContactInformation;
import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.customer.account.model.AccountModel;
import com.sun.j2ee.blueprints.shoppingcart.cart.model.ShoppingCartModel;

import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;

/**
 * OrderHandler
 *
*/
public class OrderHandler extends RequestHandlerSupport {
    ModelManager mm = null;

    public EStoreEvent processRequest(HttpServletRequest request){
        Debug.println("Creating Order Event");
        if (mm == null) mm = (ModelManager)request.getSession().getAttribute(WebKeys.ModelManagerKey);
        AccountModel account = mm.getCustomerWebImpl().getAccount();
        int requestId = JSPUtil.getEventId();
        ShoppingCartModel cart = mm.getCartModel();
        OrderEvent oe = new OrderEvent();
        HttpSession session = request.getSession();
        ContactInformation shippingInfo = (ContactInformation)
            session.getAttribute(WebKeys.ShippingContactInfoKey);
        ContactInformation billingInfo = (ContactInformation)
            session.getAttribute(WebKeys.BillingContactInfoKey);
        String carrier = (String) session.getAttribute(WebKeys.CarrierKey);
        Locale locale = JSPUtil.getLocale(session);
        CreditCard creditCard = (CreditCard)
        session.getAttribute(WebKeys.CreditCardKey);
        oe.setInfo(requestId, shippingInfo.getAddress(),
                   billingInfo.getAddress(),
                   shippingInfo.getGivenName(),
                   shippingInfo.getFamilyName(),
                   billingInfo.getGivenName(),
                   billingInfo.getFamilyName(),
                   creditCard, "UPS", locale);
        // set up the request id attribute so that the pages can
        // create orderwebimpl correctly.
        request.setAttribute(WebKeys.RequestIdKey, new Integer(requestId));
        return oe;
    }
}


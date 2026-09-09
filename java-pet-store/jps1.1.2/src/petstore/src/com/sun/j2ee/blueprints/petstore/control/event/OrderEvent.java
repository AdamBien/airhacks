/*
 * $Id: OrderEvent.java,v 1.7.4.2 2001/03/15 00:40:00 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.event;

import java.util.Collection;
import java.io.Serializable;
import com.sun.j2ee.blueprints.customer.util.Address;
import java.util.Locale;
import com.sun.j2ee.blueprints.customer.util.CreditCard;

/**
 * This event is sent from the web tier to the EJB Controller to notify
 * the EJB Controller that the user wants to change the Order model
 * data.
 */
public class OrderEvent extends EStoreEventSupport {

    public static final int CREATE_ORDER = 0;
    public static final int DELETE_ORDER = 1;
    public static final int UPDATE_ORDER = 2;

    private int actionType;
    private String carrier;
    private Address shippingAddress;
    private Address billingAddress;
    private String shipToFirstName;
    private String shipToLastName;
    private String billToFirstName;
    private String billToLastName;
    private CreditCard creditCard;
    private Locale locale;
    private int requestId;

    public OrderEvent() {}

    public void setInfo(int requestId,
        Address shippingAddress, Address billingAddress,
        String shipToFirstName,String shipToLastName,
        String billToFirstName,String billToLastName,
        CreditCard creditCard, String carrier,
        Locale locale) {
        this.actionType = CREATE_ORDER;
        this.requestId = requestId;
        this.shippingAddress =shippingAddress;
        this.billingAddress = billingAddress;
        this.shipToFirstName = shipToFirstName;
        this.shipToLastName = shipToLastName;
        this.billToFirstName = billToFirstName;
        this.billToLastName = billToLastName;
        this.creditCard = creditCard;
        this.carrier = carrier;
        this.locale = locale;
    }

    public Locale getLocale() {
        return locale;
    }

    public int getActionType() {
        return actionType;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public String getShipToFirstName() {
        return shipToFirstName;
    }
    public String getShipToLastName() {
        return shipToLastName;
    }
    public String getBillToFirstName() {
        return billToFirstName;
    }
    public String getBillToLastName() {
        return billToLastName;
    }

    public String getCarrier() {
        return carrier;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public int getRequestId() {
        return requestId;
    }

    public String getEventName() {
        return "java:comp/env/event/OrderEvent";
    }
}

/*
 * $Id: MutableOrderModel.java,v 1.1.2.1 2001/03/10 10:12:29 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.order.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import com.sun.j2ee.blueprints.customer.util.Calendar;
import com.sun.j2ee.blueprints.customer.util.Address;
import com.sun.j2ee.blueprints.customer.util.CreditCard;
import com.sun.j2ee.blueprints.customer.order.model.OrderModel;

/**
 * This class represents the model data for an order.
 * It is a value-object and has fine-grained getter
 * methods.
 */
public class MutableOrderModel extends OrderModel {

    public MutableOrderModel(int orderId, Collection lineItems,
                      Address shipToAddr, Address billToAddr,
                      String shipToFirstName, String shipToLastName,
                      String billToFirstName, String billToLastName,
                      CreditCard chargeCard, String carrier,
                      String userId, Calendar orderDate,
                      String status, double totalPrice, Locale locale) {
        super(orderId, lineItems, shipToAddr, billToAddr, shipToFirstName,
              shipToLastName, billToFirstName, billToLastName, chargeCard,
              carrier, userId, orderDate, status, totalPrice, locale);
    }

    public void setOrderId(int id) {
        this.orderId = id;
    }

    public void setLineItem(Collection lt) {
        this.lineItems = lt;
    }

    public void setStatus(String stat) {
        this.status = stat;
    }
}

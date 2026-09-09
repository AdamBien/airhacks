/*
 * $Id: OrderModel.java,v 1.5.4.6 2001/03/09 07:39:22 gmurray Exp $
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

/**
 * This class represents the model data for an order.
 * It is a value-object and has fine-grained getter
 * methods.
 */
public class OrderModel implements java.io.Serializable {

    public int orderId;
    public Collection lineItems;
    private String billToFirstName;
    private String billToLastName;
    private Address shipToAddr;
    private String shipToFirstName;
    private String shipToLastName;
    private Address billToAddr;
    private CreditCard chargeCard;
    private String carrier;
    private String userId;
    private Calendar orderDate;
    public String status;
    private double totalPrice;
    private Locale locale;


    public OrderModel(int orderId, Collection lineItems,
                      Address shipToAddr, Address billToAddr,
                      String shipToFirstName, String shipToLastName,
                      String billToFirstName, String billToLastName,
                      CreditCard chargeCard, String carrier,
                      String userId, Calendar orderDate,
                      String status, double totalPrice,
                      Locale locale) {
        this.orderId = orderId;
        this.lineItems = lineItems;
        this.shipToAddr = shipToAddr;
        this.billToAddr = billToAddr;
        this.shipToFirstName = shipToFirstName;
        this.shipToLastName = shipToLastName;
        this.billToFirstName = billToFirstName;
        this.billToLastName = billToLastName;
        this.chargeCard = chargeCard;
        this.carrier = carrier;
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.locale = locale;
    }

    /**
     * Class constructor with no arguments, used by the web tier.
     */
    public OrderModel() {}

    /** shallow copy */
    public void copy(OrderModel other) {
        this.orderId = other.orderId;
        this.lineItems = other.lineItems;
        this.shipToAddr = other.shipToAddr;
        this.billToAddr = other.billToAddr;
        this.shipToFirstName = other.shipToFirstName;
        this.shipToLastName = other.shipToLastName;
        this.billToFirstName = other.billToFirstName;
        this.billToLastName = other.billToLastName;
        this.chargeCard = other.chargeCard;
        this.carrier = other.carrier;
        this.userId = other.userId;
        this.orderDate = other.orderDate;
        this.status = other.status;
        this.totalPrice = other.totalPrice;
        this.locale = other.locale;
    }

    //
    // get methods for the instance variables
    //

    public int getOrderId() {
        return orderId;
    }

    public Collection getLineItems() {
        return lineItems;
    }

    public Address getShipToAddr() {
        return shipToAddr;
    }

    public Address getBillToAddr() {
        return billToAddr;
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

    public CreditCard getCreditCard() {
        return chargeCard;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getUserId() {
        return userId;
    }

    public Calendar getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Locale getLocale() {
        return locale;
    }

    public String toString() {
        String ret = null;
        ret = "orderId = " + orderId + "\n";

        Iterator it = lineItems.iterator();
        while ((it != null) && it.hasNext()) {
            LineItem item = (LineItem)it.next();
            ret += item.toString() + "\n";
        }

        ret += "shipToAddr = " + shipToAddr.toString() + "\n";
        ret += "billToAddr = " + billToAddr.toString() + "\n";
        ret += "shipToFirstName = " + shipToFirstName + "\n";
        ret += "shipToLastName = " + shipToLastName + "\n";
        ret += "billToFirstName = " + billToFirstName + "\n";
        ret += "billToLastName = " + billToLastName + "\n";
        ret += " chargeCard = " + chargeCard.toString() + "\n";
        ret += " carrier = " + carrier + "\n";
        ret += "userId = " + userId + "\n";
        ret += "orderDate = " + orderDate.toString() + "\n";
        ret += "status = " + status + "\n";
        ret += "totalPrice = " + totalPrice + "\n";
        ret += "locale = " + locale + "\n";
        return ret;
    }
    public Element toXml(Document doc) {
        Element root = doc.createElement("Order");
        root.setAttribute("Id", String.valueOf(orderId));
        Element node = doc.createElement("UserId");
        node.appendChild(doc.createTextNode(userId));
        root.appendChild(node);
        for (Iterator it = lineItems.iterator(); it.hasNext(); ) {
            root.appendChild(((LineItem)it.next()).toXml(doc));
        }

        root.appendChild(orderDate.toXml(doc, "OrderDate"));

        root.appendChild(chargeCard.toXml(doc, null));

        node = doc.createElement("TotalPrice");
        node.appendChild(doc.createTextNode(String.valueOf(totalPrice)));
        root.appendChild(node);

        root.appendChild(shipToAddr.toXml(doc, "ShippingAddress"));

        root.appendChild(billToAddr.toXml(doc, "BillingAddress"));

        node = doc.createElement("Carrier");
        node.appendChild(doc.createTextNode(carrier));
        root.appendChild(node);

        node = doc.createElement("Status");
        node.appendChild(doc.createTextNode(status));
        root.appendChild(node);

        return root;
    }
}

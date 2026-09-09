/*
 * $Id: OrderHandler.java,v 1.14.4.8 2001/03/15 00:39:58 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 *
 */

package com.sun.j2ee.blueprints.petstore.control.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Locale;

import javax.naming.NamingException;
import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.sun.j2ee.blueprints.util.tracer.Debug;
import com.sun.j2ee.blueprints.petstore.util.JNDIUtil;
import com.sun.j2ee.blueprints.petstore.util.EJBUtil;

import com.sun.j2ee.blueprints.inventory.ejb.InventoryHome;
import com.sun.j2ee.blueprints.inventory.ejb.Inventory;

// order imports

import com.sun.j2ee.blueprints.customer.customer.ejb.Customer;
import com.sun.j2ee.blueprints.customer.customer.ejb.CustomerHome;
import com.sun.j2ee.blueprints.customer.order.model.LineItem;

// cart imports

import com.sun.j2ee.blueprints.shoppingcart.cart.ejb.ShoppingCart;
import com.sun.j2ee.blueprints.shoppingcart.cart.model.ShoppingCartModel;
import com.sun.j2ee.blueprints.shoppingcart.cart.model.CartItem;

// email imports
import com.sun.j2ee.blueprints.mail.ejb.Mailer;

import com.sun.j2ee.blueprints.petstore.control.event.OrderEvent;
import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;

import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.customer.customer.exceptions.CustomerAppException;

public class OrderHandler extends StateHandlerSupport {


  public void perform(EStoreEvent event) throws EStoreEventException {
        try {
            String userName = (String)machine.getAttribute("userName");
            OrderEvent oe = (OrderEvent)event;
            ShoppingCart cart = machine.getShoppingClientControllerEJB().getShoppingCart();
            Locale locale = oe.getLocale();
            InventoryHome inventHome = EJBUtil.getInventoryHome();
            Iterator ci = ((ShoppingCartModel)cart.getDetails(locale)).getItems();
            ArrayList lineItems = new ArrayList();
            int lineNo = 0;
            double total = 0;
            while (ci.hasNext()) {
                lineNo++;
                CartItem cartItem = (CartItem) ci.next();
                LineItem li = new LineItem(cartItem.getItemId(),
                cartItem.getQuantity(),
                cartItem.getUnitCost(), lineNo);
                 lineItems.add(li);
                 total += (cartItem.getUnitCost() * (double) cartItem.getQuantity());
            }

            for (Iterator it = lineItems.iterator(); it.hasNext();){
                LineItem LI = (LineItem)it.next();
                Inventory inventRef = inventHome.findByPrimaryKey(LI.getItemNo());
                inventRef.reduceQuantity(LI.getQty());
            }

            CustomerHome home = EJBUtil.getCustomerHome();
            Customer cust = home.create();
            Debug.println("OrderHandler: created customer " + userName);
            int order = cust.createOrder(userName, lineItems,
                                oe.getShippingAddress(),
                                oe.getBillingAddress(),
                                oe.getShipToFirstName(),
                                oe.getShipToLastName(),
                                oe.getBillToFirstName(),
                                oe.getBillToLastName(),
                                oe.getCreditCard(),
                                oe.getCarrier(),
                                total, locale);
             Debug.println("OrderHandler: created order " + order);
            // put the requestId and the orderId in a table to match up later
            HashMap orderTable = (HashMap)machine.getAttribute("orderTable");
            if (orderTable == null) {
                orderTable = new HashMap();
                machine.setAttribute("orderTable", orderTable);
            }
            orderTable.put(oe.getRequestId() + "", order +"");

            // empty shopping cart
            cart.empty();

            if (JNDIUtil.sendConfirmationMail()) {
                // send order confirmation mail.

                MailAction mAction = new MailAction();
                mAction.sendConfirmationMessage(userName, order, locale);
            }
        } catch (CreateException ce) {
            throw new EStoreAppException("Unable to create order : " + ce.getMessage());
        } catch (FinderException fe) {
            throw new EStoreAppException("Unable find required information while creating an order " + fe.getMessage());
        } catch (CustomerAppException cust) {
            throw new EStoreAppException("Error while processing user input fields. Probably some field was left empty or reload button was hit after an order was commited");
        } catch(NamingException nex) {
            throw new GeneralFailureException("Irrecoverable error while createing order : " + nex);
        } catch (RemoteException ex) {
            throw new GeneralFailureException("Irrecoverable error while createing order : " + ex);
        }
    }
}

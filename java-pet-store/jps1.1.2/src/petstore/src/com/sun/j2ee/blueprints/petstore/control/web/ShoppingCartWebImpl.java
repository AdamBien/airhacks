/*
 * $Id: ShoppingCartWebImpl.java,v 1.12.4.7 2001/03/16 19:56:20 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import java.util.Locale;
import java.rmi.RemoteException;

import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;

import javax.servlet.http.HttpSession;
import com.sun.j2ee.blueprints.petstore.util.JNDINames;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;
import com.sun.j2ee.blueprints.shoppingcart.cart.model.ShoppingCartModel;
import com.sun.j2ee.blueprints.shoppingcart.cart.ejb.ShoppingCart;
import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;
import com.sun.j2ee.blueprints.petstore.control.web.ModelUpdateListener;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class is the web-tier representation of the Shopping Cart.
 */

public class ShoppingCartWebImpl extends ShoppingCartModel
    implements ModelUpdateListener, java.io.Serializable {

    private ModelManager mm;
    private ShoppingCart cartEjb;
    private HttpSession session;

    public ShoppingCartWebImpl() {
        super(null);
   }

    public void init(HttpSession session) {
        // initializing super class with a null list. This means that
        // if because of some bug, this object gets referenced
        // (typically in the JSP page) before performUpdate is called,
        // a null pointer exception will get thrown.
        this.session = session;
        this.mm = (ModelManager)session.getAttribute(WebKeys.ModelManagerKey);
        mm.addListener(JNDINames.CART_EJBHOME, this);
    }


    public void performUpdate() throws EStoreAppException {

        Locale locale = JSPUtil.getLocale(session);
        Debug.println("ShoppingCartWebImple: preformUpdate locale=" + locale);

        // Get data from the EJB
        if (cartEjb == null) {
            cartEjb = mm.getShoppingCartEJB();
        }
        try {
            copy(cartEjb.getDetails(locale));
        } catch (RemoteException re) {
            throw new GeneralFailureException(re.getMessage());
        }
   }

   public String getCartTotal() {
      Locale locale = JSPUtil.getLocale(session);
      double total = super.getTotalCost();
      return JSPUtil.formatCurrency(total, locale);
  }
}









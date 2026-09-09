/*
 * $Id: CartListTag.java,v 1.4.4.1 2001/03/15 00:40:09 brydon Exp $
 * Copyright 1999 Sun Microsystems, Inc. All rights reserved.
 * Copyright 1999 Sun Microsystems, Inc. Tous droits réservés.
 */
package com.sun.j2ee.blueprints.petstore.taglib.list;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import com.sun.j2ee.blueprints.petstore.control.web.ShoppingCartWebImpl;
import com.sun.j2ee.blueprints.shoppingcart.cart.model.CartItem;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;

import com.sun.j2ee.blueprints.util.tracer.Debug;


/*
 * CartListTag
 * -----------
 * Extends the list tag.  Fetches a collection of products from the Cart bean.
 * Should be used in conjunction with CartItemAttributeTags.
 */
public class CartListTag extends ListTag {

  private boolean needsNextForm = false;
  private String cartEmptyString = "Shopping cart Empty";

  protected void initParamPrefix() {
    paramPrefix = "cart_";
  }

  protected Collection findCollection() {
    ShoppingCartWebImpl cartBean =
      (ShoppingCartWebImpl) pageContext.getSession().getAttribute(WebKeys.ShoppingCartModelKey);
    if (cartBean == null) return null;
    Collection cartCollection = cartBean.getCart();
    return(trimCollection(cartCollection));
  }

  protected boolean needsNextForm() {
    return needsNextForm;
  }

  private Collection trimCollection(Collection collection) {
    Iterator iterator = collection.iterator();
    Collection trimmedCollection = new ArrayList();
    int i = 1, endIndex = startIndex + numItems - 1;

    while(iterator.hasNext()) {
      if (i > endIndex) break;
      if (i >= startIndex) trimmedCollection.add(iterator.next());
      else iterator.next();
      i++;
    }
    if (iterator.hasNext()) needsNextForm = true;
    if (trimmedCollection.size() == 0) return null;
    else return trimmedCollection;
  }

  public void setNumItems(String numItemsStr) {
    super.setNumItems(numItemsStr);
  }

  public void setStartIndex(String startIndexStr) {
    super.setNumItems(startIndexStr);
  }

  public void setCartEmptyMessage(String cartEmptyString) {
      this.cartEmptyString = cartEmptyString;
  }

  public int doEndTag() {
    try {
        if (collection == null ||
             ((collection != null) && collection.size() == 0))pageContext.getOut().println(cartEmptyString);
    } catch (java.io.IOException ex) {
        Debug.println("CartListTag caught: " + ex);
    }
    return(EVAL_PAGE);
  }

}






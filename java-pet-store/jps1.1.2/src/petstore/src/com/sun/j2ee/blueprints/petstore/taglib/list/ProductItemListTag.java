/*
 * $Id: ProductItemListTag.java,v 1.1.4.5 2001/03/16 19:56:21 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.taglib.list;

import java.util.Collection;
import java.util.Locale;

import com.sun.j2ee.blueprints.petstore.control.web.ProfileMgrWebImpl;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.ListChunk;
import com.sun.j2ee.blueprints.petstore.control.web.CatalogWebImpl;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/*
 * ProductItemsListTag
 * ---------
 * Extends the list tag.  Fetches a list of products from the ProfileMgr bean.
 * Should be used in conjunction with ProductAttributeTags.
 */
public class ProductItemListTag extends ListTag {

  private boolean hasNext = false;
  private boolean hasPrev = false;
  private String productId = null;
  private String emptyListString = "No Products";

  protected void initParamPrefix() {
    paramPrefix = "productItemList_";
  }

  protected Collection findCollection() {
    CatalogWebImpl catalog =
      (CatalogWebImpl) pageContext.getServletContext().getAttribute(WebKeys.CatalogModelKey);
    if (catalog == null) {
        return null;
    }
    Locale locale = JSPUtil.getLocale(pageContext.getSession());
    ListChunk itemList = null;
    Collection items = null;
    itemList = catalog.getItems(productId, startIndex-1, numItems, locale);
    items = itemList.getCollection();
    if ((startIndex -1 + items.size()) < itemList.getTotalCount()) hasNext = true;
    else hasNext = false;
    return(items);
  }

  protected boolean needsNextForm() {
    return hasNext;
  }

  protected boolean needsPrevForm() {
    return hasPrev;
  }

  // setters (overloaded )
  public void setNumItems(String numItemsStr) {
    super.setNumItems(numItemsStr);
  }

  public void setStartIndex(String startIndexStr) {
    super.setNumItems(startIndexStr);
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public void setEmptyList(String emptyListString) {
    this.emptyListString = emptyListString;
  }

  public int doEndTag() {
    try {
        if (collection == null ||
             ((collection != null) && collection.size() == 0))pageContext.getOut().println(emptyListString);
    } catch (java.io.IOException ex) {
        Debug.println("ProductItemListTag caught: " + ex);
    }
    return(EVAL_PAGE);
  }
}






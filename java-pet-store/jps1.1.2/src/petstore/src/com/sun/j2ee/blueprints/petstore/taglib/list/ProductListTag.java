/** $Id: ProductListTag.java,v 1.9.4.2 2001/03/15 00:40:10 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.taglib.list;

import java.lang.Exception;
import java.util.Collection;
import java.util.Locale;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.ListChunk;
import com.sun.j2ee.blueprints.petstore.control.web.CatalogWebImpl;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;

/*
 * ProductListTag
 * ---------
 * Extends the list tag.  Fetches a list of products for the specified cateogry.
 * Should be used in conjunction with ProductAttributeTags.
 */
public class ProductListTag extends ListTag {

  private String category = null;
  private boolean hasNext = false;

  protected void initParamPrefix() {
    paramPrefix = "productList_" + category + "_";
  }

  protected Collection findCollection() throws Exception {
    CatalogWebImpl catalog =
      (CatalogWebImpl) pageContext.getServletContext().getAttribute(WebKeys.CatalogModelKey);
    if (catalog == null) {
        return null;
    }
    Locale locale = JSPUtil.getLocale(pageContext.getSession());
    ListChunk prodList = null;
    Collection products = null;
    prodList = catalog.getProducts(this.category, startIndex-1, numItems, locale);
    products = prodList.getCollection();
    if ((startIndex -1 + products.size()) < prodList.getTotalCount()) hasNext = true;
    else hasNext = false;
    return(products);
  }

  protected boolean needsNextForm() {
    return hasNext;
  }

  public void setCategory(String category) {
      this.category = category;
  }

  // setters (overloaded to fix bug in tomcat)
  public void setNumItems(String numItemsStr) {
    super.setNumItems(numItemsStr);
  }

  public void setStartIndex(String startIndexStr) {
    super.setNumItems(startIndexStr);
  }
}






/** $Id: SearchListTag.java,v 1.1.4.3 2001/03/15 00:40:10 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.taglib.list;

import java.lang.Exception;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Locale;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.ListChunk;
import com.sun.j2ee.blueprints.petstore.control.web.CatalogWebImpl;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;
import com.sun.j2ee.blueprints.util.tracer.Debug;

/*
 * SearchListTag
 * ---------
 * Extends the list tag.  Fetches a list of products for the specified category.
 * Should be used in conjunction with ProductAttributeTags.
 */
public class SearchListTag extends ListTag {

  private String searchString = null;
  private String emptyListString = "Search resulted in no products";
  private boolean hasNext = false;

  protected void initParamPrefix() {
    paramPrefix = "searchList_";
  }

  protected Collection findCollection() throws Exception {
    CatalogWebImpl catalog =
      (CatalogWebImpl) pageContext.getServletContext().getAttribute(WebKeys.CatalogModelKey);
    if (catalog == null) {
        return null;
    }
    if (searchString == null) return null;
    Collection keywords = JSPUtil.parseKeywords(searchString);
    if (searchString.trim().equals("") || keywords == null) return null;
    Locale locale = JSPUtil.getLocale(pageContext.getSession());
    ListChunk prodList = null;
    Collection products = null;
    prodList = catalog.searchProducts(keywords, startIndex-1, numItems, locale);
    products = prodList.getCollection();
    if (((startIndex -1 + products.size()) < prodList.getTotalCount()) &&
       products.size() >= numItems) hasNext = true;
    else hasNext = false;
    return(products);
  }

  protected boolean needsNextForm() {
    return hasNext;
  }

  public void setSearchText(String searchString) {
      this.searchString = searchString;
  }

  public void setEmptyList(String emptyListString) {
      this.emptyListString = emptyListString;
  }

  // setters (overloaded to fix bug in tomcat)
  public void setNumItems(String numItemsStr) {
    super.setNumItems(numItemsStr);
  }

  public void setStartIndex(String startIndexStr) {
    super.setNumItems(startIndexStr);
  }

  public int doEndTag() {
    try {
        if (collection == null ||
             ((collection != null) && collection.size() == 0))pageContext.getOut().println(emptyListString);
    } catch (java.io.IOException ex) {
        Debug.println("SearchListTag caught: " + ex);
    }
    return(EVAL_PAGE);
  }
}






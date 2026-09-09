/*
 * $Id: MyListTag.java,v 1.9.4.4 2001/03/15 00:40:09 brydon Exp $
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
import com.sun.j2ee.blueprints.petstore.control.web.CustomerWebImpl;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;

/*
 * MyListTag
 * ---------
 * Extends the list tag.  Fetches a list of products from the ProfileMgr bean.
 * Should be used in conjunction with ProductAttributeTags.
 */
public class MyListTag extends ListTag {

  private boolean hasNext = false;
  private boolean hasPrev = false;

  protected void initParamPrefix() {
    paramPrefix = "myList_";
  }

  protected Collection findCollection() {
    CustomerWebImpl customerBean =
      (CustomerWebImpl) pageContext.getSession().getAttribute(WebKeys.CustomerWebImplKey);
    if (customerBean == null) return null;
    if (!customerBean.isLoggedIn()) return null;
    ProfileMgrWebImpl profileMgrBean =
      (ProfileMgrWebImpl) pageContext.getSession().getAttribute(WebKeys.ProfileMgrModelKey);
    if (profileMgrBean == null) return null;
    CatalogWebImpl catalog =
      (CatalogWebImpl) pageContext.getServletContext().getAttribute(WebKeys.CatalogModelKey);
    if (catalog == null) {
        return null;
    }
    ExplicitInformation eInfo = profileMgrBean.getExplicitInformation();
    String favCategory = eInfo.getFavCategory();

    if (eInfo == null) return null;
    if (!eInfo.getMyListOpt()) return null;
    Locale locale = JSPUtil.getLocale(pageContext.getSession());
    ListChunk prodList = null;
    Collection products = null;
    prodList = catalog.getProducts(favCategory.toUpperCase(), startIndex-1, numItems, locale);
    products = prodList.getCollection();
    if ((startIndex -1 + products.size()) < prodList.getTotalCount()) hasNext = true;
    else hasNext = false;
    return(products);
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
}






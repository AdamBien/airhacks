/** $Id: ProductItemAttributeTag.java,v 1.1.4.2 2001/03/15 00:40:09 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.taglib.list;

import java.util.Locale;

import com.sun.j2ee.blueprints.petstore.control.web.CatalogWebImpl;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;

/*
 * ProductItemAttributeTag
 * -------------------
 * Extension of ItemAttributeTag that handles a Product object.  It prints out
 * the name, id, or description of the product, depending on the attribute
 * specified, with "name" as the default.
 */
public class ProductItemAttributeTag extends ItemAttributeTag {

  protected String createText() {
    CatalogWebImpl catalog =
      (CatalogWebImpl) pageContext.getServletContext().getAttribute(WebKeys.CatalogModelKey);
    if (catalog == null) {
        return null;
    }
    Item productItem = (Item) item;
    if (productItem == null) return null;
    Product product = null;
    Locale locale = JSPUtil.getLocale(pageContext.getSession());
    product = catalog.getProduct(productItem.getProductId(), locale);
    if (product == null) return null;
    if ((attribute == null) ||
        (attribute.equalsIgnoreCase("name"))) {
      return (product.getName());
    } else if (attribute.equalsIgnoreCase("productattribute")) {
      return (productItem.getAttribute());
    } else if (attribute.equalsIgnoreCase("productid")) {
      return (product.getId());
    } else if (attribute.equalsIgnoreCase("id")) {
      return (productItem.getItemId());
    } else if (attribute.equalsIgnoreCase("unitcost")) {
      return JSPUtil.formatCurrency(productItem.getUnitCost(), locale);
    } else if (attribute.equalsIgnoreCase("listcost")) {
      return JSPUtil.formatCurrency(productItem.getListCost(), locale);
    } else if (attribute.equalsIgnoreCase("description")) {
      return (product.getDescription());
    } else return(null);
  }
}

/*
 * $Id: ProductDetailsAttributeTag.java,v 1.3.4.3 2001/03/15 00:40:08 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.taglib;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import java.util.Locale;

import com.sun.j2ee.blueprints.petstore.util.JSPUtil;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product;
import com.sun.j2ee.blueprints.petstore.control.web.InventoryWebImpl;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;

import com.sun.j2ee.blueprints.util.tracer.Debug;


/*
 * ProductDetailAttributeTag
 * -------------------------
 */

public class ProductDetailsAttributeTag extends TagSupport {

    protected Product curProduct = null;
    protected Item curItem = null;
    protected InventoryWebImpl inventory = null;
    protected String attribute = null;

    public int doStartTag() throws JspTagException {

        ProductDetailsTag prod = (ProductDetailsTag)
                        findAncestorWithClass(this, ProductDetailsTag.class);
        if (prod == null) {
            throw new JspTagException("ProdDetAttrTag: ProdDetTag tag not" +
                        "found");
        }

                inventory = (InventoryWebImpl)
                pageContext.getServletContext().getAttribute(WebKeys.InventoryModelKey);
                if (inventory == null) {
                        throw new JspTagException("ProdDetAttrTag : inventory is null");
                }

        curProduct = (Product)prod.getCurrentProduct();
        curItem = (Item)prod.getCurrentItem();

                if(curProduct == null)
            throw new JspTagException("ProdDetAttrTag: NULL curProd returned");
                if(curItem == null)
            throw new JspTagException("ProdDetAttrTag: NULL curItem returned");

        try {
            JspWriter out = pageContext.getOut();
            out.print(sendDetails());
        } catch(IOException ioe) {
            Debug.println("ProductDetailAttributeTag: " +
                                                        "Error printing attribute: " + ioe);
        }
        return(SKIP_BODY);
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

        public int doEndTag() {
                return(EVAL_PAGE);
        }

    protected String sendDetails() {
        if(attribute.equalsIgnoreCase("ItemAttribute"))
            return(curItem.getAttribute());
                else if(attribute.equalsIgnoreCase("ProdName"))
                        return(curProduct.getName());
                else if(attribute.equalsIgnoreCase("Currency")) {
                        Locale locale = JSPUtil.getLocale(pageContext.getSession());
                        return(JSPUtil.formatCurrency(curItem.getListCost(), locale));
                } else if(attribute.equalsIgnoreCase("Inventory")) {
                        if (inventory.getInventory(curItem.getItemId()) > 0) {
                                return(inventory.getInventory(curItem.getItemId())+" in stock");
                        }
                        else {
                                return("<font color=\"red\">Back Ordered</font>");
                        }
                }
                else if(attribute.equalsIgnoreCase("ItemId"))
                        return(curItem.getItemId());
                else if(attribute.equalsIgnoreCase("ProdDesc"))
                        return(curProduct.getDescription());
                else
                        return(null);
    }
}

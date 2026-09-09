/*
 * $Id: ProductDetailsTag.java,v 1.2.4.3 2001/03/15 00:40:08 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits r?erv?.
 */

package com.sun.j2ee.blueprints.petstore.taglib;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import java.util.Locale;

import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Item;
import com.sun.j2ee.blueprints.shoppingcart.catalog.model.Product;
import com.sun.j2ee.blueprints.petstore.control.web.CatalogWebImpl;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/*
 * ProductDetailsTag
 * -----------------
 */

public class ProductDetailsTag extends BodyTagSupport {
        private Item item = null;
        private Product product = null;

        public int doStartTag() throws JspTagException {

                CatalogWebImpl catalog = (CatalogWebImpl)
                pageContext.getServletContext().getAttribute(WebKeys.CatalogModelKey);
                if (catalog == null) {
                        throw new JspTagException("ProductDetailsTag : catalog is null");
                }
                Locale locale = JSPUtil.getLocale(pageContext.getSession());
                try {
                        item = catalog.getItem(
                                pageContext.getRequest().getParameter("item_id"), locale);
                        if(item == null)
                                throw new JspTagException("ProductDetailsTag : item is null");
                        product = catalog.getProduct(item.getProductId(), locale);
                        if(product == null)
                                throw new JspTagException("ProductDetailsTag:product is null");
                } catch (Exception e) {
                        throw new JspTagException("Exception while getting product " + e);
                }

                return(EVAL_BODY_TAG);
        }

        public int doEndTag() {
                try {
                        BodyContent body = getBodyContent();
                        if (body != null) {
                                JspWriter out = body.getEnclosingWriter();
                                out.print(body.getString());
                        }
                } catch(IOException ioe) {
                        Debug.println("Error handling items tag: " + ioe);
                }
                return(SKIP_BODY);
        }

        public Object getCurrentItem() {
                return (Object)item;
        }

        public Object getCurrentProduct() {
                return (Object)product;
        }
}

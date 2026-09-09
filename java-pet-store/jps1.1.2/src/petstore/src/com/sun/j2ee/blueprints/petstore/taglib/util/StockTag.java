/*
 * $Id: StockTag.java,v 1.2.4.1 2001/03/12 21:25:00 lblair Exp $
 * Copyright 1999 Sun Microsystems, Inc. All rights reserved.
 * Copyright 1999 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.taglib.util;

import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.JspTagException;

import com.sun.j2ee.blueprints.petstore.control.web.InventoryWebImpl;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/*
 * StockTag
 * -----------
 * Verifies if item quantity of items in stock
 */
public class StockTag extends TagSupport {

    private String itemId;
    private int quantity;
    private String trueCase = "true";
    private String falseCase = "false";

   public void setItemId(String itemId) {
       Debug.println("StockTag: Itemid=" + itemId);
       this.itemId = itemId;
   }

    public void setTrue(String trueCase) {
        this.trueCase = trueCase;
    }

    public void setFalse(String falseCase) {
        this.falseCase = falseCase;
    }

   public void setQuantity(String quantity) throws JspTagException {
       Debug.println("StockTag: quantity=" + quantity);
       try {
           this.quantity = Integer.parseInt(quantity);
       } catch (java.lang.NumberFormatException nx) {
           throw new JspTagException("StockTag: invalid quantity: " + quantity);
       }
   }

   public int doStartTag() {
       return SKIP_BODY;
   }

   public int doEndTag() throws JspTagException {
       try {
           InventoryWebImpl inventory = (InventoryWebImpl)pageContext.getServletContext().getAttribute(WebKeys.InventoryModelKey);
           if (inventory.getInventory(itemId) >= quantity) {
               pageContext.getOut().print(trueCase);
           } else {
               pageContext.getOut().print(falseCase);
           }
        } catch (java.io.IOException iox) {
           throw new JspTagException("StockTag: caught io exception");
        } catch (java.lang.NullPointerException nx) {
           throw new JspTagException("StockTag: unable to access inventory");
        }
        return EVAL_PAGE;
    }
}






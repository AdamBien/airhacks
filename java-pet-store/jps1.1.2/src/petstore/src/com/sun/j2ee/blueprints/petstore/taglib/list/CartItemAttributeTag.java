package com.sun.j2ee.blueprints.petstore.taglib.list;

import java.util.Locale;

import javax.servlet.jsp.JspTagException;

import com.sun.j2ee.blueprints.shoppingcart.cart.model.CartItem;
import com.sun.j2ee.blueprints.petstore.control.web.InventoryWebImpl;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;
import com.sun.j2ee.blueprints.petstore.util.JSPUtil;

import com.sun.j2ee.blueprints.util.tracer.Debug;


/*
 * CartItemAttributeTag
 * --------------------
 * Extension of ItemAttributeTag that handles a CartItem object.  It prints out
 * the name, itemid, productid, attribute, unit cost, total cost or quantity
 * of the cart item, depending on the attribute specified, with
 * "name" as the default.
 */
public class CartItemAttributeTag extends ItemAttributeTag {

    private String trueCase = "true";
    private String falseCase = "false";

  protected String createText() {
    CartItem cartItem = (CartItem) item;

    if ((attribute == null) ||
        (attribute.equalsIgnoreCase("name"))) {
      return (cartItem.getName());
    } else if (attribute.equalsIgnoreCase("itemid")) {
      return (cartItem.getItemId());
    } else if (attribute.equalsIgnoreCase("productid")) {
      return (cartItem.getProductId());
    } else if (attribute.equalsIgnoreCase("attribute")) {
      return (cartItem.getAttribute());
    } else if (attribute.equalsIgnoreCase("quantity")) {
      return (Integer.toString(cartItem.getQuantity()));
    } else if (attribute.equalsIgnoreCase("unitcost")) {
      Locale locale = JSPUtil.getLocale(pageContext.getSession());
      return JSPUtil.formatCurrency(cartItem.getUnitCost(), locale);
    } else if (attribute.equalsIgnoreCase("totalcost")) {
      Locale locale = JSPUtil.getLocale(pageContext.getSession());
      return JSPUtil.formatCurrency(cartItem.getTotalCost(), locale);
    } else if (attribute.equalsIgnoreCase("itemTotal")) {
      Locale locale = JSPUtil.getLocale(pageContext.getSession());
      double total = cartItem.getQuantity() * cartItem.getUnitCost();
      return JSPUtil.formatCurrency(total, locale);
    } else if (attribute.equalsIgnoreCase("instock")) {
      return getInStock();
    } else return(null);
  }

    public void setTrue(String trueCase) {
        this.trueCase = trueCase;
    }

    public void setFalse(String falseCase) {
        this.falseCase = falseCase;
    }

   private String getInStock(){
       CartItem cartItem = (CartItem)item;
       int quantity = cartItem.getQuantity();
       try {
           InventoryWebImpl inventory = (InventoryWebImpl)pageContext.getServletContext().getAttribute(WebKeys.InventoryModelKey);
           if (inventory.getInventory(cartItem.getItemId()) >= quantity) {
               return trueCase;
           } else {
               return falseCase;
           }
        } catch (java.lang.NullPointerException nx) {
           Debug.println("CartArrtibuteTag: unable to access inventory");
        }
        return falseCase;
    }
}


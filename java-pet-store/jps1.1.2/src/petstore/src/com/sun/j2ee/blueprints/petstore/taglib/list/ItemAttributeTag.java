package com.sun.j2ee.blueprints.petstore.taglib.list;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import com.sun.j2ee.blueprints.util.tracer.Debug;

/*
 * ItemAttributeTag
 * ----------------
 * This abstract class works with the list tag package and prints out an
 * attribute of a particular type of item.  The item is retrieved from the
 * outer items tag (an exception is thrown if not inside one).  Depending on
 * the value of the "attribute" attribute a different string is printed - this
 * should be determined in the implementation of createText(), which allows
 * for the details of the class of the item.
 */
public abstract class ItemAttributeTag extends TagSupport {
  protected Object item = null;
  protected String attribute = null;

  public int doStartTag() throws JspTagException {
    // check if itemAttribute tag is in items tag
    ItemsTag itemsTag = (ItemsTag) findAncestorWithClass(this, ItemsTag.class);
    if (itemsTag == null) {
      throw new JspTagException("ItemAttributeTag: itemsAttribute tag not" +
                                "inside items tag");
    }
    item = itemsTag.getCurrentItem();

    // print out attribute
    try {
      JspWriter out = pageContext.getOut();
      out.print(createText());
    } catch(IOException ioe) {
      Debug.println("ItemAttributeTag: Error printing attribute: " + ioe);
    }

    // there should be no body to process
    return(SKIP_BODY);
  }

  // setter for tag attribute
  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  // protected abstract methods
  protected abstract String createText();
}




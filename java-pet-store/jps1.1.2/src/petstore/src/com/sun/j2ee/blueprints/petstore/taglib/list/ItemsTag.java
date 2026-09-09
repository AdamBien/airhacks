package com.sun.j2ee.blueprints.petstore.taglib.list;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.util.Iterator;
import java.io.IOException;
import com.sun.j2ee.blueprints.util.tracer.Debug;

/*
 * ItemsTag
 * --------
 * After getting the iterator from the outer ListTag (an exception is thrown
 * if not inside one), maintains the current item, for reference by the inner
 * item attribute tags, then processes the body once for every item in the
 * iterator.
 */
public class ItemsTag extends BodyTagSupport {
  private Iterator iterator = null;
  private Object item = null;

  public int doStartTag() throws JspTagException {
    // check if items tag is in list tag
    ListTag listTag = (ListTag) findAncestorWithClass(this, ListTag.class);
    if (listTag == null) {
      throw new JspTagException("ItemsTag: items tag not inside items tag");
    }
    iterator = listTag.getIterator();
    if (iterator == null || !iterator.hasNext()) return(SKIP_BODY);
    item = iterator.next();
    return(EVAL_BODY_TAG);
  }

  // process the body again with the next item if it exists
  public int doAfterBody() {
    if (iterator.hasNext()) {
      item = iterator.next();
      return(EVAL_BODY_TAG);
    } else return(SKIP_BODY);
  }

  // print out the resulting body content to the JSP page and evaluate the
  // rest of the page
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
    return(EVAL_PAGE);
  }

  // getter for inner tags
  public Object getCurrentItem() {
    return item;
  }
}


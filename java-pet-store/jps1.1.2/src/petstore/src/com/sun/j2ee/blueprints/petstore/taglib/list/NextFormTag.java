/*
 * $Id: NextFormTag.java,v 1.2.4.2 2001/03/15 00:40:09 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.taglib.list;

import javax.servlet.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.sun.j2ee.blueprints.util.tracer.Debug;


/*
 * NextFormTag
 * -----------
 * A tag that should be nested inside a list, this just prints out a form
 * with hidden inputs that will be stored in the session object by the
 * request handler for reference by the list tag handler the next time around.
 * The value of the startIndex parameter is incremented by numItems so the
 * next bunch of items will be retrieved for display in the list.
 */
public class NextFormTag extends TagSupport {
  private String action = null;
  private ListTag listTag = null;

  public int doStartTag() throws JspTagException {

    // check if inside list tag
    listTag = (ListTag) findAncestorWithClass(this, ListTag.class);
    if (listTag == null) {
      throw new JspTagException("NextFormTag: nextForm tag not inside list" +
                                "tag");
    }

    // assume servlet request is http servlet request
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

    // if no next form is needed, don't process the body
    if (!listTag.hasNextForm()) return (SKIP_BODY);

    // print out <form> tag and hidden input for startIndex
    try {
      JspWriter out = pageContext.getOut();
      out.print("<form");
      out.print("  method=\"" + request.getMethod() + "\"");
      out.println("  action=\"" + action + "\">");
      out.println("  <input type=\"hidden\" name=\"" +
                  listTag.getParamPrefix() + listTag.getStartIndexParam() +
                  "\" value=\"" +
                  (listTag.getStartIndex() + listTag.getNumItems()) + "\">");
      out.print("  <input type=\"hidden\" name=\"" +
                listTag.getParamPrefix() + listTag.getNextParam());
      out.println("\" value=\"true\">");
    } catch(IOException ioe) {
      Debug.println("NextFormTag: error printing <form> tag");
    }
    return(EVAL_BODY_INCLUDE);
  }

  public int doEndTag() {
    // if next form has been printed out, print out </form> tag
    if (listTag.hasNextForm()) {
      try {
        JspWriter out = pageContext.getOut();
        out.print("</form>");
      } catch(IOException ioe) {
        Debug.println("NextFormTag: error printing <form> tag");
      }
    }
    return(EVAL_PAGE);
  }

  // setter for tag attribute
  public void setAction(String action) {
    this.action = action;
  }
}

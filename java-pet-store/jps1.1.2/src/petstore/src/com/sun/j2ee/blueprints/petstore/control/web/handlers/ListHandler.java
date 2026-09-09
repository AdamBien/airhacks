/*
 * $Id: ListHandler.java,v 1.1.4.1 2001/03/15 00:40:07 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web.handlers;

import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/*
 * ListHandler
 * -----------
 * This handler takes all the parameters and their values in the request
 * object and sets them as attributes in the session object for use by
 * the list tag handlers.
 */
public class ListHandler extends RequestHandlerSupport {

  public EStoreEvent processRequest(HttpServletRequest request) {
    Enumeration paramNames = request.getParameterNames();
    HttpSession session = request.getSession();
    while (paramNames.hasMoreElements()) {
      String paramName = (String) paramNames.nextElement();
      session.setAttribute(paramName, request.getParameter(paramName));
    }
    return null;
  }
}










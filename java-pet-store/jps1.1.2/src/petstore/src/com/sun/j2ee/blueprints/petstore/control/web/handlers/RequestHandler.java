/*
 * $Id: RequestHandler.java,v 1.1.4.4 2001/03/15 00:40:07 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web.handlers;

import com.sun.j2ee.blueprints.petstore.control.event.EStoreEvent;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreEventException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;

/**
 * This class is the base interface to request handlers on the
 * web tier.
 *
*/
public interface RequestHandler extends java.io.Serializable {

    public void setServletContext(ServletContext context);
    public void doStart(HttpServletRequest request);
    public EStoreEvent processRequest(HttpServletRequest request) throws EStoreEventException;
    public void doEnd(HttpServletRequest request);
}

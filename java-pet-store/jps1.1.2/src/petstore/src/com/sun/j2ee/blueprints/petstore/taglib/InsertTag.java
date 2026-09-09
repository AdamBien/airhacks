/*
 * $Id: InsertTag.java,v 1.11.4.1 2001/03/15 00:40:08 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.taglib;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.sun.j2ee.blueprints.petstore.control.web.Parameter;
import com.sun.j2ee.blueprints.petstore.control.web.ScreenFlowManager;
import com.sun.j2ee.blueprints.petstore.util.WebKeys;

import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class is an easy interface to the JSP template or other
 * text that needs to be inserted.
 */

public class InsertTag extends TagSupport {

    private boolean directInclude = false;
    private String parameter = null;
    private Parameter parameterRef = null;
    private ScreenFlowManager screenManager;

    /**
     * default constructor
     */
    public InsertTag() {
        super();
    }

    public void setParameter(String parameter){
        this.parameter = parameter;
    }

    public int doStartTag() throws JspTagException {
         try{
             pageContext.getOut().flush();
         } catch (Exception e){
             // do nothing
         }
        // load the ScreenFlowManager
        try{
                screenManager = (ScreenFlowManager)pageContext.getServletContext().getAttribute(WebKeys.ScreenManagerKey);
        } catch (NullPointerException e){
            throw new JspTagException("Error extracting screenManager from session: " + e);
        }
        if ((screenManager != null) && (parameter != null)) {
            parameterRef = (Parameter)screenManager.getParameter(parameter,pageContext.getSession());
        } else {
            Debug.println("InsertTag: screenManager is null");
        }
        if (parameterRef != null) directInclude = parameterRef.isDirect();
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException {
        try {
            if (directInclude && parameterRef != null) {
                pageContext.getOut().print(parameterRef.getValue());
            } else if (parameterRef != null)  {
                if (parameterRef.getValue() != null) pageContext.getRequest().getRequestDispatcher(parameterRef.getValue()).include(pageContext.getRequest(), pageContext.getResponse());
            }
         } catch (Exception ex) {
             Debug.println("InsertTag:doEndTag caught: " + ex);
        }
        return EVAL_PAGE;
    }
}

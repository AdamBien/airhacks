/*
 * $Id: MissingFormDataException.java,v 1.4.4.2 2001/03/15 00:40:03 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import java.util.Collection;

/**
 * This exception is thrown by the RequestToEventTranslator
 * when a user fails to provide enough form information. This
 * excption contains list of form fields needed. This exception
 * is used by a JSP page to generate an error page.
 */
public class MissingFormDataException extends Exception implements java.io.Serializable {

    private Collection missingFields;
    private String message;

    public MissingFormDataException(String message, Collection missingFields) {
        this.message = message;
        this.missingFields = missingFields;
    }

    public Collection getMissingFields() {
        return missingFields;
    }

    public String getMessage() {
        return message;
    }

}

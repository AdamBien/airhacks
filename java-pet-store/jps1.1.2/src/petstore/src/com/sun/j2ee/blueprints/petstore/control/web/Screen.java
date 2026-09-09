/*
 * $Id: Screen.java,v 1.3.4.2 2001/03/15 00:40:04 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import java.util.HashMap;

public class Screen implements java.io.Serializable {

    private String name;
    private HashMap parameters;

    public Screen(String name, HashMap parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public HashMap getParameters() {
        return parameters;
    }

    public Parameter getParameter(String key) {
        return (Parameter)parameters.get(key);
    }

    public String toString() {
        return "[Screen: name=" + name + ", parameters=" + parameters + "]";
    }
}


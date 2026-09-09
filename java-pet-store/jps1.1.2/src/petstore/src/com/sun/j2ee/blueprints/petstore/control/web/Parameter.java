/*
 * $Id: Parameter.java,v 1.3.4.2 2001/03/15 00:40:04 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import java.util.HashMap;

public class Parameter implements java.io.Serializable {

    private String key;
    private String value;
    private boolean direct;

    public Parameter(String key, String value, boolean direct) {
        this.key = key;
        this.value = value;
        this.direct = direct;
    }

    public boolean isDirect() {
        return direct;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "[Parameter: key=" + key + ", value=" + value + ", direct="+ direct + "]";
    }
}


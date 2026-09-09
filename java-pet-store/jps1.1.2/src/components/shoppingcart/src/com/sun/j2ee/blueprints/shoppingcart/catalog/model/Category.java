/*
 * $Id: Category.java,v 1.3.4.2 2001/03/08 22:50:01 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.catalog.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.io.Serializable;

/**
 * This class represents different categories of pets in the Java
 * Pet Store Demo.  Each category can have one or more products under
 * it and each product in turn can have one or more inventory items
 * under it.  For example, the Java Pet Store Demo currently has five
 * categories: birds, cats, dogs, fish, and reptiles.
*/
public class Category implements java.io.Serializable {

    private String id;
    private String name;
    private String description;

    public Category(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Class constructor with no arguments, used by the web tier.
     */
    public Category() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

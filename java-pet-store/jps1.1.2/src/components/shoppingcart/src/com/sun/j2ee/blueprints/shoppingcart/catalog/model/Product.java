/*
 * $Id: Product.java,v 1.3.4.2 2001/03/08 22:50:02 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.shoppingcart.catalog.model;

/**
 * This class represents different kinds of pets for a particular category.
 * For example, in the Java Pet Store Demo, the  category for 'BIRDS'
 * could have two products: 'Amazon Parrot' and 'Finch'.
*/
public class Product implements java.io.Serializable {

    private String id;
    private String name;
    private String description;

    public Product(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Class constructor with no arguments, used by the web tier.
     */
    public Product() {}

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

/*
 * $Id: JNDINames.java,v 1.8.4.4 2001/03/15 00:40:11 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.util;

/**
 * This class is the central location to store the internal
 * JNDI names of various entities. Any change here should
 * also be reflected in the deployment descriptors.
 */
public interface JNDINames {

    //
    // JNDI names of EJB home objects
    //
    public static final String CATALOG_EJBHOME =
        "java:comp/env/ejb/catalog/Catalog";

    /** JNDI name of the home interface of ShoppingClientController EJB */
    public static final String SCC_EJBHOME =
        "java:comp/env/ejb/scc/Scc";

    /** JNDI name of the home interface of ShoppingCart */
    public static final String CART_EJBHOME =
        "java:comp/env/ejb/cart/Cart";

    public static final String PROFILEMGR_EJBHOME =
        "java:comp/env/ejb/profilemgr/ProfileMgr";

    public static final String CUSTOMER_EJBHOME =
        "java:comp/env/ejb/customer/Customer";

    public static final String ORDER_EJBHOME =
        "java:comp/env/ejb/order/Order";

    public static final String INVENTORY_EJBHOME =
        "java:comp/env/ejb/inventory/Inventory";

    public static final String MAILER_EJBHOME =
        "java:comp/env/ejb/mail/Mailer";

    public static final String SIGNON_EJBHOME =
        "java:comp/env/ejb/signon/Signon";

    //
    // JNDI Names of data sources.
    //
    public static final String INVENTORY_DATASOURCE =
        "java:comp/env/jdbc/InventoryDataSource";

    public static final String ESTORE_DATASOURCE =
        "java:comp/env/jdbc/EstoreDataSource";

    //
    // JNDI Names of other resources.
    //
    public static final String MAIL_SESSION =
        "java:comp/env/mail/MailSession";

    //
    // JNDI Names of application properties.
    //
    //public static final String SECURITY_ADAPTER_CLASSNAME =
    //  "java:comp/env/securityAdapterClassName";

    public static final String SERVER_TYPE =
        "java:comp/env/server/ServerType";

    public static final String SEND_CONFIRMATION_MAIL =
        "java:comp/env/ejb/mail/SendConfirmationMail";
}

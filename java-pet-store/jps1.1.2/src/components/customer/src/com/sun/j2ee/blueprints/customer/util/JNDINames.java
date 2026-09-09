/*
 * $Id: JNDINames.java,v 1.7.4.4 2001/03/15 00:47:25 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.util;

/**
 * This class is the central location to store the internal
 * JNDI names of various entities. Any change here should
 * also be reflected in the deployment descriptors.
 */
public interface JNDINames {

    //
    // JNDI names of EJB home objects
    //
    public static final String ACCOUNT_EJBHOME =
        "java:comp/env/ejb/account/Account";

    public static final String ORDER_EJBHOME =
        "java:comp/env/ejb/order/Order";

    //
    // JNDI Names of data sources.
    //
    public static final String ESTORE_DATASOURCE =
        "java:comp/env/jdbc/EstoreDataSource";

    public static final String ORDER_DAO_CLASS =
        "java:comp/env/ejb/order/OrderDAOClass";

    public static final String ACCOUNT_DAO_CLASS =
        "java:comp/env/ejb/account/AccountDAOClass";

}

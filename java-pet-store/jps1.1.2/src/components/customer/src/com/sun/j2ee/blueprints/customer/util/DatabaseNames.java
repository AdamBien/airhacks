package com.sun.j2ee.blueprints.customer.util;

/**
 * This interface stores the name of all the database tables.
 * The String constants in this class should be used by other
 * classes instead of hardcoding the name of a database table
 * into the source code.
 */
public interface DatabaseNames {

    public static final String ACCOUNT_TABLE   = "account";
    public static final String ORDER_TABLE = "orders";
    public static final String ORDER_STATUS_TABLE = "orderstatus";
    public static final String LINE_ITEM_TABLE = "lineitem";
    public static final int MAX_USERID_LENGTH = 25;
    public static final int MAX_PASSWD_LENGTH = 25;
}

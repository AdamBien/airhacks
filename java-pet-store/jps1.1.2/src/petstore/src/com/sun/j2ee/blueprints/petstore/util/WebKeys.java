package com.sun.j2ee.blueprints.petstore.util;

/**
 * This interface contains all the keys that are used to
 * store data in the different scopes of web-tier. These
 * values are the same as those used in the JSP
 * pages (useBean tags).
 */
public interface WebKeys {

    public static final String CatalogModelKey = "catalog";
    public static final String AccountModelKey = "account";
    public static final String ModelManagerKey = "mm";
    public static final String ScreenManagerKey = "screenManager";
    public static final String RequestProcessorKey = "rp";
    public static final String ProfileMgrModelKey = "profilemgr";
    public static final String InventoryModelKey = "inventory";
    public static final String ShoppingCartModelKey = "cart";
    public static final String WebControllerKey = "webController";
    public static final String CurrentScreen = "currentScreen";
    public static final String PreviousScreen = "previousScreen";
    public static final String LanguageKey = "language";
    public static final String URLMappingsKey = "urlMappings";
    public static final String CustomerWebImplKey = "customer";
    public static final String MissingFormDataKey = "missingFormData";
    public static final String SigninTargetURL = "signinTargetURL";
    public static final String ServerTypeKey = "serverType";
    /**
     * The contact information corresponding to the shipping
     *  address of the web user.
     */
    public static final String ShippingContactInfoKey =
        "shippingContactInfo";

    /**
     * The contact information corresponding to the billing
     * address of the web user.
     */
    public static final String BillingContactInfoKey = "billingContactInfo";

    /**
     * The preferred carrier for the web user.
     */
    public static final String CarrierKey = "carrier";

    /**
     * The key to get credit card information for a web user.
     */
    public static final String CreditCardKey = "creditcard";

    /**
     * This attribute describes if the user needs to enter
     * shipping address or not.
     */
    public static final String ShippingAddressRequiredKey =
        "shippingAddressRequired";

    /**
     * This request-scoped attribute uniquely determines the
     * order associated with a user request.
     */
    public static final String RequestIdKey = "requestId";
}

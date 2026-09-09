/*
 * $Id: I18nUtil.java,v 1.1.2.2 2001/03/15 00:47:24 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.customer.util;

import java.util.Locale;

/**
 * This is a utility class Internationalization support.
 */
public final class I18nUtil {

    public static Locale getLocale(String localeString) {
        int separatorIndex = localeString.indexOf("_");
        String country = localeString.substring(0,separatorIndex);
        String language = localeString.substring(separatorIndex + 1, localeString.length());
        return new Locale(language, country);
    }

    public static String getLocaleString(Locale locale) {
        return locale.getCountry() + "_" + locale.getLanguage();
    }
}

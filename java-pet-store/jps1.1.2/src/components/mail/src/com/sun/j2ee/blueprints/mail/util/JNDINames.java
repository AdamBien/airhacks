/*
 * $Id: JNDINames.java,v 1.4.4.4 2001/03/14 23:28:33 lblair Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.mail.util;

/**
 * This class is the central location to store the internal
 * JNDI names of various entities. Any change here should
 * also be reflected in the deployment descriptors.
 */
public interface JNDINames {

    //
    // JNDI Names of other resources.
    //
    public static final String MAIL_SESSION =
        "java:comp/env/mail/MailSession";

}

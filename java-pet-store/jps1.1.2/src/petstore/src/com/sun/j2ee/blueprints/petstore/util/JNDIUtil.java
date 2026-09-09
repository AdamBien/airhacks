/*
 * $Id: JNDIUtil.java,v 1.5.4.1 2001/03/15 00:40:11 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.sun.j2ee.blueprints.util.tracer.Debug;

/**
 * This class implements convenience methods to access JNDI
 * entries. It is typically used to access application
 * properties that were configured through deployment
 * descriptor and hence are accessible through JNDI namespace.
 */
public final class JNDIUtil implements JNDINames {

    /**
     * a convenience method to get the boolean value corresponding
     * to the SEND_CONFIRMATION_MAIL property.
     */
    public static boolean sendConfirmationMail() {
        boolean boolVal = false;
        try {
            InitialContext ic = new InitialContext();
            Boolean bool = (Boolean)
                ic.lookup(JNDINames.SEND_CONFIRMATION_MAIL);
            if (bool != null) {
                boolVal = bool.booleanValue();
            }
        } catch (NamingException ne) {
            // If property is not present in the deployment
            // descriptor, conservatively assume that we do not send
            // confirmation mail for each order.
            Debug.print(ne);
        }
        return boolVal;
    }
}


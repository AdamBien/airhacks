/*
 * $Id: LanguageChangeEvent.java,v 1.1.2.2 2001/03/15 00:40:00 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.event;

import java.io.Serializable;

/**
 * This event is sent from the web tier to the EJB Controller to notify
 * the EJB Controller that a user has changed the language which should
 * result in the shopping cart being refreshed.
 */
public class LanguageChangeEvent extends EStoreEventSupport {

    public LanguageChangeEvent() {}


}



/*
 * $Id: ScreenFlowData.java,v 1.1.2.3 2001/03/15 03:50:35 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import java.util.HashMap;

public class ScreenFlowData implements java.io.Serializable {

    private HashMap exceptionMappings;
    private HashMap screenDefinitionMappings;
    private String defaultScreen = null;
    private String signinScreen = null;
    private String signinErrorScreen = null;

    public ScreenFlowData (HashMap exceptionMappings,
                           HashMap screenDefinitionMappings,
                           String defaultScreen,
                           String signinScreen,
                           String signinErrorScreen) {
        this.exceptionMappings = exceptionMappings;
        this.screenDefinitionMappings = screenDefinitionMappings;
        this.defaultScreen = defaultScreen;
        this.signinScreen = signinScreen;
        this.signinErrorScreen = signinErrorScreen;
    }

    public String getSigninScreen() {
        return signinScreen;
    }

    public String getSigninErrorScreen() {
        return signinErrorScreen;
    }

    public String getDefaultScreen() {
        return defaultScreen;
    }

    public HashMap getScreenDefinitionMappings() {
        return screenDefinitionMappings;
    }

    public HashMap getExceptionMappings() {
        return exceptionMappings;
    }

    public String toString() {
        return "ScreenFlowData: {defaultScreen=" + defaultScreen + ", " +
                    " signinScreen=" + signinScreen + ", " +
                    " screenDefinitionMappings=" + screenDefinitionMappings + ", " +
                    " exceptionMappings=" + exceptionMappings + "}";
    }
}


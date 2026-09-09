/*
 * $Id: PopulateBean.java,v 1.1.2.3 2001/03/15 00:40:11 brydon Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.tools.populate.web;

import java.util.HashMap;

/**
 * This class holds data for the populate servlet for presentation.
 */
public class PopulateBean implements java.io.Serializable {

    private HashMap tables = null;
    private HashMap optionalTables = null;
    private String databaseProductName = null;
    private boolean databaseInstallationValid = false;
    private boolean optionalDatabaseInstallationValid = false;

    public PopulateBean() {}

    public void setRequiredTables(HashMap tables) {
        this.tables = tables;
    }

    public HashMap getRequiredTables() {
        return tables;
    }

    public void setOptionalTables(HashMap optionalTables) {
        this.optionalTables = optionalTables;
    }

    public HashMap getOptionalTables() {
        return optionalTables;
    }

    public void setDatabaseProductName(String databaseProductName) {
        this.databaseProductName = databaseProductName;
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public boolean isOptionalDatabaseInstallationValid() {
        return optionalDatabaseInstallationValid;
    }
    public boolean isDatabaseInstallationValid() {
        return databaseInstallationValid;
    }

    public void setDatabaseInstallationValid(boolean databaseInstallationValid) {
        this.databaseInstallationValid = databaseInstallationValid;
    }

    public void setOptionalDatabaseInstallationValid(boolean optionalDatabaseInstallationValid) {
        this.optionalDatabaseInstallationValid = optionalDatabaseInstallationValid;
    }
}

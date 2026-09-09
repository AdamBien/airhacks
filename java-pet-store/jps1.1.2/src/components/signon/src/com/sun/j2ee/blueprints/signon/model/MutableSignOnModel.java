/*
 * $Id: MutableSignOnModel.java,v 1.1.2.1 2001/03/10 10:12:33 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.signon.model;

import com.sun.j2ee.blueprints.signon.model.SignOnModel;

/**
 * This class represents the model data for the
 * sign on info. Note that this object is mutable
 * and will be used by the signon EJB only
 */
public class MutableSignOnModel extends SignOnModel {

    public MutableSignOnModel(String id, String pwd) {
        super(id, pwd);
    }

    public void setPassWord(String pwd) {
        password = pwd;
    }
}

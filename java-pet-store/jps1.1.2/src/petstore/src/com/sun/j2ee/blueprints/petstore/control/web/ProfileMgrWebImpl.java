/*
 * $Id: ProfileMgrWebImpl.java,v 1.14.4.6 2001/03/16 19:56:19 gmurray Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.petstore.control.web;

import com.sun.j2ee.blueprints.personalization.profilemgr.model.ProfileMgrModel;
import com.sun.j2ee.blueprints.personalization.profilemgr.ejb.ProfileMgr;
import com.sun.j2ee.blueprints.petstore.control.web.ModelManager;
import com.sun.j2ee.blueprints.petstore.control.web.ModelUpdateListener;
import com.sun.j2ee.blueprints.petstore.util.JNDINames;
import com.sun.j2ee.blueprints.petstore.util.EJBUtil;

import java.rmi.RemoteException;
import com.sun.j2ee.blueprints.personalization.profilemgr.exceptions.ProfileMgrAppException;

import com.sun.j2ee.blueprints.petstore.control.exceptions.GeneralFailureException;
import com.sun.j2ee.blueprints.petstore.control.exceptions.EStoreAppException;

/**
 * This class is the web-tier representation of the ProfileMgr.
 */
public class ProfileMgrWebImpl extends ProfileMgrModel
    implements ModelUpdateListener, java.io.Serializable {

    private ModelManager mm;
    private ProfileMgr proEjb;

    public ProfileMgrWebImpl(ModelManager mm) {
        super(null, null);
        this.mm = mm;
        mm.addListener(JNDINames.PROFILEMGR_EJBHOME, this);
    }

    public ProfileMgrWebImpl() {
        super();
        // do nothing.  used for JSP useBean directive.
    }

    public void performUpdate() throws EStoreAppException {
        // Get data from the EJB
        if (proEjb == null) {
            proEjb = mm.getProfileMgrEJB();
        }
        try {
            if (proEjb != null) copy(proEjb.getDetails());
        } catch (RemoteException re) {
            throw new GeneralFailureException(re.getMessage());
        }
    }

    public String getBanner(String favCategory) throws ProfileMgrAppException{
        ProfileMgrModel proMod = new ProfileMgrModel();
        return proMod.getBanner(favCategory);
    }

}




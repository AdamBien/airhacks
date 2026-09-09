/*
 * $Id: MutableProfileMgrModel.java,v 1.1.2.1 2001/03/10 10:12:32 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.model;

import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ProfileMgrModel;

/**
 * MutableProfileMgrModel is a class which models personal preferences data
 * for a particular user with the additional facility to change the data
 */
public class MutableProfileMgrModel extends ProfileMgrModel {

    /**
     * Class constructor specifying the user and preference information.
     * @param userId    a string which represents the id of this user
     * @param eInfo     an <code>ExplicitInformation</code> structure
     *                  containing the personal preferences of this user
     */
    public MutableProfileMgrModel(String userId, ExplicitInformation eInfo) {
        super(userId, eInfo);
    }

    /**
     * Sets the string which represents the id of this user.
     * @param  the string representing this user id
     */
    public void setUserId(String id) {
        this.userId = id;
    }

    /**
     * Sets the <code>ExplicitInformation</code> structure which contains the
     * personal preferences for this user.
     * @param  the <code>ExplicitInformation</code> object for this user
     */
    public void setExplicitInformation(ExplicitInformation ein) {
        this.eInfo = ein;
    }
}

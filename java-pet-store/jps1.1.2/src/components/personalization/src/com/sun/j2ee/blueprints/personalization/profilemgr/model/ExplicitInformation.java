/*
 * $Id: ExplicitInformation.java,v 1.1.2.2 2001/03/13 06:57:16 vijaysr Exp $
 * Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 * Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
 */

package com.sun.j2ee.blueprints.personalization.profilemgr.model;

import java.io.Serializable;

/**
 * The ExplicitInformation class encapsulates the personal preferences
 * profile data specified by a particular user.
 */
public class ExplicitInformation implements Serializable{

    private String langPref;
    private String favCategory;
    private boolean myListOpt;
    private boolean bannerOpt;

    /**
     * Class constructor specifying the user's personal preference information
     * details.
     * @param langPref      a string which represents the language preference
     *                      for this user
     * @param favCategory   a string which represents the favorite pet category
     *                      for this user
     * @param myListOpt     a boolean <code>true</code> indicates the user
     *                      wants to view more of their favorite pets while at
     *                      their shopping cart.
     *                      Otherwise a boolean of <code>false</code> disables
     *                      this feature.
     * @param bannerOpt     a boolean <code>false</code> indicated the user
     *                      wants to view informational messages basedon their
     *                      favorite pets while shopping.
     *                      Otherwise a boolean of <code>false</code> disables
     *                      this feature.
     */
    public ExplicitInformation(String langPref, String favCategory,
                boolean myListOpt, boolean bannerOpt){

        this.langPref = langPref;
        this.favCategory = favCategory;
        this.myListOpt = myListOpt;
        this.bannerOpt = bannerOpt;
    }

    /**
     * Gets the language preference for this user.
     * @returns langPref    a string which represents the language preference
     *                      for this user
     */
    public String getLangPref(){
        return langPref;
    }

    /**
     * Gets the favorite pet category for this user.
     * @returns favCategory a string which represents the favorite pet category
     * for this user
     */
    public String getFavCategory(){
        return favCategory;
    }

    /**
     * Gets whether this user has enabled/disabled the my list option.
     * @returns myListOpt   a boolean which indicates the my list option -
     *                      enabled/disabled
     */
    public boolean getMyListOpt(){
        return myListOpt;
    }

    /**
     * Gets whether this user has enabled/disabled the banner option.
     * @returns bannerOpt   a boolean which indicates the banner option -
     *                      enabled/disabled
     */
    public boolean getBannerOpt(){
        return bannerOpt;
    }

    /**
     * Creates a string which represents the personal preferences profile for
     * this user.
     * @returns string   a human readable string which summarizes this users
     *                   profile
     */
    public String toString(){
        return "[langPref=" + langPref +
            ",  favCatetory=" + favCategory +
            ", myListOpt=" + myListOpt +
            ", bannerOpt=" + bannerOpt +  "]";
    }
}

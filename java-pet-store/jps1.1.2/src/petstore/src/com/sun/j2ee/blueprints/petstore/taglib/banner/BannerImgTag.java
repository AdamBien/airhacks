package com.sun.j2ee.blueprints.petstore.taglib.banner;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.IOException;
import com.sun.j2ee.blueprints.petstore.control.web.ProfileMgrWebImpl;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;

import com.sun.j2ee.blueprints.util.tracer.Debug;


/*
 * BannerImgTag
 * ------------
 * This tag, which should be nested inside a banner tag, retrieves the banner
 * url from the outer banner tag and prints out the url to the JSP page. See
 * BannerTag.java for an example of how to use this tag.  An exception is
 * thrown if the tag is not inside a banner tag.
 */

public class BannerImgTag extends TagSupport {
  public int doStartTag() throws JspTagException {

    // check if bannerImg tag is inside banner tag
    BannerTag bannerTag =
      (BannerTag) findAncestorWithClass(this, BannerTag.class);
    if (bannerTag == null) {
      throw new JspTagException("BannerImgTag: bannerImg tag not inside" +
                                "banner tag");
    }

    // print out url
    try {
      JspWriter out = pageContext.getOut();
      out.print(bannerTag.getBannerString());
    } catch(IOException ioe) {
      Debug.println("BannerImgTag: Error printing banner image url: " +
                         ioe);
    }

    // there should be no body to process
    return(SKIP_BODY);
  }
}

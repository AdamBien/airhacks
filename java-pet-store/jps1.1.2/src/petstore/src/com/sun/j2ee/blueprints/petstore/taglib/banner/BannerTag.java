package com.sun.j2ee.blueprints.petstore.taglib.banner;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import com.sun.j2ee.blueprints.petstore.control.web.ProfileMgrWebImpl;
import com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation;

/*
 * BannerTag
 * ---------
 * On hitting the start tag, attempts to find the ProfileMgrWebImpl bean and
 * and determine if the banner option is on.  If the profilemgr bean exists
 * and the banner option is on, the body is processed.  A method is also
 * provided for inner tags (i.e. BannerImgTag) to retrieve the banner string.
 * All accesses to the profileMgr bean happen in this tag.  If it is not found
 * a Jsp exception is not thrown.  Rather, the body is just not included.
 *
 * The banner tag might be used like this, assuming the tag library is
 * identified with "j2ee", and the profilemgr bean is initialized and present
 * in the jsp page:
 *
 * <j2ee:banner>
 *  <img src="<%=request.getContextPath()%>/<j2ee:bannerImg />">
 * </j2ee:banner>
 */

public class BannerTag extends TagSupport {
  private ProfileMgrWebImpl profileMgrBean;

  public int doStartTag() {
    profileMgrBean =
      (ProfileMgrWebImpl) pageContext.findAttribute("profilemgr");
    if (profileMgrBean == null) return(SKIP_BODY);
    ExplicitInformation eInfo = profileMgrBean.getExplicitInformation();
    if (!eInfo.getBannerOpt()) return(SKIP_BODY);
    else return(EVAL_BODY_INCLUDE);
  }

  public String getBannerString() {
    //return(profileMgrBean.getBanner());
    return "BannerTag not used";
  }
}

<%--
 % $Id: petfooter.jsp,v 1.4.4.3 2001/03/15 00:40:14 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % Displays the pet footer
--%>

<%@ page import="com.sun.j2ee.blueprints.personalization.profilemgr.model.ExplicitInformation" %>

<jsp:useBean
  id="customer"
  class="com.sun.j2ee.blueprints.petstore.control.web.CustomerWebImpl"
  scope="session"
/>
<jsp:useBean
  id="profilemgr"
  class="com.sun.j2ee.blueprints.petstore.control.web.ProfileMgrWebImpl"
  scope="session"
/>

<%
  if (customer.isLoggedIn()) {
    ExplicitInformation explicitInfo = profilemgr.getExplicitInformation();
    String favCat = explicitInfo.getFavCategory().toLowerCase();
    if(explicitInfo.getBannerOpt()) {
%>

<table width="100%" cellspacing="0" border="0"
  background="<%=request.getContextPath()%>/images/bkg-topbar.gif">
  <tr>
    <td align="center" border="0">
      <%= profilemgr.getBanner(favCat)%>
    </td>
  </tr>
</table>

<%
    }
  }
%>

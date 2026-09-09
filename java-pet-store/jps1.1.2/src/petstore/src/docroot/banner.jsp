<%--
 % $Id: banner.jsp,v 1.5.4.3 2001/03/16 19:56:21 gmurray Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % The banner to be displayed at the top of each screen.  This
 % includes the site logo, search, category navigation bar etc.
--%>

<jsp:useBean
  id="customer"
  type="com.sun.j2ee.blueprints.petstore.control.web.CustomerWebImpl"
  scope="session"
/>

<%@ include file="mouseover.js" %>

<script language="JavaScript">

<!--
// load all our images here
if (document.images) {

  img_help_off = new Image();
  img_help_off.src = '<%= request.getContextPath() %>/images/help.gif';
  img_help_on = new Image();
  img_help_on.src = '<%= request.getContextPath() %>/images/helpHL.gif';

  img_signin_off = new Image();
  img_signin_off.src = '<%= request.getContextPath() %>/images/sign-in.gif';
  img_signin_on = new Image();
  img_signin_on.src = '<%= request.getContextPath() %>/images/sign-inHL.gif';

  img_signout_off = new Image();
  img_signout_off.src = '<%= request.getContextPath() %>/images/sign-out.gif';
  img_signout_on = new Image();
  img_signout_on.src = '<%= request.getContextPath() %>/images/sign-outHL.gif';

  img_search_off = new Image();
  img_search_off.src = '<%= request.getContextPath() %>/images/search.gif';
  img_search_on = new Image();
  img_search_on.src = '<%= request.getContextPath() %>/images/searchHL.gif';

  img_myaccount_off = new Image();
  img_myaccount_off.src = '<%= request.getContextPath() %>/images/my_account.gif';
  img_myaccount_on = new Image();
  img_myaccount_on.src = '<%= request.getContextPath() %>/images/my_accountHL.gif';

  img_cart_off = new Image();
  img_cart_off.src = '<%= request.getContextPath() %>/images/cart.gif';
  img_cart_on = new Image();
  img_cart_on.src = '<%= request.getContextPath() %>/images/cartHL.gif';

  img_fish_off = new Image();
  img_fish_off.src = '<%= request.getContextPath() %>/images/fish.gif';
  img_fish_on = new Image();
  img_fish_on.src = '<%= request.getContextPath() %>/images/fishHL.gif';

  img_dogs_off = new Image();
  img_dogs_off.src = '<%= request.getContextPath() %>/images/dogs.gif';
  img_dogs_on = new Image();
  img_dogs_on.src = '<%= request.getContextPath() %>/images/dogsHL.gif';

  img_reptiles_off = new Image();
  img_reptiles_off.src = '<%= request.getContextPath() %>/images/reptiles.gif';
  img_reptiles_on = new Image();
  img_reptiles_on.src = '<%= request.getContextPath() %>/images/reptilesHL.gif';

  img_cats_off = new Image();
  img_cats_off.src = '<%= request.getContextPath() %>/images/cats.gif';
  img_cats_on = new Image();
  img_cats_on.src = '<%= request.getContextPath() %>/images/catsHL.gif';

  img_birds_off = new Image();
  img_birds_off.src = '<%= request.getContextPath() %>/images/birds.gif';
  img_birds_on = new Image();
  img_birds_on.src = '<%= request.getContextPath() %>/images/birdsHL.gif';
}

// -->
</script>

<table width="100%" cellspacing="0" border="0"
  background="<%=request.getContextPath()%>/images/bkg-topbar.gif">
  <tr>
    <td>
      <a href="main"><image src="<%=request.getContextPath()%>/images/logo-topbar.gif" border="0"></a>
    </td>
    <td align="left">
      <form action="<%=request.getContextPath()%>/control/search">
        <font color="cyan" size="-1">What are you looking for?</font>
        <br>
        <input type="text" size="14" name="search_text">
        <input type="image" border="0"
          src="<%=request.getContextPath()%>/images/search.gif"
          name="search">
      </form>
    </td>
    <td align="right">
      <a href="<%=request.getContextPath()%>/control/cart" onmouseover="img_on('cart')" onmouseout="img_off('cart')"><img src="<%=request.getContextPath()%>/images/cart.gif" border="0" name="img_cart"></a>
      <img src="<%=request.getContextPath()%>/images/separator.gif" border="0">

<%
  if (!customer.isLoggedIn()) {
%>

      <a href="<%=request.getContextPath()%>/control/signin" onmouseover="img_on('signin')" onmouseout="img_off('signin')"><img src="<%=request.getContextPath()%>/images/sign-in.gif" border="0" name="img_signin"></a>

<% } else{  %>

      <a href="<%=request.getContextPath()%>/control/signout" onmouseover="img_on('signout')" onmouseout="img_off('signout')"><img src="<%=request.getContextPath()%>/images/sign-out.gif" border="0" name="img_signout"></a>
      <img src="<%=request.getContextPath()%>/images/separator.gif" border="0">
      <a href="<%=request.getContextPath()%>/control/editaccount" onmouseover="img_on('myaccount')" onmouseout="img_off('myaccount')"><img src="<%=request.getContextPath()%>/images/my_account.gif" border="0" name="img_myaccount"></a>

<% } %>
      <img src="<%=request.getContextPath()%>/images/separator.gif" border="0">
      <a href="<%=request.getContextPath()%>/control/help" onmouseover="img_on('help')" onmouseout="img_off('help')"><img src="<%=request.getContextPath()%>/images/help.gif" border="0" name="img_help"></a>
    </td>
  </tr>
</table>

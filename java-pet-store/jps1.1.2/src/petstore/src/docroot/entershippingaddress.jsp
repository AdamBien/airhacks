<%--
 % $Id: entershippingaddress.jsp,v 1.2.4.1 2001/03/15 00:40:13 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % prompts for address information, applies it to the shipping
 % address.
--%>

<p>
<form action="validateshippinginformation">
  Please enter the name and address to which you would like your
  order shipped.
  <p>
    <%@ include file="addressform.html" %>
    <p>
    <input type="image" src="../images/button_cont.gif" value=
      "Continue" border="0">
</form>

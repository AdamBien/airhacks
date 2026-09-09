<%--
 % $Id: entershippingaddress.jsp,v 1.2 2000/09/20 04:50:09 brydon Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits rå“ervå“.
--%>

<%--
 % prompts for address information, applies it to the shipping
 % address.
--%>
<%@ page contentType="text/html;charset=SJIS" %>

<p>
<form action="validateshippinginformation">
  ‚ ‚È‚½‚Ì‚²’•¶‚Ì”­‘—æ‚ÌZŠ‚Æ–¼‘O‚ð“ü—Í‚µ‚Ä‰º‚³‚¢B
  <p>
    <%@ include file="/addressform.html" %>
    <p>
    <input type="image" src="../images/button_cont.gif" value=
      "Continue" border="0">
</form>

<%--
 % $Id: createnewaccount.jsp,v 1.5.4.2 2001/03/15 22:53:31 gmurray Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % Form that prompts the user for account details required at the time
 % of creating a new account.
--%>

<form action="validatenewaccount">
  <input type="hidden" name="action" value="createAccount">
  <p>
    <font size="5" color="green">
      Account Information:
    </font>
  <p>
  <table border ="0">
    <tr>
      <td>User ID:</td>
      <td align="left">
        <input type="text" size="15" name="user_name" maxlength="20">
      </td>
    </tr>
    <tr>
      <td>Password:</td>
      <td align="left">
        <input type="password" size="15" name="password" maxlength="20">
      </td>
    </tr>
    <tr>
      <td>E-Mail Address:</td>
      <td align="left">
        <input type="text" size="30" name="user_email" maxlength="70">
      </td>
    </tr>
  </table>
  <p>
  <p>
  <p>
    <font size="5" color="green">Address:</font>
  <p>
    <%@ include file="addressform.html"%>
  <p>
    <font size="5" color="green">Preferences:</font>
  <p>
    <%@ include file="preferencesform.html"%>
  <p>
    <input type="image" border="0" name="Submit"
      src="<%=request.getContextPath()%>/images/button_submit.gif">
</form>

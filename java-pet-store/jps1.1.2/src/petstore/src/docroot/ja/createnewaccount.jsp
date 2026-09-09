<%--
 % $Id: createnewaccount.jsp,v 1.5 2000/09/20 04:50:09 brydon Exp $
 % Copyright 2000 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2000 Sun Microsystems, Inc. Tous droits r蜩erv蜩.
--%>

<%--
 % Form that prompts the user for account details required at the time
 % of creating a new account.
--%>
<%@ page contentType="text/html;charset=SJIS" %>

<form action="validatenewaccount">
  <input type="hidden" name="action" value="createAccount">
  <p>
    <font size="5" color="green">
      顧客情報:
    </font>
  <p>
  <table border ="0">
    <tr>
      <td>ユーザ ID:</td>
      <td align="left">
        <input type="text" size="15" name="user_name" maxlength="20">
      </td>
    </tr>
    <tr>
      <td>パスワード:</td>
      <td align="left">
        <input type="password" size="15" name="password" maxlength="20">
      </td>
    </tr>
    <tr>
      <td>顧客タイプ:</td>
      <td align="left">
        <select  name = "user_type">
          <option value="default">Home Consumer
          <option value="business">Commercial Business
          <option value="whole_seller">Whole Seller
        </select>
      </td>
    </tr>
    <tr>
      <td>E-Mail アドレス:</td>
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
    <jsp:include page="addressform.jsp" flush="true" />
  <p>
    <font size="5" color="green">Preferences:</font>
  <p>
    <jsp:include page="preferencesform.jsp" flush="true"/>
  <p>
    <input type="image" border="0" name="Submit"
      src="<%=request.getContextPath()%>/images/button_submit.gif">
</form>

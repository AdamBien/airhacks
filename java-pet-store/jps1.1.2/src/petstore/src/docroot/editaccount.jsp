<%--
 % $Id: editaccount.jsp,v 1.12.4.3 2001/03/15 00:40:13 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<%--
 % Form that prompts the user for account details required at the time
 % of editing the user's account.
--%>

<%@ page import="com.sun.j2ee.blueprints.customer.util.ContactInformation" %>
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

<form action="updateaccount">
  <input type="hidden" name="action" value="updateAccount">
  <p>
    <font size="5" color="green">
      Account Information:
    </font>
  <p>

<%
  if (customer.isLoggedIn()) {
%>

  <table border ="0">
    <tr>
      <td>User ID:</td>
      <td align="left">
        <%= customer.getUserId() %>
        <input type="hidden" name="user_name"
          value="<%= customer.getUserId() %>">
      </td>
    </tr>

    <% ContactInformation contact = customer.getContactInformation(); %>

    <tr>
      <td>E-Mail Address:</td>
      <td align="left">
        <input type="text" size="30" name="user_email" maxlength="70"
          value="<%= contact.getEMail() %>">
      </td>
    </tr>
  </table>
  <p>
  <p>
  <p>
    <font size="5" color="green">Address:</font>
  <p>
    <%@ include file="changeaddressform.jsp"%>
  <p>
    <font size="5" color="green">Preferences:</font>
  <p>
    <% ExplicitInformation explicitInfo = profilemgr.getExplicitInformation(); %>
    <%@ include file="changepreferencesform.jsp"%>

  <p>
    <input type="image" border="0" name="Submit"
      src="<%=request.getContextPath()%>/images/button_submit.gif">

<%
  } else {
%>

  <p>
    You need to sign in before you can edit your account information.
  </p>

<%
  }
%>
</form>

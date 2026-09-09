<%--
 % $Id: changeaddressform.jsp,v 1.6 2000/09/28 05:59:35 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits rÂìervÂì.
--%>
<%@ page contentType="text/html;charset=SJIS" %>
<%@ page import="com.sun.j2ee.blueprints.customer.util.ContactInformation" %>

<%
ContactInformation contact = (ContactInformation)request.getAttribute("contact");
%>
<table>
  <tr>
    <td align="right">ñºéö:</td>
    <td align="left" colspan="2">
      <input type="text" name="family_name" size="30" maxlength="30"
        value="<%=contact.getFamilyName()%>">
    </td>
  </tr>
  <tr>
    <td align="right">ñºëO:</td>
    <td align="left" colspan="2">
      <input type="text" name="given_name" size="30" maxlength="30"
        value="<%=contact.getGivenName()%>">
    </td>
  </tr>
  <tr>
    <td align="right">í¨ñºî‘ín:</td>
    <td align="left" colspan="2">
      <input type="text" name="address_1" size="55" maxlength="70"
        value="<%=contact.getAddress().getStreetName1()%>">
    </td>
  </tr>
  <tr>
    <td></td>
    <td align="left" colspan="2">
      <input type="text" name="address_2" size="55" maxlength="70"
        value="<%=contact.getAddress().getStreetName2()%>">
    </td>
  </tr>
  <tr>
    <td align="right">ésí¨ë∫ñº:</td>
    <td align="left" colspan ="2">
      <input type="text" name="city" size="55" maxlength="70"
        value="<%=contact.getAddress().getCity()%>">
    </td>
  </tr>

  <% String state = contact.getAddress().getState(); %>

  <tr>
    <td>èBñº/ìsìπï{åßñº:</td>
    <td align="left">
      <select size="1" name="state_or_province">
        <option
          value="ìåãûìs"
          <% if (state.equals("ìåãûìs") ||
                 state.equals("ìåãûìs")) { %> selected <% } %>
          >ìåãûìs</option>
        <option
          value="ëÂç„ï{"
          <% if (state.equals("ëÂç„ï{") ||
                 state.equals("ëÂç„ï{")) { %> selected <% } %>
          >ëÂç„ï{</option>
        <option
          value="í∑ñÏåß"
          <% if (state.equals("í∑ñÏåß") |
                 state.equals("í∑ñÏåß")) { %> selected <% } %>
          >í∑ñÏåß</option>
      </select>
    </td>
    <td>óXï÷î‘çÜ:
      <input type="text" name="postal_code" size="12" maxlength="12"
        value="<%=contact.getAddress().getZipCode()%>">
    </td>
  </tr>

  <% String country = contact.getAddress().getCountry(); %>

  <tr>
    <td>çëñº:</td>
    <td align="left" colspan ="2">
      <select size="1" name="country">
        <option
          value="USA"
          <% if (country.equals("USA")) { %> selected <% } %>
          >USA</option>
        <option
          value="Canada"
          <% if (country.equals("Canada")) { %> selected <% } %>
          >Canada</option>
        <option
          value="Japan"
          <% if (country.equals("Japan")) { %> selected <% } %>
          >Japan</option>
      </select>
    </td>
  </tr>
  <tr>
    <td>ìdòbî‘çÜ:</td>
    <td align="left" colspan ="2">
      <input type="text" name="telephone_number" size="12" maxlength="70"
        value="<%=contact.getTelephone()%>">
    </td>
  </tr>
</table>

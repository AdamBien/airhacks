<%--
 % $Id: changeaddressform.jsp,v 1.6.4.1 2001/03/15 00:40:12 brydon Exp $
 % Copyright 2001 Sun Microsystems, Inc. All rights reserved.
 % Copyright 2001 Sun Microsystems, Inc. Tous droits réservés.
--%>

<table>
  <tr>
    <td align="right">First Name:</td>
    <td align="left" colspan="2">
      <input type="text" name="given_name" size="30" maxlength="30"
        value="<%=contact.getGivenName()%>">
    </td>
  </tr>
  <tr>
    <td align="right">Last Name:</td>
    <td align="left" colspan="2">
      <input type="text" name="family_name" size="30" maxlength="30"
        value="<%=contact.getFamilyName()%>">
    </td>
  </tr>
  <tr>
    <td align="right">Street Address:</td>
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
    <td align="right">City:</td>
    <td align="left" colspan ="2">
      <input type="text" name="city" size="55" maxlength="70"
        value="<%=contact.getAddress().getCity()%>">
    </td>
  </tr>

  <% String state = contact.getAddress().getState(); %>

  <tr>
    <td>State/Province:</td>
    <td align="left">
      <select size="1" name="state_or_province">
        <option
          value="California"
          <% if (state.equals("CA") ||
                 state.equals("California")) { %> selected <% } %>
          >California</option>
        <option
          value="New York"
          <% if (state.equals("NY") ||
                 state.equals("New York")) { %> selected <% } %>
          >New York</option>
        <option
          value="Texas"
          <% if (state.equals("TX") ||
                 state.equals("Texas")) { %> selected <% } %>
          >Texas</option>
      </select>
    </td>
    <td>Postal Code:
      <input type="text" name="postal_code" size="12" maxlength="12"
        value="<%=contact.getAddress().getZipCode()%>">
    </td>
  </tr>

  <% String country = contact.getAddress().getCountry(); %>

  <tr>
    <td>Country:</td>
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
    <td>Telephone Number:</td>
    <td align="left" colspan ="2">
      <input type="text" name="telephone_number" size="12" maxlength="70"
        value="<%=contact.getTelephone()%>">
    </td>
  </tr>
</table>
